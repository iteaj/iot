package com.iteaj.network.message;

/**
 * <p>空的报文体</p>
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class VoidMessageBody extends DeviceMessageBody {

    public VoidMessageBody() {
        this(null, 0);
    }

    public VoidMessageBody(byte[] messageBody) {
        super(messageBody);
    }

    public VoidMessageBody(byte[] messageBody, int length) {
        super(messageBody, length);
    }
}
