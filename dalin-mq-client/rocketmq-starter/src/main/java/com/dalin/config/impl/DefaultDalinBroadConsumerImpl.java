package com.dalin.config.impl;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import com.dalin.config.DalinBroadConsumer;
import com.dalin.configure.RocketmqConfig;
import com.dalin.dto.DalinSubscribe;
import com.dalin.enums.DalinRocketmqEnums;
import com.dalin.service.IConsumerCallback;
import com.dalin.service.ISubscribeCallback;
import com.dalin.util.MqUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DefaultDalinBroadConsumerImpl implements DalinBroadConsumer {

	private DefaultMQPushConsumer consumer;
	
	private static Map<String,Set<IConsumerCallback>> factory = new ConcurrentHashMap<String,Set<IConsumerCallback>>();

	@Autowired
	private RocketmqConfig config;
	
	@PostConstruct
	public void init() {
		LocalDateTime start = LocalDateTime.now();
		consumer = new DefaultMQPushConsumer("broadcast_"+config.getConsumerGroupName(), true, MqUtil.DALIN_TRACE_MESSAGE_TOPIC);
		
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		
		consumer.setNamesrvAddr(config.getNamesrvAddr());
		
		consumer.setInstanceName(config.getAppName());
		
		consumer.setMessageModel(MessageModel.BROADCASTING);
		
		consumer.setMessageListener(new ConsumerMessageListener());
		try {
			consumer.start();
			log.info("broadcast 消费者启动,useTime={}",Duration.between(start, LocalDateTime.now()));
		} catch (MQClientException e) {
			e.printStackTrace();
			log.error("broad 消费者启动失败");
		}
	}
	
	@Override
	public void subcsribe(String bizId, DalinSubscribe subscribe, ISubscribeCallback callback) {
		subscribe(bizId, Lists.list(subscribe), callback);
	}

	@Override
	public void subscribe(String bizId, List<DalinSubscribe> subscribes, ISubscribeCallback callback) {
		long st = System.currentTimeMillis();
		try {
			if(addTopic(bizId, subscribes, callback)) {
				log.info("RocketMq启动订阅成功,useTime={}",System.currentTimeMillis()-st);
				callback.onSuccess(bizId, subscribes);
				return;
			}
		} catch (MQClientException e) {
			callback.onFail(bizId, subscribes,e.getErrorMessage());
			log.info("RocketMq启动订阅失败,失败原因");
		}
	}
	
	private boolean addTopic(String bizId, List<DalinSubscribe> subscibeList, ISubscribeCallback subscribeCallback) throws MQClientException {
		if(!Optional.of(subscibeList).isPresent()) {
			subscribeCallback.onFail(bizId, subscibeList,DalinRocketmqEnums.SUBSCRIBE_DATA_NOT_NULL.getMsg());
			return false;
		}
		if(!Optional.of(subscribeCallback).isPresent()) {
			subscribeCallback.onFail(bizId, subscibeList,DalinRocketmqEnums.SUBSCRIBE_CALLBACK_NOT_NULL.getMsg());
			return false;
		}
		for(DalinSubscribe subscribe:subscibeList) {
			if(StringUtils.isEmpty(subscribe.getTopicName())) {
				subscribeCallback.onFail(bizId, subscibeList,DalinRocketmqEnums.SUBSCRIBE_TOPIC_NOT_NULL.getMsg());
				return false;
			}
			if(!Optional.ofNullable(subscribe).map(DalinSubscribe::getCallbacks).isPresent()) {
				subscribeCallback.onFail(bizId, subscibeList,DalinRocketmqEnums.SUBSCRIBE_CONSUMER_CALLBACK_NOT_NULL.getMsg());
				return false;
			}
			consumer.subscribe(subscribe.getTopicName(), StringUtils.isEmpty(subscribe.getTag())?"*":subscribe.getTag());
			factory.putIfAbsent(subscribe.getTopicName(), subscribe.getCallbacks());
		}
			
		return true;
	}
	@PreDestroy
	public void destory() {
		if(!Optional.ofNullable(consumer).isPresent()) {
			consumer.shutdown();
		}
	}

	/**
	 * 
	 * @ClassName: ConsumerMessageListener
	 * @Description: 消费者监听器 
	 * @author 18801
	 * @date 2019年12月23日
	 */
	class ConsumerMessageListener implements MessageListenerConcurrently{

		@Override
		public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
			msgs.parallelStream().forEach(msg->{
					factory.get(msg.getTopic()).parallelStream().forEach(consumerCallback->{
						try {
							log.info("received message[megId={},{} ms later]",msg.getMsgId(),System.currentTimeMillis()-msg.getStoreTimestamp());
							consumerCallback.onSuccess(MessageModel.BROADCASTING,MqUtil.instance().createAccept(msg));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					});
			});
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		
	}
}
