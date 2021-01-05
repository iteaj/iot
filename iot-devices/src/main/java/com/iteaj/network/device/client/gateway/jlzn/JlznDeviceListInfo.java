package com.iteaj.network.device.client.gateway.jlzn;

import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.iteaj.iot.client.http.HttpMethod;
import com.iteaj.iot.client.http.HttpRequestMessage;
import com.iteaj.iot.client.http.HttpResponseMessage;
import com.iteaj.network.ProtocolException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取网关设备已经绑定的设备列表
 */
public class JlznDeviceListInfo extends JlznHttpProtocolAdapter{

    private List<JlznEntity> devices;

    public JlznDeviceListInfo(String ip) {
        super(ip);
    }

    @Override
    protected void resolverResponseMessage(HttpResponseMessage content) {
        int code = content.getCode();
        if(code == 200) {
            String message = content.getMessage("UTF-8");

            JSONObject jsonObject = JSONUtil.parseObj(message);
            JSONArray jsonArray = jsonObject.getJSONArray("response_params");
            this.devices = JSONUtil.toList(jsonArray, JlznEntity.class);
        } else {
            throw new ProtocolException("请求失败, 状态码["+code+"]");
        }
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public JlznProtocolType protocolType() {
        return JlznProtocolType.get_devlist;
    }

    @Override
    protected HttpRequestMessage doBuildRequestMessage() {
        Map<String, Object> param = new HashMap<>();
        param.put("token", this.getToken());
        param.put("request_id", this.getRequestId());
        param.put("data", "{\"op_name\":\"get_devlist\"}");
        return new HttpRequestMessage(getUrl(), HttpMethod.Get, param);
    }

    public List<JlznEntity> getDevices() {
        return devices;
    }

    public void setDevices(List<JlznEntity> devices) {
        this.devices = devices;
    }

}
