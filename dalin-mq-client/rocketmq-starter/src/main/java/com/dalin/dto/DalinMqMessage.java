package com.dalin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @ClassName: DalinMqSendMessage
 * @Description: 发送的消息
 * @author 18801
 * @date 2019年12月21日
 */
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DalinMqMessage extends DalinMessage{
	
	/**消息体**/
	private String msgeBody;
	
	public String getMsgeBody() {
		return msgeBody;
	}
	
	public void setMsgeBody(String msgeBody) {
		this.msgeBody = msgeBody;
	}
}
