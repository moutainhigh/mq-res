package com.dalin.service;

import java.util.List;

import com.dalin.dto.DalinSubscribe;

/**
 * 
 * @ClassName: ISubscribeCallback
 * @Description: 消息订阅服务类
 * @author 18801
 * @date 2019年12月21日
 */
public interface ISubscribeCallback {
	
	/**
	 * 
	 * @Title: onSuccess
	 * @Description: 订阅成功的回调函数
	 * @param  bizId 业务id
	 * @param  message 消息参数
	 * @return void 返回类型
	 * @throws
	 */
	void onSuccess(String bizId,List<DalinSubscribe> subscribes);
	/**
	 * 
	 * @Title: onFail
	 * @Description: 消息订阅失败回调函数
	 * @param @param bizId
	 * @param @param subscribe 参数
	 * @return void 返回类型
	 * @throws
	 */
	void onFail(String bizId,List<DalinSubscribe> subscribes,String msg);
	
}
