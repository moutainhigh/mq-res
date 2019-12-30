package com.dalin.exceptions;

import com.dalin.enums.DalinRocketmqEnums;

/**
 * 
 * @ClassName: SubscribeException
 * @Description: 消息订阅自定义异常
 * @author 18801
 * @date 2019年12月23日
 */
public class SubscribeException extends RuntimeException{
	
	/** **/
	private static final long serialVersionUID = -6135449958054451954L;
	
	private String code;
	
	public SubscribeException(String msg) {
		super(msg);
	}

	public SubscribeException(String code,String msg) {
		super(msg);
		this.code = code;
	}
	
	public SubscribeException(DalinRocketmqEnums enums) {
		super(enums.getMsg());
		this.code = enums.getCode();
	}
	
	public String getCode() {
		return code;
	}
	
}
