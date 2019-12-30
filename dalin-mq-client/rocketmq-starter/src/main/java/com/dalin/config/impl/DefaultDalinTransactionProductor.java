package com.dalin.config.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;

import com.dalin.config.DalinTransactionProductor;
import com.dalin.configure.RocketmqConfig;
import com.dalin.dto.DalinMqMessage;
import com.dalin.enums.DalinRocketmqEnums;
import com.dalin.service.IProductCallback;
import com.dalin.util.MqUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: DefaultDalinTranscationProductor
 * @Description:  默认事务消息生产者
 * @author 18801
 * @date 2019年12月21日
 */
@Slf4j
public class DefaultDalinTransactionProductor implements DalinTransactionProductor{

	private TransactionMQProducer productor;
	
	@Autowired
	private RocketmqConfig config;
	
	/**
	 * 
	 * @Title: init
	 * @Description: 事务消息发送者初始化
	 * @param  参数
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		LocalDateTime now = LocalDateTime.now();
		
		productor = new TransactionMQProducer(config.getTransactionProductorGroupName());
		
		productor.setNamesrvAddr(config.getNamesrvAddr());
		
		productor.setInstanceName(config.getAppName());
		
		productor.setSendMsgTimeout(config.DEFAULT_SENDMESSAGE_TIMEOUT);
		
		try {
			productor.start();
			log.info("事务生产者启动成功,useTime={}",Duration.between(now, LocalDateTime.now()).toMillis());
		} catch (MQClientException e) {
			e.printStackTrace();
			log.info("事务生产者启动失败,useTime={}",Duration.between(now, LocalDateTime.now()).toMillis());
		}
	}
	
	@PreDestroy
	public void destory() {
		if(!Optional.of(productor).isPresent()) {
			productor.shutdown();
		}
	}

	@Override
	public void send(String bizId, DalinMqMessage message, IProductCallback productCallback) {
		if(!Optional.ofNullable(message).isPresent()) {
			productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_MESSAGE_NOT_NULL.getMsg());
		}
		if(StringUtils.isBlank(message.getTopicName())) {
			productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_TOPICNAME_NOT_NULL.getMsg());
		}
		if(StringUtils.isBlank(message.getMsgeBody())) {
			productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_DATA_NOT_NULL.getMsg());
		}
		
		try {
			productor.sendMessageInTransaction(MqUtil.instance().createMessage(message), null);
			productCallback.onSuccess(bizId, message);
		} catch (MQClientException e) {
			log.error(e.getMessage(),e);
			productCallback.onError(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_ERROR.getMsg());
		}
		
	}

	@Override
	public void setListener(TransactionListener transactionListener) {
		if(!Optional.of(transactionListener).isPresent()) {
			log.info("事务监听器未设置");
			return;
		}
		if(!Optional.ofNullable(productor).isPresent()) {
			log.info("消息生产者没有初始化");
			return;
		}
		productor.setTransactionListener(transactionListener);
	}
	
}
