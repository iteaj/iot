package com.iteaj.network.event;

import org.springframework.context.ApplicationListener;

public interface DeviceEventListener extends ApplicationListener<DeviceEvent> {

    @Override
    default void onApplicationEvent(DeviceEvent event) {
        if(event == null) {
            throw new IllegalArgumentException("未指定设备事件参数");
        }

        switch (event.getEventType()) {
            case 设备上线:
                deviceOnlineHandle(event.getSource());
                break;
            case 设备断线:
                deviceOfflineHandle(event.getSource());
                break;

            default: throw new IllegalStateException("错误的设备事件类型: " + event.getEventType());
        }
    }

    /**
     * 设备断线处理
     * @param deviceSn
     */
    void deviceOfflineHandle(String deviceSn);

    /**
     * 设备上线处理
     * @param deviceSn
     */
    void deviceOnlineHandle(String deviceSn);
}
