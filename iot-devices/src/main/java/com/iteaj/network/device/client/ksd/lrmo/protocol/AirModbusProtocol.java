package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.iot.client.modbus.protocol.ModbusStandardProtocol;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.client.ksd.lrmo.KLrmClientComponent;
import com.iteaj.network.device.client.ksd.lrmo.KLrmModbusClient;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;
import org.springframework.util.StringUtils;

/**
 * 空调温控面板设备相关协议
 */
public abstract class AirModbusProtocol extends ModbusStandardProtocol {

    private int port;
    private String host;

    private AirCode code;
    private byte unitId;
    private String gateway;

    /**
     * @param gateway 网关编号
     * @param equipCode 设备编号 1-99
     * @param code 空调状态码
     */
    public AirModbusProtocol(String gateway, String equipCode, AirCode code) {
        this.code = code;
        this.gateway = gateway;
        if(!StringUtils.hasText(gateway))
            throw new ProtocolException("请传入网关编号");

        if(!StringUtils.hasText(equipCode))
            throw new ProtocolException("请传入设备编号");

        this.unitId = Byte.valueOf(equipCode);
        this.setEquipCode(equipCode);
    }

    @Override
    public AirModbusProtocol request() throws ProtocolException {
        if(!StringUtils.hasText(this.host))
            throw new ProtocolException("未指定要链接的主机");
        if(port<=0 || port > 65535) throw new ProtocolException("设置的端口错误");

        return (AirModbusProtocol) super.request();
    }

    @Override
    protected IotNettyClient getIotNettyClient() {
        ClientComponent clientComponent = IotClientBootstrap.clientComponentFactory.getByClass(ModbusStandardMessage.class);
        if(clientComponent != null) {
            KLrmModbusClient KLrmModbusClient = (KLrmModbusClient) clientComponent.nettyClient();
            return KLrmModbusClient.getInstance(getHost(), getPort());
        } else {
            throw new ProtocolException("不存在客户端组件对象: " + KLrmClientComponent.class.getSimpleName());
        }
    }

    @Override
    public String desc() {
        return "空调面板协议 - " + protocolType();
    }

    @Override
    protected byte getUnitId() {
        return unitId;
    }

    @Override
    public abstract AirType protocolType();

    public byte getCode() {
        return code.code;
    }

    public void setCode(AirCode code) {
        this.code = code;
    }


    public int getPort() {
        return port;
    }

    public AirModbusProtocol setPort(int port) {
        this.port = port;
        return this;
    }

    public String getHost() {
        return host;
    }

    public AirModbusProtocol setHost(String host) {
        this.host = host;
        return this;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
}
