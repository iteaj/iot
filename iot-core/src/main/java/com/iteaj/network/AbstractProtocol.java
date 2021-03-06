package com.iteaj.network;

import com.iteaj.network.consts.ExecStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public abstract class AbstractProtocol<M extends AbstractMessage> implements Protocol, BusinessAction {

    /** 设备编号 */
    private String equipCode;

    /** 请求设备的报文 */
    protected M requestMessage;

    /** 设备响应平台的报文 */
    protected M responseMessage;

    /** 自定义参数 */
    private Map<String, Object> param;

    private CountDownLatch downLatch; //同步锁字段

    /*异步固定线程数量*/
    private static int ASYNC_THREAD_NUM = 2;

    /*业务异步执行器*/
    protected static Executor asyncExecutor;

    /** 协议回调状态 */
    private ExecStatus execStatus = ExecStatus.成功;
    public static Logger logger = LoggerFactory.getLogger(AbstractProtocol.class);

    public AbstractProtocol() {

    }

    public AbstractProtocol(String equipCode) {
        this.equipCode = equipCode;
    }

    public AbstractProtocol(M requestMessage, M responseMessage) {
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
    }

    @Override
    public Message requestMessage() {
        return requestMessage;
    }

    @Override
    public Message responseMessage() {
        return responseMessage;
    }

    /**
     * 构建请求报文
     * @return
     */
    public abstract AbstractProtocol buildRequestMessage();

    /**
     * 构建响应报文
     * @return
     */
    public abstract AbstractProtocol buildResponseMessage();

    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        if(business == null) return this;

        business.business(this);

        return this;
    }

    /**
     * 是否需要将请求的协议报文和响应的协议报文进行关联
     * 由于平台主动发起请求的报文,有时需要得到对方的响应,在做出判断
     * ,所以此协议的报文需要保存起来并且与响应的报文进行匹配(通过消息id)
     * @see #relationKey() 作为两者进行关联的key
     * @return
     */
    public abstract boolean isRelation();

    /**
     * 用来做为将请求报文和响应报文进行关联的key
     * @return
     */
    public abstract Object relationKey();

    @Override
    public String desc() {
        return "";
    }

    public AbstractProtocol addParam(String key, Object value){
        if(CollectionUtils.isEmpty(param))
            param = new HashMap<>();

        param.put(key, value);

        return this;
    }

    public AbstractProtocol removeParam(String key){
        if(CollectionUtils.isEmpty(param))
            return this;

        param.remove(key);
        return this;
    }

    public Object getParam(String key){
        if(CollectionUtils.isEmpty(param))
            return null;

        return param.get(key);
    }

    /**
     * 是否进行同步请求处理,默认否
     * @return
     */
    public boolean isSyncRequest(){
        return getDownLatch() != null;
    }

    /**
     * 是否异步执行业务
     * @return false
     */
    protected boolean isAsyncExec() {
        return false;
    }

    /**
     *
     * @return
     */
    protected Executor getExecutor() {
        if(asyncExecutor != null) return asyncExecutor;

        synchronized (AbstractProtocol.class) {
            if(asyncExecutor != null) return asyncExecutor;

            return asyncExecutor = Executors.newFixedThreadPool(ASYNC_THREAD_NUM);
        }
    }

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public ExecStatus getExecStatus() {
        return execStatus;
    }

    public void setExecStatus(ExecStatus execStatus) {
        this.execStatus = execStatus;
    }

    public void releaseLock() {
        if(isSyncRequest()) {
            getDownLatch().countDown();
        }
    }

    protected CountDownLatch getDownLatch() {
        return downLatch;
    }

    protected void setDownLatch(CountDownLatch downLatch) {
        this.downLatch = downLatch;
    }

    public void setRequestMessage(M requestMessage) {
        this.requestMessage = requestMessage;
    }

    public <T extends AbstractProtocol> T setResponseMessage(M responseMessage) {
        this.responseMessage = responseMessage;
        return (T) this;
    }

    public static int getAsyncThreadNum() {
        return ASYNC_THREAD_NUM;
    }
}
