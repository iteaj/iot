package com.iteaj.iot.client.modbus;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.IotClientBootstrap;
import io.netty.channel.Channel;

/**
 * 多服务器网络拓扑结构的标准Modbus协议客户端
 */
public abstract class ModbusMultiServerClient extends IotModbusClient{

    private ModbusClientManager clients = new ModbusClientManager();

    public ModbusMultiServerClient(ClientComponent clientComponent) {
        super(clientComponent, null, 0);
    }

    public ModbusMultiServerClient getInstance(String host, int port) {
        ModbusMultiServerClient iotModbusClient = clients.get(host + ":" + port);
        if(iotModbusClient != null) {
            Channel channel = iotModbusClient.getChannel();
            if(!channel.isActive()) { // 如果通道未激活, 则先激活
                iotModbusClient.doConnect(10);
            }

            return iotModbusClient;
        }

        synchronized (clients) {
            iotModbusClient = clients.get(host + ":" + port);
            if(iotModbusClient != null) return iotModbusClient;

            iotModbusClient = getInstance();
            iotModbusClient.setHost(host);
            iotModbusClient.setPort(port);

            iotModbusClient.init(IotClientBootstrap.clientGroup);
            iotModbusClient.doConnect(10);
            clients.add(host+":"+port, iotModbusClient);
            return iotModbusClient;
        }
    }

    protected abstract ModbusMultiServerClient getInstance();
}
