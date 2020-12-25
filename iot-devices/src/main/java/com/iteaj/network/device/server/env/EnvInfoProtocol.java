package com.iteaj.network.device.server.env;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.elfin.ElfinMessageBody;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import com.iteaj.network.utils.ByteUtil;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 获取环境监测设备信息的协议
 *  用来获取所有的环境监测设备可以监测的信息, 读取所有的寄存器
 */
public class EnvInfoProtocol extends PlatformRequestProtocol {

    private Double noise; // 噪音
    private Integer so2;
    private Integer no2;
    private Integer co;
    private Integer o3;
    private Integer pm25;
    private Integer pm10;
    private Double wenDu; // 空气温度
    private Double shiDu; // 空气湿度
    private Double atmos; // 大气压强
    private Double windSpeed; // 风速
    private String windDire; // 风向
    private Double rainNum; // 雨量
    private Integer radiation; // 辐射
    private Double guangZhaoDu; // 光照
    private Integer vindex; // 紫外指数
    private Integer co2;
    public EnvInfoProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    protected String doGetMessageId() {
        return getEquipCode();
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        ElfinMessageHeader envElfinHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        byte[] messageBody = ByteUtil.hexToByte("FF03000100134019");
        ElfinMessageBody envModbusPdu = new ElfinMessageBody(messageBody);

        return new EnvElfinMessage(envElfinHeader, envModbusPdu);
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        int iLen = 6;
        String hexMsg = ByteUtil.bytesToHex(message.getMessage());
        try {
            String hexnoise = hexMsg.substring(iLen, iLen + 4);
            BigInteger intnoise = new BigInteger(hexnoise, 16);
            this.noise = Double.parseDouble(intnoise.toString()) / 10;
            iLen += 4;
            //4位保留
            iLen += 4;
            String hexSO2 = hexMsg.substring(iLen, iLen + 4);
            this.so2 = new BigInteger(hexSO2, 16).intValue();
            iLen += 4;
            String hexNO2 = hexMsg.substring(iLen, iLen + 4);
            this.no2 = new BigInteger(hexNO2, 16).intValue();
            iLen += 4;
            String hexCO = hexMsg.substring(iLen, iLen + 4);
            this.co = new BigInteger(hexCO, 16).intValue();
            iLen += 4;
            String hexO3 = hexMsg.substring(iLen, iLen + 4);
            this.o3 = new BigInteger(hexO3, 16).intValue();
            iLen += 4;
            String hexPM25 = hexMsg.substring(iLen, iLen + 4);
            this.pm25 = new BigInteger(hexPM25, 16).intValue();
            iLen += 4;
            String hexPM10 = hexMsg.substring(iLen, iLen + 4);
            this.pm10 = new BigInteger(hexPM10, 16).intValue();
            iLen += 4;
            String hextemperature = hexMsg.substring(iLen, iLen + 4);
            BigInteger inttemperature = new BigInteger(hextemperature, 16);
            this.wenDu = Double.parseDouble(inttemperature.toString()) / 100 - 40;
            iLen += 4;
            String hexhumidity = hexMsg.substring(iLen, iLen + 4);
            BigInteger inthumidity = new BigInteger(hexhumidity, 16);
            this.shiDu = Double.parseDouble(inthumidity.toString()) / 100;
            iLen += 4;
            String hexpressure = hexMsg.substring(iLen, iLen + 4);
            BigInteger intpressure = new BigInteger(hexpressure, 16);
            this.atmos = Double.parseDouble(intpressure.toString()) / 10;
            iLen += 4;
            String hexwindSpeed = hexMsg.substring(iLen, iLen + 4);
            BigInteger intwindSpeed = new BigInteger(hexwindSpeed, 16);
            this.windSpeed = Double.parseDouble(intwindSpeed.toString()) / 100;
            iLen += 4;
            String hexwindDirection = hexMsg.substring(iLen, iLen + 4);
            BigInteger intwindDirection = new BigInteger(hexwindDirection, 16);
            Double windDirection = Double.parseDouble(intwindDirection.toString()) / 10;
            this.windDire = changeWindDirection(windDirection);
            iLen += 4;
            String hexrainfall = hexMsg.substring(iLen, iLen + 4);
            BigInteger intrainfall = new BigInteger(hexrainfall, 16);
            this.rainNum = Double.parseDouble(intrainfall.toString()) / 10;
            iLen += 4;
            String hexradiation = hexMsg.substring(iLen, iLen + 4);
            this.radiation = new BigInteger(hexradiation, 16).intValue();
            iLen += 4;
            String hexilluminance = hexMsg.substring(iLen, iLen + 4);
            BigInteger intilluminance = new BigInteger(hexilluminance, 16);
            this.guangZhaoDu = Double.parseDouble(intilluminance.toString()) / 100;
            iLen += 4;
            String hexUVindex = hexMsg.substring(iLen, iLen + 4);
            this.vindex = new BigInteger(hexUVindex, 16).intValue();
            iLen += 4;
            String hexCO2 = hexMsg.substring(iLen, iLen + 4);
            this.co2 = new BigInteger(hexCO2, 16).intValue();

        } catch (Exception e) {

        }
        return message;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Env_Elfin_Info;
    }

    private String changeWindDirection(Double windDirection){
        String windDir = "";
        if(windDirection >= 348.76 || windDirection < 11.26){
            windDir = "北";
        }
        else if(windDirection >= 11.26 && windDirection < 33.76){
            windDir = "北东北";
        }
        else if(windDirection >= 33.76 && windDirection < 56.26){
            windDir = "东北";
        }
        else if(windDirection >= 56.26 && windDirection < 78.76){
            windDir = "东东北";
        }
        else if(windDirection >= 78.76 && windDirection < 101.26){
            windDir = "东";
        }
        else if(windDirection >= 101.26 && windDirection < 123.76){
            windDir = "东东南";
        }
        else if(windDirection >= 123.76 && windDirection < 146.26){
            windDir = "东南";
        }
        else if(windDirection >= 146.26 && windDirection < 168.76){
            windDir = "南东南";
        }
        else if(windDirection >= 168.76 && windDirection < 191.26){
            windDir = "南";
        }
        else if(windDirection >= 191.26 && windDirection < 213.76){
            windDir = "南西南";
        }
        else if(windDirection >= 213.76 && windDirection < 236.26){
            windDir = "西南";
        }
        else if(windDirection >= 236.26 && windDirection < 258.76){
            windDir = "西西南";
        }
        else if(windDirection >= 258.76 && windDirection < 281.26){
            windDir = "西";
        }
        else if(windDirection >= 281.26 && windDirection < 303.76){
            windDir = "西西北";
        }
        else if(windDirection >= 303.76 && windDirection < 326.26){
            windDir = "西北";
        }
        else if(windDirection >= 326.26 && windDirection < 348.76){
            windDir = "北西北";
        }
        return windDir;
    }

    public Double getNoise() {
        return noise;
    }

    public Integer getSo2() {
        return so2;
    }

    public Integer getNo2() {
        return no2;
    }

    public Integer getCo() {
        return co;
    }

    public Integer getO3() {
        return o3;
    }

    public Integer getPm25() {
        return pm25;
    }

    public Integer getPm10() {
        return pm10;
    }

    public Integer getRadiation() {
        return radiation;
    }

    public Double getGuangZhaoDu() {
        return guangZhaoDu;
    }

    public Integer getVindex() {
        return vindex;
    }

    public Integer getCo2() {
        return co2;
    }

    public Double getWenDu() {
        return wenDu;
    }

    public Double getShiDu() {
        return shiDu;
    }

    public Double getAtmos() {
        return atmos;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getWindDire() {
        return windDire;
    }

    public Double getRainNum() {
        return rainNum;
    }

}
