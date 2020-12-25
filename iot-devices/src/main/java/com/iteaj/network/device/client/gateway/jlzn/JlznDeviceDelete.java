package com.iteaj.network.device.client.gateway.jlzn;

import cn.hutool.http.Method;
import com.iteaj.iot.client.http.HttpMethod;
import com.iteaj.iot.client.http.HttpRequestMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 删除设备
 */
public class JlznDeviceDelete extends JlznHttpProtocolAdapter {

    /**
     * 设备id
     * @see JlznEntity#getId()
     */
    private int id;


    public JlznDeviceDelete(String ip, int id) {
        super(ip);
        this.id = id;
    }

    @Override
    public JlznProtocolType protocolType() {
        return JlznProtocolType.del_dev;
    }

    @Override
    protected HttpRequestMessage doBuildRequestMessage() {
        Map<String, Object> param = new HashMap<>();
        param.put("token", this.getToken());
        param.put("request_id", this.getRequestId());
        param.put("data", "{\"op_name\":\"del_dev\",\"id\": "+getId()+"}");
        return new HttpRequestMessage(getUrl(), HttpMethod.Get, param);
    }

    public int getId() {
        return id;
    }

}
