package com.dalin.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dalin.config.DalinBroadConsumer;
import com.dalin.config.DalinConsumer;
import com.dalin.config.DalinProductor;
import com.dalin.config.DalinTransactionProductor;
import com.dalin.dto.DalinMqMessage;
import com.dalin.dto.DalinSubscribe;
import com.dalin.enums.DalinRocketmqEnums;
import com.dalin.exceptions.SubscribeException;
import com.dalin.service.IProductCallback;
import com.dalin.service.ISubscribeCallback;
import com.dalin.service.RocketmqService;
/**
 * 
 * @ClassName: DalinRocketMqServiceImpl
 * @Description: rocketmq服务实现类
 * @author 18801
 * @date 2019年12月21日
 */
public class DalinRocketMqServiceImpl implements RocketmqService{
	
	
	@Autowired
	private DalinProductor productor;
	
	@Autowired
	private DalinTransactionProductor transactionProductor;
	
	@Autowired
	private DalinConsumer consumer;
	
	@Autowired
	private DalinBroadConsumer broadConsumer;
	
	@Override
	public void send(String bizId, DalinMqMessage message, IProductCallback productCallback) {
		if(checkMessage(bizId, message, productCallback)) {
			productor.send(bizId, message, productCallback);
		}
		
	}
	@Override
	public void asycSend(String bizId, DalinMqMessage message, IProductCallback productCallback) {
		if(checkMessage(bizId, message, productCallback)) {
			productor.sendSync(bizId, message, productCallback);
		}
	}

	@Override
	public void sendTranscation(String bizId, DalinMqMessage message, IProductCallback productCallback) {
		transactionProductor.send(bizId, message, productCallback);
	}

	@Override
	public void subscribe(String bizId, List<DalinSubscribe> subscribes, ISubscribeCallback callback) {
		if(checkSubscribeList(bizId, subscribes, callback)) {
			consumer.subscribe(bizId, subscribes, callback);
		}
	}

	@Override
	public DalinMqMessage findMessage(String topicName, String msgId) {
		return null;
	}
	
	/**
	 * 
	 * @Title: checkMessage
	 * @Description: 校验消息是否合规
	 * @param @param bizId
	 * @param @param message
	 * @param @param productCallback
	 * @param @return 参数
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean checkMessage(String bizId,DalinMqMessage message, IProductCallback productCallback) {
		Optional<DalinMqMessage> optional = Optional.of(message);
		if(!optional.isPresent()) {
			productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_MESSAGE_NOT_NULL.getMsg());
			return false;
		}
		if(!optional.map(DalinMqMessage::getTopicName).isPresent()) {
			productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_TOPICNAME_NOT_NULL.getMsg());
			return false;
		}
		if(!optional.map(DalinMqMessage::getMsgeBody).isPresent()) {
			productCallback.onFail(bizId, message, DalinRocketmqEnums.PRODUCT_SEND_DATA_NOT_NULL.getMsg());
			return false;
		}
		return true;
	}
	
	public boolean checkSubscribeList(String bizId, List<DalinSubscribe> subscribes, ISubscribeCallback callback) {
		Optional<List<DalinSubscribe>> subscribesOptional = Optional.of(subscribes);
		if(!subscribesOptional.isPresent()) {
			throw new SubscribeException(DalinRocketmqEnums.SUBSCRIBE_CALLBACK_NOT_NULL);
		}
		for(DalinSubscribe subscribe : subscribes) {
			if(StringUtils.isBlank(subscribe.getTopicName())) {
				callback.onFail(bizId, subscribes, DalinRocketmqEnums.SUBSCRIBE_TOPIC_NOT_NULL.getMsg());
				return false;
			}
			if(subscribe.getCallbacks().size()==0) {
				callback.onFail(bizId, subscribes, DalinRocketmqEnums.SUBSCRIBE_CONSUMER_CALLBACK_NOT_NULL.getMsg());
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void subscribe(String bizId, DalinSubscribe subscribe, ISubscribeCallback callback) {
		consumer.subscribe(bizId, subscribe, callback);
		
	}
	@Override
	public void broadSubscribe(String bizId, DalinSubscribe subscribe, ISubscribeCallback callback) {
		broadConsumer.subcsribe(bizId, subscribe, callback);
		
	}
	@Override
	public void broadSubscribe(String bizId, List<DalinSubscribe> subscribes, ISubscribeCallback callback) {
		broadConsumer.subscribe(bizId, subscribes, callback);
		
	}
}
