package com.iteaj.network.event;

import org.springframework.context.ApplicationEvent;

public class DeviceEvent extends ApplicationEvent {

    private DeviceEventType eventType;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param deviceSn the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DeviceEvent(String deviceSn, DeviceEventType eventType) {
        super(deviceSn);
        this.eventType = eventType;
    }

    @Override
    public String getSource() {
        return (String) super.getSource();
    }

    public DeviceEventType getEventType() {
        return eventType;
    }

    public void setEventType(DeviceEventType eventType) {
        this.eventType = eventType;
    }
}
