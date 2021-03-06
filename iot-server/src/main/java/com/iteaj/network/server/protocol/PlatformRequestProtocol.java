package com.iteaj.network.server.protocol;

import com.iteaj.network.*;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.AppClientServerHandle;
import com.iteaj.network.client.AppClientServerProtocol;
import com.iteaj.network.client.ClientRelation;
import com.iteaj.network.client.ClientRelationEntity;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientResponseBody;
import com.iteaj.network.client.app.AppClientUtil;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.AbstractServerProtocol;
import com.iteaj.network.server.manager.DevicePipelineManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * <p>所有由平台主动发请求到设备的协议的基类</p>
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public abstract class PlatformRequestProtocol<M extends AbstractMessage> extends AbstractServerProtocol<M> implements ClientRelation<PlatformRequestProtocol> {

    // 默认超时时间15秒
    private long timeout = 15;
    private String messageId;
    private boolean clientStart;
    private ClientRelationEntity clientEntity;
    private FreeProtocolHandle freeProtocolHandle;
    private static final Object messageIdLocK = new Object();
    public PlatformRequestProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        // 先使用自定义处理器
        ProtocolHandle protocolService = getFreeProtocolHandle();

        // 如果没有声明自定义服务, 则使用和协议绑定的处理器
        if(protocolService == null) {
            protocolService = factory.getProtocolHandle(getClass());
        }

        return this.exec(protocolService);
    }

    /**
     * 构建请求报文
     * @return
     */
    public PlatformRequestProtocol buildRequestMessage() {
        try {
            this.requestMessage = this.doBuildRequestMessage();
        } catch (IOException e) {
            logger.error("构建请求报文异常： {}", e.getMessage(), e);
            throw new ProtocolException("构建请求报文异常", e);
        }
        return this;
    }

    protected final String getMessageId() {
        if(messageId != null) return messageId;

        synchronized (messageIdLocK) {
            this.messageId = doGetMessageId();
            return this.messageId;
        }
    }

    /**
     * 用来标识报文唯一
     * @return
     */
    protected abstract String doGetMessageId();

    /**
     * 构建请求报文
     * @return
     */
    protected abstract M doBuildRequestMessage() throws IOException;

    /**
     * 对于平台主动向外请求的协议 由于构建响应报文必须等到响应报文的到来<br>
     *     所以此方法不能使用,请使用以下方法
     * @see #buildResponseMessage(AbstractMessage) 为响应的报文
     * @return
     */
    @Override
    public PlatformRequestProtocol buildResponseMessage() {
        if(this.responseMessage == null)
            throw new ProtocolException("请设置设备端响应的报文");

        return this.buildResponseMessage(this.responseMessage);
    }

    /**
     *  通过响应的报文来构建响应协议
     * @param message
     * @return
     */
    public PlatformRequestProtocol buildResponseMessage(M message) throws ProtocolException{
        try {
            this.responseMessage = resolverResponseMessage(message);

            if(logger.isDebugEnabled() && this.responseMessage != null) {
                Message.MessageHead head = this.responseMessage.getHead();
                String messageId = head != null ? head.getMessageId() : null;
                logger.debug("客户端响应平台 HASH: {} - 设备编号: {} - 协议类型: {} - messageId: {} - 响应报文: {}"
                        , hashCode(), getEquipCode(), protocolType(), messageId, this.responseMessage());
            }
        } catch (Exception e) {
            throw new ProtocolException(e);
        }

        return this;
    }

    protected abstract M resolverResponseMessage(M message);

    @Override
    public boolean isSyncRequest() {
        return getDownLatch() != null;
    }

    @Override
    protected Executor getExecutor() {
        if(asyncExecutor != null)
            return asyncExecutor;

        if(IotServeBootstrap.BEAN_FACTORY.containsBean("applicationTaskExecutor")) {
            return asyncExecutor = IotServeBootstrap.BEAN_FACTORY.getBean(ThreadPoolTaskExecutor.class);
        } else {
            return super.getExecutor();
        }
    }

    /**
     * 用来做为将请求报文和响应报文进行关联的key
     * 默认用报文头的{@link com.iteaj.network.Message.MessageHead#getMessageId()}作为key
     * @return
     */
    public Object relationKey(){
        return requestMessage().getHead().getMessageId();
    }

    /**
     * 平台主动向外发起请求
     */
    public <P extends PlatformRequestProtocol<M>> P request(FreeProtocolHandle<P> handle) throws ProtocolException {
        this.freeProtocolHandle = handle;
        return (P) request();
    }

    /**
     * 平台主动向外发起请求
     */
    public PlatformRequestProtocol request() throws ProtocolException {
        try {

            this.buildRequestMessage();

            //构建完请求协议必须验证请求数据是否存在
            if(null == requestMessage() || ArrayUtils.isEmpty(requestMessage().getMessage())) {
                throw new IllegalStateException("不存在请求报文");
            }

            //获取设备管理器
            DeviceManager deviceManager = DevicePipelineManager.getInstance();

            if(null == deviceManager) {
                throw new IllegalStateException("找不到设备管理器: " + DeviceManager.class.getName());
            }

            //向设备写出协议信息
            ChannelFuture channelFuture = deviceManager.writeAndFlush(getEquipCode(), this);
            if(null == channelFuture){
                setExecStatus(ExecStatus.断线);
                exec(this.getFreeProtocolHandle());
            } else {
                if(logger.isDebugEnabled()){
                    channelFuture.addListener((ChannelFutureListener) future -> {
                        String msg = future.isSuccess() ? "成功" : "失败";
                        logger.debug("平台请求客户端({}) HASH: {} - 设备编号: {}- 协议类型: {} - 请求报文: {}",
                                msg, hashCode(), getEquipCode(), protocolType(), requestMessage());
                    });
                }
            }

            /**
             * 不同请求阻塞线程等待
             * @see #getTimeout() 等待超时
             */
            if(isSyncRequest()) {
                boolean await = getDownLatch().await(getTimeout(), TimeUnit.SECONDS);
                if(!await) {
                    setExecStatus(ExecStatus.超时);
                    exec(IotServeBootstrap.getBusinessFactory());
                }
            }

            // 同步请求返回当前对象
            if(isSyncRequest()) {
                return this;
            } else { // 异步请求直接返回 null
                return null;
            }
        } catch (InterruptedException e) {
            throw new ProtocolException("执行中断", e);
        } catch (Exception e) {
            logger.error("平台请求客户端异常 HASH: {} - 设备编号: {} - 协议类型: {}", hashCode(), getEquipCode(), protocolType(), e);
            if(e instanceof ProtocolException) {
                throw e;
            } else {
                throw new ProtocolException("请求异常", e);
            }
        }
    }

    /**
     * 同步调用, 调用的线程将阻塞, 直到超时或者设备响应
     * @see #releaseLock()
     * @param timeout 阻塞超时时间
     * @return
     */
    public PlatformRequestProtocol sync(long timeout) {
        setDownLatch(new CountDownLatch(1));
        return timeout(timeout);
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

    @Override
    public boolean isClientStart() {
        return clientStart;
    }

    public PlatformRequestProtocol setClientStart(boolean clientStart) {
        this.clientStart = clientStart;
        return this;
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
                if(responseBody.getStatus() == null) {
                    responseBody.setStatus(getExecStatus());
                }

                if(responseBody.getReason() == null) {
                    responseBody.setReason("success");
                }
            } else {
                ExecStatus execStatus = getExecStatus();
                String reason = execStatus.desc;
                if(execStatus != ExecStatus.成功) {
                    String equipCode = getEquipCode();
                    if(execStatus == ExecStatus.断线) {
                        reason = "设备["+ equipCode +"]不在线";
                    } else if(execStatus == ExecStatus.超时) {
                        reason = "请求设备超时["+equipCode+"]";
                    }
                }

                responseBody = new AppClientResponseBody(reason, execStatus);
            }

            AppClientMessage<AppClientResponseBody> serverResponseMessage = AppClientUtil.buildServerResponseMessage(clientEntity, responseBody);
            AbstractProtocol abstractProtocol = new CommonWriteProtocol(serverResponseMessage);
            ChannelFuture channelFuture = DevicePipelineManager.getInstance().writeAndFlush(clientEntity.getEquipCode(), abstractProtocol);
            channelFuture.addListener(future -> {
                String msg = future.isSuccess() ? "成功" : "失败";
                if(logger.isDebugEnabled()) {
                    logger.debug("平台响应应用客户端({}) HASH: {} - 客户设备: {} - 操作设备: {} - 操作协议: {}", msg
                            , hashCode(), clientEntity.getEquipCode(), null, clientEntity.getTradeType());
                }
            });
        }
    }

    @Override
    public boolean isRelation() {
        return getTimeout()>0 && relationKey() != null;
    }

    @Override
    public ClientRelationEntity getClientEntity() {
        return this.clientEntity;
    }

    public void setClientEntity(ClientRelationEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public long getTimeout() {
        return timeout;
    }

    public FreeProtocolHandle getFreeProtocolHandle() {
        return freeProtocolHandle;
    }

    public ClientRelation setFreeProtocolHandle(FreeProtocolHandle requestService) {
        this.freeProtocolHandle = requestService;
        return this;
    }

    /**
     *  设置超时时间
     * @param timeout
     */
    public PlatformRequestProtocol timeout(long timeout) {
        if(timeout < 0) {
            throw new ProtocolException("超时时间必须大于 0(s), 如果等于0将不会做同步和异步的等待处理");
        }

        this.timeout = timeout;
        return this;
    }

    @Override
    public String toString() {
        return "PlatformRequestProtocol{" +
                "messageId='" + messageId + '\'' +
                ", equipCode='" + getEquipCode() + '\'' +
                ", protocolType='" + protocolType() + '\'' +
                ", message='" + requestMessage() + '\'' +
                '}';
    }
}
