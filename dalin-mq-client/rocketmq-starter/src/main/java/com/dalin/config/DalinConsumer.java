package com.dalin.config;

import java.util.List;

import com.dalin.dto.DalinSubscribe;
import com.dalin.service.ISubscribeCallback;

/**
 * 
 * @ClassName: DalinConsumer
 * @Description: 消费者服务
 * @author 18801
 * @date 2019年12月23日
 */
public interface DalinConsumer {

	void subscribe(String bizId,DalinSubscribe subscribe,ISubscribeCallback callback);
	/**
	 * 
	 * @Title: subscribe
	 * @Description: 订阅
	 * @param @param bizId
	 * @param @param subscibeList
	 * @param @param consumerCallback 参数
	 * @return void 返回类型
	 * @throws
	 */
	void subscribe(String bizId,List<DalinSubscribe> subscibeList,ISubscribeCallback subscribeCallback);
	
	
}
