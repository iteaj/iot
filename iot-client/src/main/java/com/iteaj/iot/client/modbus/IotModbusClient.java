package com.iteaj.iot.client.modbus;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.modbus.codec.ModbusClientDecoder;
import io.netty.channel.ChannelInboundHandler;

import java.nio.ByteOrder;

public class IotModbusClient extends IotNettyClient {

    private int port;
    private String host;

    protected IotModbusClient(ClientComponent clientComponent, String host, int port) {
        super(clientComponent);
        this.port = port;
        this.host = host;
    }

    @Override
    protected ChannelInboundHandler initClientRequestDecoder() {
        return new ModbusClientDecoder(ByteOrder.BIG_ENDIAN);
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
