package com.iteaj.network.device.client.gateway.jlzn;

import com.iteaj.network.device.consts.SwitchStatus;

public class JlznEntity {

    /**
     * 设备的唯一编号, 在其他接口里面需要用到
     * @see JlznDeviceRename#getId()
     * @see JlznDeviceDelete#getId()
     * ...
     */
    private int id;
    /**
     * 设备类型
     * 2. 窗帘开关
     * 7. 网关
     */
    private int type;
    private String pic;
    private int order;
    /**
     * 设备名称
     */
    private String name;
    private int room_id;
    /**
     * 是否在线
     */
    private boolean online;
    private Info info;

    public static class Info {
        /**
         * 设备的设备编号
         */
        private String ieee;
        private String ep;
        private String nwk_addr;
        private String model_id;
        private String rssi;
        private boolean arm;
        /**
         * 当前设备的开关状态
         */
        private SwitchStatus on_off;

        public String getIeee() {
            return ieee;
        }

        public void setIeee(String ieee) {
            this.ieee = ieee;
        }

        public String getEp() {
            return ep;
        }

        public void setEp(String ep) {
            this.ep = ep;
        }

        public String getNwk_addr() {
            return nwk_addr;
        }

        public void setNwk_addr(String nwk_addr) {
            this.nwk_addr = nwk_addr;
        }

        public String getModel_id() {
            return model_id;
        }

        public void setModel_id(String model_id) {
            this.model_id = model_id;
        }

        public String getRssi() {
            return rssi;
        }

        public void setRssi(String rssi) {
            this.rssi = rssi;
        }

        public boolean isArm() {
            return arm;
        }

        public void setArm(boolean arm) {
            this.arm = arm;
        }

        public SwitchStatus getOn_off() {
            return on_off;
        }

        public void setOn_off(SwitchStatus on_off) {
            this.on_off = on_off;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
