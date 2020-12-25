package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

import java.io.IOException;
import java.net.ProtocolException;

public class PvcSetupProtocol extends PduPlatformProtocol{

    /**
     * trd 为温度模式(0：关闭,1:升温,2:降温)
     * ,tset 为设置的温度(扩大了 10 倍),th 为回差值(扩大了 10 倍)
     * ,tsocket 为选择的插座口(如 tsocket='145' 145=10010001,选择的插座口为 1---5--8)
     */
    private static final String T = "START PVC_setup tag='%s' type='T' trd='%s' tset='%s' th='%s' tsocket='%s' check='%s' END\n";

    /**
     * type 类型(C:电流,V:电压,P:功率,T:温度)
     * ch 上限,cl 为下限,cw 为警告(注：ch,cl,cw 为电流,电压为 vh,vl,vw,功率为 ph,pl,pw,电流扩大了 100 倍,电压扩大了 10 倍,功率没变)
     * cport 超出上限时要执行的插座口(如:cport='135' 135=1000 0111,所以选择的插座口为123----8)
     * cstate 执行的动作(0:关闭,1:开启,2:重启,3:延时关闭,4:延时开启,5:延时重启,6:不动作)
     * csec 延时时间
     */
    // 设置电流
    private static final String C = "START PVC_setup tag='%s' type='C' ch='%s' cl='%s' cw='%s' cport='%s' cstate='%s' csec='%s' check='%s' END";
    // 设置电压
    private static final String V = "START PVC_setup tag='%s' type='V' vh='%s' vl='%s' vw='%s' vport='%s' vstate='%s' vsec='%s' check='%s' END";
    // 设置功率
    private static final String P = "START PVC_setup tag='%s' type='P' ph='%s' pl='%s' pw='%s' pport='%s' pstate='%s' psec='%s' check='%s' END";

    /**
     * 类型(C:电流,V:电压,P:功率,T:温度)
     */
    private String type;
    private String mh; // 上限
    private String l; // 下限
    private String w; // 警告
    private String sec;// 为延时时间;
    private String port;
    private String state;

    private String ch;
    private String cl;
    private String cw;
    private String csec;
    private String cport;
    private String cstate;

    private String vh;
    private String vl;
    private String vw;
    private String vsec;
    private String vport;
    private String vstate;

    private String ph;
    private String pl;
    private String pw;
    private String psec;
    private String pport;
    private String pstate;

    private String t; // 温度
    private String f; // 华氏
    private String h; // 湿度
    private String th; // th 为回差值(扩大了 10 倍)
    private String trd; // 温度模式(0：关闭,1:升温,2:降温)
    private String tset; // 设置的温度(扩大了 10 倍)
    private String tflag;
    private String tsocket; // 为选择的插座口(如 tsocket='145' 145=10010001,选择的插座口为 1---5--8)

    public PvcSetupProtocol(String equipCode) {
        super(equipCode);
    }

    public PvcSetupProtocol(String equipCode, String type, String mh, String l, String w, String state, String sec, String port) {
        super(equipCode);
        this.w = w;
        this.l = l;
        this.mh = mh;
        this.sec = sec;
        this.port = port;
        this.type = type;
        this.state = state;
    }

    public static PvcSetupProtocol buildTemp(String equipCode, String trd, String tset, String th, String tsocket) {
        PvcSetupProtocol pvcSetupProtocol = new PvcSetupProtocol(equipCode);
        pvcSetupProtocol.setType("T");
        pvcSetupProtocol.setTh(th);
        pvcSetupProtocol.setTrd(trd);
        pvcSetupProtocol.setTset(tset);
        pvcSetupProtocol.setTsocket(tsocket);
        return pvcSetupProtocol;
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.PVC_setup;
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        String message;
        if("T".equals(type)) {
            message = String.format(T, getMessageId(), trd, tset, th, tsocket, "%s");
        } else if("C".equals(type)) {
            int mh = Double.valueOf(Double.valueOf(this.mh) * 100).intValue();
            int w = Double.valueOf(Double.valueOf(this.w) * 100).intValue();
            int l = Double.valueOf(Double.valueOf(this.l) * 100).intValue();
            message = String.format(C, getMessageId(), mh, l, w, port, state, sec, "%s");
        } else if("V".equals(type)) {
            int mh = Double.valueOf(Double.valueOf(this.mh) * 10).intValue();
            int w = Double.valueOf(Double.valueOf(this.w) * 10).intValue();
            int l = Double.valueOf(Double.valueOf(this.l) * 10).intValue();
            message = String.format(V, getMessageId(), mh, l, w, port, state, sec, "%s");
        } else if("P".equals(type)) {
            message = String.format(P, getMessageId(), mh, l, w, port, state, sec, "%s");
        } else {
            throw new ProtocolException("未知的操作类型： " + type);
        }

        int code = PduMessageUtil.getCode(message);

        message = String.format(message, code);
        return newMessage(message.getBytes("GBK"));
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        PduMessage pduMessage = (PduMessage) message;
        String[] content = pduMessage.getContent();
        int index = 2;
        if(PduMessageUtil.startWith(content, index, "tag")) {
            ++ index;
        }
        this.ph = PduMessageUtil.getString(content, index);
        this.pl = PduMessageUtil.getString(content, ++index);
        this.pw = PduMessageUtil.getString(content, ++index);
        this.pport = PduMessageUtil.getString(content, ++index);
        this.pstate = PduMessageUtil.getString(content, ++index);
        this.psec = PduMessageUtil.getString(content, ++index);
        this.vh = PduMessageUtil.getString(content, ++index);
        this.vl = PduMessageUtil.getString(content, ++index);
        this.vw = PduMessageUtil.getString(content, ++index);
        this.vport = PduMessageUtil.getString(content, ++index);
        this.vstate = PduMessageUtil.getString(content, ++index);
        this.vsec = PduMessageUtil.getString(content, ++index);
        this.ch = PduMessageUtil.getString(content, ++index);
        this.cl = PduMessageUtil.getString(content, ++index);
        this.cw = PduMessageUtil.getString(content, ++index);
        this.cport = PduMessageUtil.getString(content, ++index);
        this.cstate = PduMessageUtil.getString(content, ++index);
        this.csec = PduMessageUtil.getString(content, ++index);
        this.tflag = PduMessageUtil.getString(content, ++index);
        this.t = PduMessageUtil.getString(content, ++index);
        this.trd = PduMessageUtil.getString(content, ++index);
        this.tset = PduMessageUtil.getString(content, ++index);
        this.th = PduMessageUtil.getString(content, ++index);
        this.tsocket = PduMessageUtil.getString(content, ++index);
        this.f = PduMessageUtil.getString(content, ++index);
        this.h = PduMessageUtil.getString(content, ++index);
        return pduMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMh() {
        return mh;
    }

    public void setMh(String mh) {
        this.mh = mh;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getCw() {
        return cw;
    }

    public void setCw(String cw) {
        this.cw = cw;
    }

    public String getCsec() {
        return csec;
    }

    public void setCsec(String csec) {
        this.csec = csec;
    }

    public String getCport() {
        return cport;
    }

    public void setCport(String cport) {
        this.cport = cport;
    }

    public String getCstate() {
        return cstate;
    }

    public void setCstate(String cstate) {
        this.cstate = cstate;
    }

    public String getVh() {
        return vh;
    }

    public void setVh(String vh) {
        this.vh = vh;
    }

    public String getVl() {
        return vl;
    }

    public void setVl(String vl) {
        this.vl = vl;
    }

    public String getVw() {
        return vw;
    }

    public void setVw(String vw) {
        this.vw = vw;
    }

    public String getVsec() {
        return vsec;
    }

    public void setVsec(String vsec) {
        this.vsec = vsec;
    }

    public String getVport() {
        return vport;
    }

    public void setVport(String vport) {
        this.vport = vport;
    }

    public String getVstate() {
        return vstate;
    }

    public void setVstate(String vstate) {
        this.vstate = vstate;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPsec() {
        return psec;
    }

    public void setPsec(String psec) {
        this.psec = psec;
    }

    public String getPport() {
        return pport;
    }

    public void setPport(String pport) {
        this.pport = pport;
    }

    public String getPstate() {
        return pstate;
    }

    public void setPstate(String pstate) {
        this.pstate = pstate;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public String getTrd() {
        return trd;
    }

    public void setTrd(String trd) {
        this.trd = trd;
    }

    public String getTset() {
        return tset;
    }

    public void setTset(String tset) {
        this.tset = tset;
    }

    public String getTflag() {
        return tflag;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public String getTsocket() {
        return tsocket;
    }

    public void setTsocket(String tsocket) {
        this.tsocket = tsocket;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PvcSetupProtocol{" +
                "ch='" + ch + '\'' +
                ", cl='" + cl + '\'' +
                ", cw='" + cw + '\'' +
                ", csec='" + csec + '\'' +
                ", cport='" + cport + '\'' +
                ", cstate='" + cstate + '\'' +
                ", vh='" + vh + '\'' +
                ", vl='" + vl + '\'' +
                ", vw='" + vw + '\'' +
                ", vsec='" + vsec + '\'' +
                ", vport='" + vport + '\'' +
                ", vstate='" + vstate + '\'' +
                ", ph='" + ph + '\'' +
                ", pl='" + pl + '\'' +
                ", pw='" + pw + '\'' +
                ", psec='" + psec + '\'' +
                ", pport='" + pport + '\'' +
                ", pstate='" + pstate + '\'' +
                ", t='" + t + '\'' +
                ", f='" + f + '\'' +
                ", h='" + h + '\'' +
                ", th='" + th + '\'' +
                ", trd='" + trd + '\'' +
                ", tset='" + tset + '\'' +
                ", tflag='" + tflag + '\'' +
                ", tsocket='" + tsocket + '\'' +
                '}';
    }
}
