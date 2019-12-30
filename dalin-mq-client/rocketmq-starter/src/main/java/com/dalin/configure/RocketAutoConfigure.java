package com.dalin.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dalin.config.DalinBroadConsumer;
import com.dalin.config.DalinConsumer;
import com.dalin.config.DalinProductor;
import com.dalin.config.DalinTransactionProductor;
import com.dalin.config.impl.DefaultDalinBroadConsumerImpl;
import com.dalin.config.impl.DefaultDalinClusterConsumerImpl;
import com.dalin.config.impl.DefaultDalinProductorImpl;
import com.dalin.config.impl.DefaultDalinTransactionProductor;
import com.dalin.service.RocketmqService;
import com.dalin.service.impl.DalinRocketMqServiceImpl;

@Configuration
@ConditionalOnProperty(value = "rocket.instance.enable",havingValue = "true")
public class RocketAutoConfigure {
	
	@Bean
	public RocketmqConfig rocketmqConfig() {
		RocketmqConfig rocketmqConfig = new RocketmqConfig();
		return rocketmqConfig;
	}
	
	@Bean
	@ConditionalOnMissingBean(value = DalinConsumer.class)
	@ConditionalOnBean(value = RocketmqConfig.class)
	public DalinConsumer consumer() {
		
		return new DefaultDalinClusterConsumerImpl();
	}
	
	@Bean
	@ConditionalOnBean(value = RocketmqConfig.class)
	@ConditionalOnMissingBean(value = DalinBroadConsumer.class)
	public DalinBroadConsumer broadConsumer() {
		return new DefaultDalinBroadConsumerImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = DalinProductor.class)
	@ConditionalOnBean(value = RocketmqConfig.class)
	public DalinProductor productor() {
		return new DefaultDalinProductorImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = DalinTransactionProductor.class)
	@ConditionalOnBean(value = RocketmqConfig.class)
	public DalinTransactionProductor transactionProductor() {
		return new DefaultDalinTransactionProductor();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = RocketmqService.class)
	@ConditionalOnBean(value = RocketmqConfig.class)
	public RocketmqService rocketmqService() {
		return new DalinRocketMqServiceImpl();
	}
	
}
