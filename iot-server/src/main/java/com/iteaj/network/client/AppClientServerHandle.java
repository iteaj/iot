package com.iteaj.network.client;

import com.alibaba.fastjson.JSONObject;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.app.*;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import com.iteaj.network.server.service.DeviceRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 对应的协议：
 * @see AppClientServerProtocol
 */
public class AppClientServerHandle implements DeviceRequestService<AppClientServerProtocol>, InitializingBean {

    private List<AppClientTypeMatcherHandle> matcherHandles;
    @Autowired
    private ObjectProvider<List<AppClientTypeMatcherHandle>> provider;
    private Logger logger = LoggerFactory.getLogger(AppClientServerHandle.class);

    @Override
    public Object doBusiness(AppClientServerProtocol protocol) {
        AppClientMessage serverMessage = protocol.requestMessage();
        JSONObject jsonMessage = serverMessage.getJsonMessage();

        try {
            JSONObject head = jsonMessage.getJSONObject("head");
            String tradeType = head.getString("tradeType");

            if(CollectionUtils.isEmpty(matcherHandles)) {
                this.matcherHandles = provider.getIfAvailable();
                if(this.matcherHandles == null)
                    throw new ProtocolException("没有找到客户端协议处理对象: " + AppClientTypeMatcherHandle.class.getSimpleName());
            }

            // 查找和tradeType匹配的处理器
            Optional<AppClientTypeMatcherHandle> typeMatcherHandle = matcherHandles
                    .stream().filter(handle -> handle.isMatcher(tradeType)).findFirst();

            if(typeMatcherHandle.isPresent()) {
                AppClientTypeMatcherHandle handle = typeMatcherHandle.get();
                // 通过处理器反序列化报文体
                AppClientMessageBody clientMessageBody = handle.deserialize(jsonMessage.getJSONObject("body"), RequestType.REQ);
                if(null == clientMessageBody) {
                    throw new ProtocolException("反序列化报文异常, 返回Null [AppClientTypeMatcherHandle.deserialize()]");
                }

                serverMessage.setBody(clientMessageBody);
                AppClientMessageHead messageHead = serverMessage.getHead();

                /**
                 * 如果处理完后:
                 * 1. 如果返回了需要调用设备的协议, 先调用协议请求设备, 等待设备响应或在响应客户端
                 * @see PlatformRequestProtocol#appClientHandle(Object) 等待设备响应后, 会主动在给客户端调用请求, 响应请求信息
                 *
                 * 2. 如果返回 null, 则直接响应客户端
                 */
                ClientRelation abstractProtocol = handle.handle(serverMessage);
                if(abstractProtocol != null) {
                    abstractProtocol.setClientStart(true);

                    long timeout = abstractProtocol.getTimeout();

                    /**
                     * 1. 如果应用客户端请求携带了超时时间和MessageId, 说明应用客户端需要等待设备的响应
                     * 2. 如果应用客户端要调用的协议[abstractProtocol]不需要同步等待, 则直接响应应用客户端成功
                     */
                    if(messageHead.getTimeout() > 0 && serverMessage.getDeviceSn()
                            != null && messageHead.getMessageId() != null) {
                        ClientRelationEntity entity = new ClientRelationEntity(messageHead.getTimeout(),
                                messageHead.getTradeType(), messageHead.getEquipCode(), messageHead.getMessageId());

                        abstractProtocol.setClientEntity(entity);

                        // 如果客户端请求的超时时间大于协议默认的超时时间, 使用客户端的时间
                        if(messageHead.getTimeout() > timeout) {
                            timeout = messageHead.getTimeout();
                        }
                    }

                    abstractProtocol.timeout(timeout).request();
                    /**
                     * 功能:　通过此标记来判断是否直接响应应用客户端
                     * 　　1. 设置{null}, 将会等待设备的响应, 然后再响应客户端, 达到同步效果
                     *    2. 如果协议不需要关联响应,设置成功{@link ExecStatus#成功}直接响应客户端
                     * @see AppClientServerProtocol#doBuildResponseMessage()
                     */
                    if(!abstractProtocol.isRelation()) {
                        protocol.setExecStatus(ExecStatus.成功); // 直接响应客户端成功
                    } else {
                        protocol.setExecStatus(null); // 等待设备响应后在响应
                    }


                } else {
                    logger.warn("应用客户端请求处理 无返回ClientRelation - 交易类型: {} - 请求设备: {} - 说明: 将直接响应客户端"
                            , messageHead.getTradeType(), messageHead.getEquipCode());

                    // 直接响应客户端成功
                    protocol.setExecStatus(ExecStatus.成功);
                }

            } else {
                // 直接响应客户端失败, 以及失败的原因
                protocol.setExecStatus(ExecStatus.失败);
                protocol.setFailEx(new ProtocolException("查找不到和tradeType: " + tradeType + "对应的handle[AppClientTypeMatcherHandle]"));
            }
        } catch (Exception e) { // 直接响应客户端失败, 以及异常信息
            protocol.setFailEx(e);
            protocol.setExecStatus(ExecStatus.失败);
            logger.error("处理应用程序客户端请求失败", e);
        }

        return null;
    }

    @Override
    public AppClientType protocolType() {
        return AppClientType.App_Client_Server;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
