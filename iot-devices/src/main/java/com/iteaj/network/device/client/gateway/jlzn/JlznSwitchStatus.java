package com.iteaj.network.device.client.gateway.jlzn;

import cn.hutool.http.Method;
import com.iteaj.iot.client.http.HttpMethod;
import com.iteaj.iot.client.http.HttpRequestMessage;
import com.iteaj.network.device.consts.SwitchStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 开关切换
 */
public class JlznSwitchStatus extends JlznHttpProtocolAdapter {

    /**
     * 设备id
     * @see JlznEntity#getId()
     */
    private int id;

    /**
     * 要下发的操作的命令
     * @see SwitchStatus#on 开
     * @see SwitchStatus#off 关
     */
    private SwitchStatus status;

    public JlznSwitchStatus(String ip, int id, SwitchStatus status) {
        this(ip, null, id, status);
    }

    public JlznSwitchStatus(String ip, String deviceSn, int id, SwitchStatus status) {
        super(ip, deviceSn);
        this.id = id;
        this.status = status;
    }

    @Override
    public JlznProtocolType protocolType() {
        return JlznProtocolType.switch_status;
    }

    @Override
    protected HttpRequestMessage doBuildRequestMessage() {
        Map<String, Object> param = new HashMap<>();
        param.put("token", this.getToken());
        param.put("request_id", this.getRequestId());
        param.put("data", "{\"op_name\":\""+getStatus()+"\",\"id\":"+getId()+"}");
        return new HttpRequestMessage(getUrl(), HttpMethod.Get, param);
    }

    public int getId() {
        return id;
    }

    public SwitchStatus getStatus() {
        return status;
    }
}
