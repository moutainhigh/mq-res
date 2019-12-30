package com.dalin.dto;

import java.util.HashSet;
import java.util.Set;

import com.dalin.service.IConsumerCallback;

import lombok.Data;

/**
 * 
 * @ClassName: Subscribe
 * @Description: 订阅消息类
 * @author 18801
 * @date 2019年12月21日
 */
@Data
public class DalinSubscribe {
	
	/**
	 * 主体名称
	 */
	private String topicName;
	/**
	  * tag标签 
	 */
	private String tag;
	/**消费者**/
	private Set<IConsumerCallback> callbacks = new HashSet<IConsumerCallback>();
	
	public void add(IConsumerCallback callback) {
		callbacks.add(callback);
	}
		
}
