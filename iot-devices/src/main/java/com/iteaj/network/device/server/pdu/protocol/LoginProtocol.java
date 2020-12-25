package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

import java.io.UnsupportedEncodingException;

public class LoginProtocol extends PduDeviceProtocol {

    private String id;
    private String type;
    private String user;
    private String device;
    private String password;

    public LoginProtocol(PduMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected PduMessage doBuildResponseMessage() {
        try {
            PduMessage.PduMessageHead head = (PduMessage.PduMessageHead)requestMessage().getHead();
            return new PduMessage(head, "Login Successful\n".getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void resolverRequestMessage(PduMessage requestMessage) {
        String[] content = requestMessage.getContent();

        this.type = PduMessageUtil.getString(content, 2);
        this.user = PduMessageUtil.getString(content, 5);
        this.password = PduMessageUtil.getString(content, 6);
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.登录;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
