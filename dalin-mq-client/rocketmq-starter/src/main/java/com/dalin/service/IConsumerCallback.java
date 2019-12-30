package com.dalin.service;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import com.dalin.dto.DalinMqMessage;

/**
 * 
 * @ClassName: IConsumerCannback
 * @Description: 消费者消费消息类
 * @author 18801
 * @date 2019年12月21日
 */
public interface IConsumerCallback {

	void onSuccess(MessageModel messageModel,DalinMqMessage message);
}
