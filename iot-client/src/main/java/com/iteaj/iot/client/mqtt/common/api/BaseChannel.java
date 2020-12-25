package com.iteaj.iot.client.mqtt.common.api;

import com.iteaj.iot.client.mqtt.common.SocketModel;
import io.netty.channel.Channel;
import io.netty.handler.ssl.SslContext;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public abstract class BaseChannel {
	private Semaphore blockExec = new Semaphore(0);
	
	protected Channel channel;
	
	/**
	 * 主机名
	 */
	private String host;
	
	/**
	 * 端口号
	 */
	private int port=8181;
	
    /**
     * 工作线程池大小
     */
    protected int workerCount;
	
    /**
     * 是否启用keepAlive
     */
    protected boolean keepAlive = false;
	
    /**
     * 是否启用tcpNoDelay
     */
    protected boolean tcpNoDelay = false;
	
	/**
	 * Sokcet参数, 存放已完成三次握手请求的队列最大长度, 默认511长度
	 */
	protected int soBacklog = 1024;
    
    
    /**
     * 设置是否心跳检查
     */
    protected boolean checkHeartbeat = false;
	
    /**
     * 心跳检查时的读空闲时间
     */
    protected int readerIdleTimeSeconds = 0;
	
    /**
     * 心跳检查时的写空闲时间
     */
    protected int writerIdleTimeSeconds = 0;
	
    /**
     * 心跳检查时的读写空闲时间
     */
    protected int allIdleTimeSeconds = 90;

    /**
     *编码格式
     */
	protected String charsetName = "utf-8";
	
	
	/**
	 * ssl
	 */
	protected SslContext sslCtx = null;

	
	/**
	 * socketModel
	 */
	protected SocketModel socketModel = SocketModel.UNBOLOCK;
	
    
    protected List<EventListener> eventListeners = new ArrayList<>();
    
	public BaseChannel() {
	   // 默认工作线程数
       this.workerCount = Runtime.getRuntime().availableProcessors() * 2;
       //添加JVM关闭时的勾子
       Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
       
       this.init();
	}
	
	public boolean isActive() {
		return (channel == null)? false : channel.isActive();
	}
	
	public boolean isOpen() {
		return (channel == null)? false : channel.isOpen();
	}
	
	protected void init() {
		
	}
	
	/**
	 * close socket
	 */
	public abstract void shutdown();
	
    public void addEventListener(EventListener listener) {
        this.eventListeners.add(listener);
    }

	public Semaphore getBlockExec() {
		return blockExec;
	}

	public void setBlockExec(Semaphore blockExec) {
		this.blockExec = blockExec;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getSoBacklog() {
		return soBacklog;
	}

	public void setSoBacklog(int soBacklog) {
		this.soBacklog = soBacklog;
	}

	public boolean isCheckHeartbeat() {
		return checkHeartbeat;
	}

	public void setCheckHeartbeat(boolean checkHeartbeat) {
		this.checkHeartbeat = checkHeartbeat;
	}

	public int getReaderIdleTimeSeconds() {
		return readerIdleTimeSeconds;
	}

	public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
		this.readerIdleTimeSeconds = readerIdleTimeSeconds;
	}

	public int getWriterIdleTimeSeconds() {
		return writerIdleTimeSeconds;
	}

	public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
		this.writerIdleTimeSeconds = writerIdleTimeSeconds;
	}

	public int getAllIdleTimeSeconds() {
		return allIdleTimeSeconds;
	}

	public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
		this.allIdleTimeSeconds = allIdleTimeSeconds;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public SslContext getSslCtx() {
		return sslCtx;
	}

	public void setSslCtx(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	public SocketModel getSocketModel() {
		return socketModel;
	}

	public void setSocketModel(SocketModel socketModel) {
		this.socketModel = socketModel;
	}

	public List<EventListener> getEventListeners() {
		return eventListeners;
	}

	public void setEventListeners(List<EventListener> eventListeners) {
		this.eventListeners = eventListeners;
	}

	public void requireSync() {
    	try {
			blockExec.acquire();
		} catch (InterruptedException e) {
			blockExec.release();
			e.printStackTrace();
		}
    }
    
    public void releaseSync() {
    	blockExec.release(0);
    }

	class ShutdownHook extends Thread {
        private BaseChannel baseChannel;

        public ShutdownHook(BaseChannel baseChannel) {
            this.baseChannel = baseChannel;
        }

        @Override
        public void run() {
        	releaseSync();
        	baseChannel.shutdown();
        }
    }
}
