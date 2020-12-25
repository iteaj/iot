package com.iteaj.network.device.server.ths;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.IotServeBootstrap;
import com.iteaj.network.Message;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

public abstract class ThsAirProtocolAbstract extends ThsPlatformProtocol{

    public ThsAirProtocolAbstract(String equipCode) {
        super(equipCode);
    }


    @Override
    public String relationKey() {
        return getEquipCode() + "_Air";
    }

    @Override
    public PlatformRequestProtocol buildRequestMessage() {
        super.buildRequestMessage();
        AbstractMessage message = (AbstractMessage) requestMessage();

        // 同一设备不能在同一时间有多个操作, 否则区分不出是那个操作
        DeviceServerComponent serverComponent = IotServeBootstrap.getServerComponent(message.getClass());
        if(serverComponent.protocolFactory().isExists(relationKey())) {
            throw new ProtocolException("设备不支持同一时间下发多条指令, 请使用同步方式(sync), 依次下发指令");
        }

        return this;
    }

    @Override
    protected ThsElfinMessage resolverResponseMessage(ThsElfinMessage message) {
        byte[] messageByte = message.getMessage();
        int respCode = messageByte[0] & 0xff;
        if(respCode == 0x89) { // 空调协议返回成功
            setExecStatus(ExecStatus.成功);
        } else if(respCode == 0xe0) { // 空调协议返回失败, 或者校验失败
            setExecStatus(ExecStatus.失败);
        } else {
            throw new ProtocolException("错误的响应码: " + respCode);
        }

        return message;
    }
}
