package com.iteaj.network.device.client.gateway.jlzn;

import cn.hutool.http.Method;
import com.iteaj.iot.client.http.HttpMethod;
import com.iteaj.iot.client.http.HttpRequestMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改设备名称
 */
public class JlznDeviceRename extends JlznHttpProtocolAdapter {

    /**
     * 设备id
     * @see JlznEntity#getId()
     */
    private int id;
    /**
     * 要修改的设备名称
     * @see JlznEntity#getName()
     */
    private String name;

    public JlznDeviceRename(String ip, int id, String name) {
        super(ip);
        this.id = id;
        this.name = name;
    }

    @Override
    public JlznProtocolType protocolType() {
        return JlznProtocolType.dev_rename;
    }

    @Override
    protected HttpRequestMessage doBuildRequestMessage() {
        Map<String, Object> param = new HashMap<>();
        param.put("token", this.getToken());
        param.put("request_id", this.getRequestId());
        param.put("data", "{\"op_name\":\"dev_rename\",\"id\": "+getId()+",\"name\":\""+getName()+"\"}");
        return new HttpRequestMessage(getUrl(), HttpMethod.Get, param);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
