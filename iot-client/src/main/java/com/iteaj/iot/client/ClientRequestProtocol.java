package com.iteaj.iot.client;

import com.iteaj.network.*;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.*;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientResponseBody;
import com.iteaj.network.client.app.AppClientUtil;
import com.iteaj.network.consts.ExecStatus;
import io.netty.channel.ChannelFuture;

import java.nio.channels.ClosedChannelException;
import java.util.Optional;
import java.util.concurrent.*;

public abstract class ClientRequestProtocol<C extends ClientMessage> extends AbstractClientProtocol<C> implements ClientRelation<ClientRequestProtocol> {

    private long timeout = 10; // 超时时间, 默认10秒
    private boolean clientStart;
    private ClientRelationEntity clientEntity;
    private FreeProtocolHandle freeProtocolHandle; // 自定义协议处理器
    private ClientProtocolHandle defaultProtocolHandle; // 默认协议处理器

    public ClientRequestProtocol() {
        super(null);
    }

    public ClientRequestProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    public C requestMessage() {
        return (C) super.requestMessage();
    }

    @Override
    public C responseMessage() {
        return (C) super.responseMessage();
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        ProtocolHandle protocolService = getFreeProtocolHandle();
        if(protocolService == null) {
            protocolService = factory.getProtocolHandle(getClass());
        }

        return this.exec(protocolService);
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        try {
            if(this.isAsyncExec()) { // 需要异步执行
                if(business != null) {
                    this.getExecutor().execute(() -> {
                        Object respObject = business.business(this);

                        // 验证是否需要响应报文给应用程序客户端
                        if(isClientStart()) {
                            this.appClientHandle(respObject);
                        }
                    });
                } else {
                    // 验证是否需要响应报文给应用程序客户端
                    if(isClientStart()) {
                        this.appClientHandle(null);
                    }
                }

            } else {
                Object respObject = null;
                if(business != null) {
                    respObject = business.business(this);
                }

                // 验证是否需要响应报文给应用程序客户端
                if(isClientStart()) {
                    this.appClientHandle(respObject);
                }
            }
        } catch (Exception exception) {
            logger.error("执行业务异常： {}", exception.getMessage(), exception);
        } finally {

            //如果是同步调用必须释放锁
            releaseLock();

            return null;
        }
    }

    /**
     * @see #clientStart true 说明此次请求是由应用程序客户端发起的调用
     * @see ClientRelation#getClientEntity() 返回的值不为空, 说明需要响应调用的应用客户端
     * @param clientResp
     */
    protected void appClientHandle(Object clientResp) {
        ClientRelationEntity clientEntity = this.getClientEntity();

        if(clientEntity != null) {
            AppClientResponseBody responseBody;
            if(clientResp instanceof AppClientResponseBody) {
                responseBody = (AppClientResponseBody) clientResp;
            } else {
                responseBody = new AppClientResponseBody(getExecStatus().desc, getExecStatus());
            }

            AppClientMessage<AppClientResponseBody> serverResponseMessage = AppClientUtil.buildServerResponseMessage(clientEntity, responseBody);
            AbstractProtocol abstractProtocol = new CommonWriteProtocol(serverResponseMessage);
            ChannelFuture channelFuture = IotClientBootstrap.deviceManager.writeAndFlush(clientEntity.getEquipCode(), abstractProtocol);
            channelFuture.addListener(future -> {
                String msg = future.isSuccess() ? "成功" : "失败";
                if(logger.isDebugEnabled()) {
                    logger.debug("平台响应应用客户端({}) HASH: {} - 客户设备: {} - 操作设备: {} - 操作协议: {}", msg
                            , hashCode(), clientEntity.getEquipCode(), null, clientEntity.getTradeType());
                }
            });
        } else {
            /**
             * 此次调用是否由应用程序客户端发起的
             * @see AppClientServerProtocol
             * @see com.iteaj.network.client.AppClientServerService#doBusiness(AppClientServerProtocol)
             * @see ClientRelation#isClientStart()
             */
            if(logger.isDebugEnabled()) {
                logger.debug("应用客户端协议处理 无需响应客户端 - 协议类型: {} - 说明: 此协议由应用程序客户端发起的请求调用", getClass().getSimpleName());
            }
        }
    }

    @Override
    public abstract ClientRequestProtocol buildRequestMessage();

    /**
     * 平台主动向外发起请求
     * @return 只有在同步的时候{@link #sync(long)}会返回对象, 如果是异步返回 Null
     */
    protected <T extends ClientRequestProtocol> T sendRequest() throws ProtocolException {
        try {
            /**
             *  如果没有指定协议业务 and 不是同步请求, 则去除超时时间
             * @see #isRelation() 返回 false
             * @see #isSyncRequest() 同步请求必须指定超时时间
             */
            if(!isSyncRequest() && !isProtocolHandle()) {
                this.timeout = 0;
            }

            ClientRequestProtocol requestProtocol = buildRequestMessage();
            if(requestProtocol == null) {
                throw new ProtocolException("构建协议失败");
            }

            if(requestProtocol.requestMessage() == null) {
                throw new ProtocolException("构建请求报文失败");
            }

            IotNettyClient client = getIotNettyClient();
            ChannelFuture request = client.writeAndFlush(requestProtocol);

            if(request != null) {
                if(getTimeout() > 0) {
                    request.get(getTimeout(), TimeUnit.SECONDS); // 此处用来等待报文发送成功
                }
            } else {
                throw new ProtocolException("请求失败");
            }

            /**
             * 是同步请求
             */
            if(isSyncRequest()) {
                // 同步请求必须验证超时时间
                if(getTimeout() <=0) {
                    throw new ProtocolException("同步请求必须设置的超时时间必须大于0(s)");
                }

                boolean await = getDownLatch().await(getTimeout(), TimeUnit.SECONDS);// 如果发送成功等待报文响应

                if(!await) {
                    setExecStatus(ExecStatus.超时);
                }

                return (T) this;
            }

        } catch (InterruptedException e) {
            throw new ProtocolException("同步中断", e);
        } catch (ExecutionException e) {
            if(e.getCause() instanceof ClosedChannelException) {
                throw new ProtocolException("连接异常, 通道关闭", e.getCause());
            } else {
                throw new ProtocolException("请求失败", e);
            }
        } catch (TimeoutException e) {
            throw new ProtocolException("网络异常, 请求超时", e);
        }

        return null; // 只有同步请求的时候才有返回值
    }

    /**
     * 平台主动发起请求
     * @param service 需要处理的业务
     * @return 只有在同步的时候{@link #sync(long)}会返回对象, 如果是异步返回 Null
     */
    public <T extends ClientRequestProtocol> T request(FreeProtocolHandle<T> service) throws ProtocolException{
        this.freeProtocolHandle = service;
        if(this.getFreeProtocolHandle() == null) {
            throw new ProtocolException("协议业务对象不能为Null");
        }

        // 指定要执行的业务后, 必须指定超时时间
        validateTimeout(getTimeout());

        return (T) this.request();
    }

    @Override
    public ClientRequestProtocol request() throws ProtocolException {
        return this.sendRequest();
    }

    protected void validateTimeout(long timeout) {
        if(timeout < 0) {
            throw new ProtocolException("超时时间(timeout)必须大于0(s)");
        }

        this.timeout = timeout;
    }

    protected Executor getExecutor() {
        return IotClientBootstrap.taskExecutor;
    }

    @Override
    public final AbstractProtocol buildResponseMessage() {
        try {
            // 超时的报文不需要构建, 直接执行业务
            if(this.getExecStatus() != ExecStatus.超时) {
                doBuildResponseMessage(responseMessage());
            }

            if(isAsyncExec()) { // 异步执行业务
                getExecutor().execute(()->{
                    exec(IotClientBootstrap.businessFactory);
                });
            } else { // 同步执行业务
                exec(IotClientBootstrap.businessFactory);
            }

        } finally {
            if(isSyncRequest()) { // 如果是同步请求必须释放锁
                this.releaseLock();
            }
        }

        return this;
    }

    @Override
    public boolean isClientStart() {
        return this.clientStart;
    }

    public ClientRequestProtocol<C> setClientStart(boolean clientStart) {
        this.clientStart = clientStart;
        return this;
    }

    @Override
    public ClientRelationEntity getClientEntity() {
        return this.clientEntity;
    }

    @Override
    public void setClientEntity(ClientRelationEntity entity) {
        this.clientEntity = entity;
    }

    /**
     * @param message 可能会为空
     */
    public abstract void doBuildResponseMessage(C message);

    protected abstract IotNettyClient getIotNettyClient();

    /**
     * 此客户端是否激活
     * @return
     */
    public boolean isActive() {
        return getIotNettyClient().getChannel().isActive();
    }

    /**
     * 先和服务端尝试链接, 如果需要的话
     * @return
     */
    public Optional<ClientRequestProtocol> activeIfNeed() {
        // 如果已经激活, 直接发送数据
        if (!this.isActive()) {
            // 没有激活, 先尝试激活
            try {
                getIotNettyClient().doConnect(10);
            } catch (ProtocolException e) {
                return Optional.ofNullable(null);
            }

        }
        return Optional.of(this);
    }

    /**
     * 同步请求
     * @param timeout 同步超时时间
     * @return
     */
    public ClientRequestProtocol sync(long timeout) {
        validateTimeout(timeout);
        setDownLatch(new CountDownLatch(1));
        return this;
    }

    /**
     * 设置异步超时时间
     * @see com.iteaj.network.AbstractProtocolTimeoutManager 超时报文管理
     * @param timeout 超时时间
     * @return
     */
    public ClientRequestProtocol timeout(long timeout) {
        validateTimeout(timeout);
        return this;
    }

    protected String getMessageId() {
        ClientMessage message = requestMessage();
        return message.getMessageId();
    }

    @Override
    public boolean isRelation() {
        return getTimeout() > 0 && relationKey() != null;
    }

    /**
     * 是否有业务处理器
     * @return
     */
    protected boolean isProtocolHandle() {
        return this.getFreeProtocolHandle() != null
                || this.getDefaultProtocolHandle() != null;
    }

    @Override
    public String relationKey() {
        return getMessageId();
    }

    public long getTimeout() {
        return timeout;
    }

    public FreeProtocolHandle getFreeProtocolHandle() {
        return freeProtocolHandle;
    }

    public ClientProtocolHandle getDefaultProtocolHandle() {
        if(defaultProtocolHandle == null) {
            defaultProtocolHandle = (ClientProtocolHandle) IotClientBootstrap
                    .businessFactory.getProtocolHandle(getClass());
        }

        return defaultProtocolHandle;
    }
}
