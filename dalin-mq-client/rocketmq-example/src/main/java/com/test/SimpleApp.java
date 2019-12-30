package com.test;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dalin.configure.RocketmqConfig;
import com.dalin.dto.DalinMessage;
import com.dalin.dto.DalinMqMessage;
import com.dalin.dto.DalinSubscribe;
import com.dalin.enums.DalinMsgDelayTimeLevel;
import com.dalin.service.IConsumerCallback;
import com.dalin.service.IProductCallback;
import com.dalin.service.ISubscribeCallback;
import com.dalin.service.RocketmqService;

@SpringBootApplication
@RestController
public class SimpleApp {

	public static void main(String[] args) {
		SpringApplication.run(SimpleApp.class, args);
	}
	
	@Autowired
	RocketmqService service;
	
	@Autowired
	RocketmqConfig config;
	
	@GetMapping("/a")
	public String testMq(String msg) {
		System.out.println(config.getConsumerGroupName());
		DalinMqMessage message = new  DalinMqMessage();
		message.setTopicName("test1");
		message.setMsgeBody(msg);
		message.setDelayTimeLevel(DalinMsgDelayTimeLevel.FIVE_LEVEL);
		service.send(UUID.randomUUID().toString(), message, new IProductCallback() {
			
			@Override
			public void onSuccess(String bizId, DalinMessage message) {
				System.out.println("发送成功");
				
			}
			
			@Override
			public void onFail(String bizId, DalinMessage message, String msg) {
				System.out.println("发送失败");
				
			}
			
			@Override
			public void onError(String bizId, DalinMessage message, String msg) {
				System.out.println("发送异常");
				
			}
		});
		return "test";
	}
	
	@Component
	public static class TestConsumer implements IConsumerCallback{
		
		
		@Autowired
		RocketmqService servicea;
		
		@PostConstruct
		public void init() {
			DalinSubscribe subscribe = new DalinSubscribe();
			subscribe.setTopicName("test1");
			subscribe.add(this);
		
			servicea.subscribe(UUID.randomUUID().toString(), subscribe, new ISubscribeCallback() {
				
				@Override
				public void onSuccess(String bizId, List<DalinSubscribe> subscribes) {
					System.out.println("订阅成功");
					
				}
				
				@Override
				public void onFail(String bizId, List<DalinSubscribe> subscribes, String msg) {
					System.out.println("订阅失败");					
				}
			});
			servicea.broadSubscribe(UUID.randomUUID().toString(), subscribe, new ISubscribeCallback() {

				@Override
				public void onSuccess(String bizId, List<DalinSubscribe> subscribes) {
					System.out.println("广播模式订阅成功");
					
				}

				@Override
				public void onFail(String bizId, List<DalinSubscribe> subscribes, String msg) {
					System.out.println("广播模式订阅失败");	
					
				}
				
			});
		}

		@Override
		public void onSuccess(MessageModel model,DalinMqMessage message) {
			System.out.println(model.getClass()+"|"+message.getTopicName()+"|"+message.getTag()+"|"+message.getMsgeBody());
		}
	}
}
