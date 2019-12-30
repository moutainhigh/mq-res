package com.dalin.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import com.dalin.configure.RocketmqConfig;
import com.dalin.dto.DalinBatchMessage;
import com.dalin.dto.DalinMqMessage;

/**
 * 
 * @ClassName: MqUtil
 * @Description: mq工具类
 * @author 18801
 * @date 2019年12月21日
 */
public class MqUtil {

	  /**
     * topic支持字符串
     */
    private static final String TOPIC_VALID_STR = "^[%|a-zA-Z0-9_-]+$";
    /**
     * w字符串匹配
     */
    private static final Pattern TOPIC_PATTERN = Pattern.compile(TOPIC_VALID_STR);
    /**
     * mq扩展符号
     */
	private static final String EXPAND_SEPARATION = "_";
	/**
	 * mq 轨迹topic
	 */
	public static final String DALIN_TRACE_MESSAGE_TOPIC = "dalin_trace_message_topic";
	
	private static final MqUtil INSTANCE = new MqUtil();
	
	private MqUtil() {
		
	}
	
	public static MqUtil instance() {
		
		return INSTANCE;
	}
	
	/**
	 * 
	 * @Title: expandName
	 * @Description: 扩展消费者 生产者的组名
	 * @param  groupName  组名 
	 * @param  config rockermqconfig 配置类
	 * @return String 返回类型
	 * @throws
	 */
	public String expandName(String groupName,RocketmqConfig config) {
		return new StringBuffer().append(config.getEnvName()).append(EXPAND_SEPARATION)
				.append(config.getAppName()).append(EXPAND_SEPARATION).append(groupName).toString();
	}
	/**
	 * 
	 * @Title: createMessage
	 * @Description: 创建发送消息
	 * @param @param message
	 * @param @return 参数
	 * @return Message 返回类型
	 * @throws
	 */
	public Message createMessage(DalinMqMessage message) {
		Message msg = new Message();
		msg.setTopic(message.getTopicName());
		msg.setTags(message.getTag());
		msg.setBody(message.getMsgeBody().getBytes());
		if(Optional.ofNullable(message.getDelayTimeLevel()).isPresent()) {
			msg.setDelayTimeLevel(message.getDelayTimeLevel().getLevel());
		}
		return msg;
	}
	
	/**
	 * 
	 * @Title: createBatchMessage
	 * @Description: 创建批量发送消息
	 * @param @param messages
	 * @param @return 参数
	 * @return List<Message> 返回类型
	 * @throws
	 */
	public List<Message> createBatchMessage(DalinBatchMessage messages){
		List<Message> msgs = new ArrayList<Message>();
		messages.getMsgs().parallelStream().forEach(msg->{
			Message message = new Message();
			message.setTopic(messages.getTopicName());
			message.setTags(messages.getTag());
			message.setBody(msg.getBytes());
			if(Optional.ofNullable(message.getDelayTimeLevel()).isPresent()) {
				message.setDelayTimeLevel(messages.getDelayTimeLevel().getLevel());
			}
			msgs.add(message);
		});
		return msgs;
	}
	
	/**
	 * 
	 * @Title: createAccept
	 * @Description: 根据rocketmq message构造包装的消息
	 * @param @param msg
	 * @param @return
	 * @param @throws UnsupportedEncodingException 参数
	 * @return DalinMqMessage 返回类型
	 * @throws
	 */
	public DalinMqMessage createAccept(MessageExt msg) throws UnsupportedEncodingException {
		 DalinMqMessage messge = DalinMqMessage.builder().msgeBody(new String(msg.getBody(),RemotingHelper.DEFAULT_CHARSET)).build();
		 messge.setTopicName(msg.getTopic());
		 messge.setTag(msg.getTags());
		 return messge;
	}
	
	public boolean validTopic(String topicName) {
		return TOPIC_PATTERN.matcher(topicName).matches();
	}
}
