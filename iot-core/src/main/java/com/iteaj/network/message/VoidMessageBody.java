package com.iteaj.network.message;

/**
 * <p>空的报文体</p>
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class VoidMessageBody extends DeviceMessageBody {

    private static VoidMessageBody instance = new VoidMessageBody();

    public VoidMessageBody() {
        this(new byte[0]);
    }

    public VoidMessageBody(byte[] message) {
        super(message);
    }

    public static VoidMessageBody getInstance() {
        return instance;
    }
}
