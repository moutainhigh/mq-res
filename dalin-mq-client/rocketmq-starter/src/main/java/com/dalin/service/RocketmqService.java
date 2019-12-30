package com.dalin.service;

import java.util.List;

import com.dalin.dto.DalinMqMessage;
import com.dalin.dto.DalinSubscribe;

/**
 * 
 * @ClassName: RocketmqService
 * @Description: rocketmq 服务接口
 * @author 18801
 * @date 2019年12月20日
 */
public interface RocketmqService {
	
	/**
	 * 
	 * @Title: send
	 * @Description: 同步发送消息
	 * @param  bizId  业务id
	 * @param  message 发送消息体
	 * @param  productCallback 发送消息的回调函数
	 * @return void 返回类型
	 * @throws
	 */
	void send(String bizId,DalinMqMessage message,IProductCallback productCallback);
	/**
	 * 
	 * @Title: send
	 * @Description: 同步发送消息
	 * @param  bizId  业务id
	 * @param  message 发送消息体
	 * @param  productCallback 发送消息的回调函数
	 * @return void 返回类型
	 * @throws
	 */
	void asycSend(String bizId,DalinMqMessage message,IProductCallback productCallback);
	
	void sendTranscation(String bizId,DalinMqMessage message,IProductCallback productCallback);
	
	/**
	 * 
	 * @Title: subscribe
	 * @Description: 集群式订阅
	 * @param  bizId 业务Id
	 * @param  subscribes 订阅参数
	 * @param  callback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void subscribe(String bizId,DalinSubscribe subscribe,ISubscribeCallback callback);
	/**
	 * 
	 * @Title: subscribe
	 * @Description: 集群式订阅
	 * @param  bizId 业务id
	 * @param  subscribe 消息订阅主体
	 * @param  callback 消费者消费消息
	 * @return void 返回类型
	 * @throws
	 */
	void subscribe(String bizId,List<DalinSubscribe> subscribes,ISubscribeCallback callback);
	/**
	 * 
	 * @Title: broadSubscribe
	 * @Description: 传播式订阅
	 * @param  bizId
	 * @param  subscribe
	 * @param  callback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void broadSubscribe(String bizId,DalinSubscribe subscribe,ISubscribeCallback callback);
	/**
	 * 
	 * @Title: broadSubscribe
	 * @Description: 传播式订阅
	 * @param @param bizId
	 * @param @param subscribes
	 * @param @param callback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void broadSubscribe(String bizId,List<DalinSubscribe> subscribes,ISubscribeCallback callback);
	/**
	 * 
	 * @Title: findMessage
	 * @Description: 根据主体名称  消息id查询消息
	 * @param topicName
	 * @param msgId
	 * @return DalinMqSendMessage 消息体
	 * @throws
	 */
	DalinMqMessage findMessage(String topicName,String msgId);
}
