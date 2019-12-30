package com.dalin.config.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.dalin.config.DalinProductor;
import com.dalin.configure.RocketmqConfig;
import com.dalin.dto.DalinBatchMessage;
import com.dalin.dto.DalinMqMessage;
import com.dalin.enums.DalinRocketmqEnums;
import com.dalin.service.IProductCallback;
import com.dalin.util.MqUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: DefaultDalinProductorImpl
 * @Description: 默认生产者实例
 * @author 18801
 * @date 2019年12月21日
 */
@Slf4j
public class DefaultDalinProductorImpl implements DalinProductor{

	private DefaultMQProducer productor;
	
	@Autowired
	private RocketmqConfig config;
	
	/**
	 * 
	 * @Title: init
	 * @Description: 启动productor
	 * @param  参数
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		LocalDateTime start = LocalDateTime.now();
		
		productor = new DefaultMQProducer(config.getProductGroupName(), true, MqUtil.DALIN_TRACE_MESSAGE_TOPIC);
		
		productor.setNamesrvAddr(config.getNamesrvAddr());
		
		productor.setInstanceName(config.getAppName());
		
		productor.setSendMsgTimeout(config.DEFAULT_SENDMESSAGE_TIMEOUT);
		
		try {
			productor.start();
			log.info("生产者启动成功,useTime={}",Duration.between(start, LocalDateTime.now()).toMillis());
		} catch (MQClientException e) {
			e.printStackTrace();
			log.info("生产者启动失败");
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
		LocalDateTime start = LocalDateTime.now();
		Message msg = MqUtil.instance().createMessage(message);	
		try {
			SendResult sendResult = productor.send(msg);
			if(SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
				productCallback.onSuccess(bizId, message);
				log.info("发送消息，useTime={}",Duration.between(start, LocalDateTime.now()).toMillis());
			}else {
				productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCTOR_SEND_FAIL.getMsg());
				log.info("bizId={}发送消息失败",bizId);
			}
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			log.info("bizId={}发送消息失败",bizId);
			e.printStackTrace();
		}
	}

	@Override
	public void sendSync(String bizId, DalinMqMessage message, IProductCallback productCallback) {
		LocalDateTime start = LocalDateTime.now();
		Message msg = MqUtil.instance().createMessage(message);
		try {
			productor.send(msg, new SendCallback() {
				
				@Override
				public void onSuccess(SendResult sendResult) {
					if(SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
						productCallback.onSuccess(bizId, message);
						log.info("消息发送完成,useTime{}",Duration.between(start, LocalDateTime.now()).toMillis());
					}else {
						productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCTOR_SEND_FAIL.getMsg());
						log.info("bizId={}发送消息失败",bizId);
					}
				}
				
				@Override
				public void onException(Throwable e) {
					e.printStackTrace();
					productCallback.onError(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_ERROR.getMsg());
					log.error("消息发送异常",e);
				}
			});
		} catch (MQClientException | RemotingException | InterruptedException e) {
			productCallback.onError(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_ERROR.getMsg());
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		
	}

	@Override
	public void batchSend(String bizId, DalinBatchMessage batchMessage, IProductCallback productCallback) {
		LocalDateTime startTime = LocalDateTime.now();
		try {
			SendResult sendResult= productor.send(MqUtil.instance().createBatchMessage(batchMessage));
			if(SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
				productCallback.onSuccess(bizId, batchMessage);
				log.info("消息发送完成,useTime{}",Duration.between(startTime, LocalDateTime.now()).toMillis());
			}else {
				log.info("bizId={}发送消息失败",bizId);
				productCallback.onFail(bizId, batchMessage, DalinRocketmqEnums.PRODUCTOR_SEND_FAIL.getMsg());
			}
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			e.printStackTrace();
			productCallback.onFail(bizId, batchMessage, DalinRocketmqEnums.PRODUCT_SEND_ERROR.getMsg());
			log.error(e.getMessage(),e);
		}
		
	}

}
