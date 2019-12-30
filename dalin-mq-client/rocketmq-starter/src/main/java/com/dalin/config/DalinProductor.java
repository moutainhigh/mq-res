package com.dalin.config;

import com.dalin.dto.DalinBatchMessage;
import com.dalin.dto.DalinMqMessage;
import com.dalin.service.IProductCallback;

/**
 * 
 * @ClassName: DalinProductor
 * @Description: 生产者服务类
 * @author 18801
 * @date 2019年12月23日
 */
public interface DalinProductor {

	/**
	 * 
	 * @Title: send
	 * @Description: 同步发送
	 * @param @param biz
	 * @param @param message
	 * @param @param productCallback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void send(String bizId,DalinMqMessage message,IProductCallback productCallback);
	/**
	 * 
	 * @Title: sendSync
	 * @Description: 异步发送
	 * @param @param biz
	 * @param @param message
	 * @param @param productCallback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void sendSync(String bizId,DalinMqMessage message,IProductCallback productCallback);
	/**
	 * 
	 * @Title: batchSend
	 * @Description: 批量发送消息
	 * @param @param bizId
	 * @param @param batchMessage
	 * @param @param productCallback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void batchSend(String bizId,DalinBatchMessage batchMessage,IProductCallback productCallback);
	
}
