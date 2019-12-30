package com.dalin.config;

import org.apache.rocketmq.client.producer.TransactionListener;

import com.dalin.dto.DalinMqMessage;
import com.dalin.service.IProductCallback;

/**
 * 
 * @ClassName: DalinTransactionProductor
 * @Description: 发送事务消息
 * @author 18801
 * @date 2019年12月23日
 */
public interface DalinTransactionProductor {
	
	void setListener(TransactionListener transactionListener);
	/**
	 * 
	 * @Title: send
	 * @Description: 发送事务消息
	 * @param  bizId
	 * @param  message
	 * @param  productCallback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void send(String bizId, DalinMqMessage message, IProductCallback productCallback);
}
