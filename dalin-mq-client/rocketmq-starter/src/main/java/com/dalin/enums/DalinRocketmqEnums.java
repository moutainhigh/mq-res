package com.dalin.enums;

/**
 * 
 * @ClassName: DalinRocketmqEnums
 * @Description: mq 自定义枚举类
 * @author 18801
 * @date 2019年12月23日
 */
public enum DalinRocketmqEnums {
	
	SUBSCRIBE_SUCCESS("1000","订阅成功"),
	
	SUBSCRIBE_DATA_NOT_NULL("1001","订阅数据为空"),
	
	SUBSCRIBE_CALLBACK_NOT_NULL("1002","订阅回调函数不能为空"),
	
	SUBSCRIBE_TOPIC_NOT_NULL("1003","订阅主题不能为空"),
	
	SUBSCRIBE_CONSUMER_CALLBACK_NOT_NULL("1004","订阅主体的消费者不能为空"),
	
	PRODUCTOR_SEND_FAIL("2001","发送消息失败"),
	
	PRODUCT_SEND_ERROR("2002","发送消息异常"),
	
	PRODUCT_SEND_MESSAGE_NOT_NULL("2003","发送消息体不能为空"),
	
	PRODUCT_SEND_TOPICNAME_NOT_NULL("2004","发送消息的主题不能为空"),
	
	PRODUCT_SEND_DATA_NOT_NULL("2005","发送消息内容不能为空"),
	
	UNDEFINE(null,null),
	
	;
	
	private String code;
	
	private String msg;
	
	DalinRocketmqEnums(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
	
}
