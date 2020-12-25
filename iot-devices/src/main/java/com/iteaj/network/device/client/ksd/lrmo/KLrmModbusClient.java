package com.iteaj.network.device.client.ksd.lrmo;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.modbus.ModbusMultiServerClient;
import com.iteaj.network.ProtocolException;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.lang3.StringUtils;

public class KLrmModbusClient extends ModbusMultiServerClient {

    public KLrmModbusClient(ClientComponent clientComponent) {
        super(clientComponent);
    }

    @Override
    protected ModbusMultiServerClient getInstance() {
        return new KLrmModbusClient(getClientComponent());
    }

    @Override
    public void init(NioEventLoopGroup clientGroup) {
        if(StringUtils.isNotBlank(getHost())) {
            super.init(clientGroup);
        }
    }

    @Override
    public void doConnect() {
        if(getBootstrap() != null) {
            super.doConnect();
        }
    }

    @Override
    public void doConnect(long timeout) throws ProtocolException {
        if(getBootstrap() != null) {
            super.doConnect(timeout);
        }
    }
}
