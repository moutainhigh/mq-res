package com.dalin.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rocket.instance")	
public class RocketConfigureProperties {
	/**是否开启rocketmq**/
	private boolean enable;
	/**namesrv 连接地址**/
	private String namesrvAddr;
	/**生产者分组名称**/
	private String productGroupName;
	/**消费者分组名称**/
	private String consumerGroupName;
	/**发送消息超时时间**/
	private int sendMessageTimeout;
	
	public boolean isEnable() {
		return enable;
	}

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public String getConsumerGroupName() {
		return consumerGroupName;
	}

	public void setConsumerGroupName(String consumerGroupName) {
		this.consumerGroupName = consumerGroupName;
	}

	public int getSendMessageTimeout() {
		return sendMessageTimeout;
	}

	public void setSendMessageTimeout(int sendMessageTimeout) {
		this.sendMessageTimeout = sendMessageTimeout;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	
}
