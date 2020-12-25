package com.iteaj.network.device.client.ksd.lrmo;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.client.ClientMessage;

/**
 * 空调面板设备, 使用Lora网关透传, 使用标准Modbus协议
 * lrmo = Lora Modbus Air
 */
public class KLrmClientComponent extends ClientComponent {

    @Override
    protected IotNettyClient createNettyClient() {
        return new KLrmModbusClient(this);
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new KLrmProtocolFactory();
    }

    @Override
    public String name() {
        return "空调温控面板(Lora网关, Modbus协议)";
    }

    @Override
    public Class<? extends ClientMessage> messageClass() {
        return ModbusStandardMessage.class;
    }
}
