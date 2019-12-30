package com.dalin.configure;


import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;

import com.dalin.util.MqUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RocketmqConfig {
	
	/**默认环境名称**/
	private static final String DEFAULT_ENV_NAME = "dev";
	
	public static final int DEFAULT_SENDMESSAGE_TIMEOUT = 3000; 
	
	@Autowired
	private WebApplicationContext wac;
	
	@Value("${rocket.instance.namesrvAddr}")
	private String namesrvAddr;
	@Value("${rocket.instance.productGroupName}")
	private String productGroupName;
	@Value("${rocket.instance.consumerGroupName}")
	private String consumerGroupName;
	@Value("${rocket.instance.sendMessageTimeout}")
	private int sendMessageTimeout;
	
	/**rocketmq实例名称**/
	private String instanceClientName;
	
	private String transactionProductorGroupName;
	/**环境名称**/
	private String envName;
	/**应用名称**/
	@Value(value = "${spring.application.name}")
	private String appName;
	
	@PostConstruct
	public void init() {
		check();
	}
	
	public void check() {
		//获取环境名称
		Optional<String[]> profilesOptional =Optional.ofNullable(wac.getEnvironment().getActiveProfiles());
	    if(!profilesOptional.isPresent()||profilesOptional.get().length==0) { 
	    	envName = DEFAULT_ENV_NAME; 
	    }else {
	    	envName = profilesOptional.get()[0]; 
	    }
		if(envName.equalsIgnoreCase("default")) { 
			envName = DEFAULT_ENV_NAME; 
		}
		
		if(StringUtils.isBlank(this.getNamesrvAddr())) {
			log.error("rockermq 服务器列表不能为空");
			throw new RuntimeException("rockermq 服务器列表不能为空");
		}
		
		if(StringUtils.isBlank(productGroupName)) {
			log.error("生产者组名不能为空");
			throw new RuntimeException("生产者组名不能为空");
		}
		setProductGroupName(MqUtil.instance().expandName(productGroupName, this));
		setTransactionProductorGroupName("transaction_"+productGroupName);
		if(StringUtils.isBlank(consumerGroupName)) {
			log.error("消费者组名不能为空");
			throw new RuntimeException("消费者组名不能为空");
		}
		setConsumerGroupName(MqUtil.instance().expandName(consumerGroupName, this));
		
		instanceClientName = appName;
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

	public String getInstanceClientName() {
		return instanceClientName;
	}

	public void setInstanceClientName(String instanceClientName) {
		this.instanceClientName = instanceClientName;
	}

	public String getTransactionProductorGroupName() {
		return transactionProductorGroupName;
	}

	public void setTransactionProductorGroupName(String transactionProductorGroupName) {
		this.transactionProductorGroupName = transactionProductorGroupName;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	
}
