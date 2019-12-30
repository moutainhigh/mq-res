package com.dalin.service;

import com.dalin.dto.DalinMessage;

/**
 * 
 * @ClassName: IProductCallback
 * @Description: 消息发送回调函数
 * @author 18801
 * @date 2019年12月21日
 */
public interface IProductCallback {
	
	/**
	 * 
	 * @Title: onSuccess
	 * @Description: 发送消息成功回调函数
	 * @param  bizId 业务id
	 * @param  message 消息体
	 * @return void 返回类型
	 * @throws
	 */
	void onSuccess(String bizId,DalinMessage message);
	
	/**
	 * 
	 * @Title: onFail
	 * @Description: 发送失败返回消息
	 * @param @param bizId
	 * @param @param message
	 * @param @param rocketmqEnums 参数
	 * @return void 返回类型
	 * @throws
	 */
	void onFail(String bizId,DalinMessage message,String msg);
	/**
	 * 
	 * @Title: onError
	 * @Description: 发送消息异常
	 * @param @param bizId
	 * @param @param message
	 * @param @param msg 参数
	 * @return void 返回类型
	 * @throws
	 */
	void onError(String bizId,DalinMessage message,String msg);
}
