package com.iteaj.iot.client.handle;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.mqtt.protocol.ClientProtocolProcess;
import com.iteaj.iot.client.mqtt.common.NettyLog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttClientHandler extends SimpleChannelInboundHandler<Object> {
	private ClientComponent clientComponent;
	private ClientProtocolProcess clientProtocolProcess;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public MqttClientHandler(ClientProtocolProcess clientProtocolProcess, ClientComponent clientComponent) {
		this.clientComponent = clientComponent;
		this.clientProtocolProcess = clientProtocolProcess;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msgx) throws Exception {
		if (msgx == null) {return ;}

		MqttMessage msg = (MqttMessage) msgx;
		if(logger.isDebugEnabled()) {
			logger.debug("Mqtt客户端({})收到报文 - Mqtt类型: {} - 报文: {}", this.clientComponent.name(), msg.fixedHeader().messageType(), msg);
		}

		MqttFixedHeader mqttFixedHeader = msg.fixedHeader();
		switch (mqttFixedHeader.messageType()) {
			case CONNACK:
				clientProtocolProcess.processConnectBack(ctx.channel(), (MqttConnAckMessage) msg);
				break;
			case UNSUBACK:
				clientProtocolProcess.processUnSubBack(ctx.channel(), msg);
				break;
			case PUBLISH:
				clientProtocolProcess.processPublish(ctx.channel(), (MqttPublishMessage) msg);
				break;
			case PUBACK:
				clientProtocolProcess.processPubAck(ctx.channel(), msg);
				break;
			case PUBREC:
				clientProtocolProcess.processPubRec(ctx.channel(), msg);
				break;
			case PUBREL:
				clientProtocolProcess.processPubRel(ctx.channel(), msg);
				break;
			case PUBCOMP:
				clientProtocolProcess.processPubComp(ctx.channel(), msg);
				break;
			case SUBACK:
				clientProtocolProcess.processSubAck(ctx.channel(), (MqttSubAckMessage) msg);
				break;
			case PINGRESP:

				default:
				break;
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.WRITER_IDLE) {
				MqttFixedHeader pingreqFixedHeader =
						new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
				MqttMessage pingreqMessage = new MqttMessage(pingreqFixedHeader);

				if(logger.isDebugEnabled()) {
					logger.debug("Mqtt客户端({}) 发送心跳请求", this.clientComponent.name());
				}

				ctx.writeAndFlush(pingreqMessage);
			} else if (e.state() == IdleState.READER_IDLE) {

				return;
			} else if (e.state() == IdleState.ALL_IDLE) {

				ctx.close();
			}
		}
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().eventLoop().schedule(() -> {
			ctx.channel().close();
			clientComponent.nettyClient().doConnect();
		}, 10, TimeUnit.SECONDS);
	}
}
