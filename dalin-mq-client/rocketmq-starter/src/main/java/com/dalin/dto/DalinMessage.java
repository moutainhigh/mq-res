package com.dalin.dto;

import com.dalin.enums.DalinMsgDelayTimeLevel;

public class DalinMessage {
	/**订阅主体消息体**/
	private String topicName;
	/**tag标签**/
	private String tag;
	/**访问key**/
	private String accessKey;
	/**设置延时**/
	private DalinMsgDelayTimeLevel delayTimeLevel;
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public DalinMsgDelayTimeLevel getDelayTimeLevel() {
		return delayTimeLevel;
	}
	
	public void setDelayTimeLevel(DalinMsgDelayTimeLevel delayTimeLevel) {
		this.delayTimeLevel = delayTimeLevel;
	}
	
}
