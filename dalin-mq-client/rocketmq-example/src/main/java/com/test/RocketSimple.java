package com.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class RocketSimple {

	private static String NAMESRVADDR="192.168.56.102:9876";
	
	private static String TOPICNAME = "test1";
	
	public static void main(String[] args) throws Exception {
		transactionProductor();
	}
	
	public static void product() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("a");
        producer.setNamesrvAddr(NAMESRVADDR);
        List<Message> messages = new ArrayList<>();
        producer.start();
        Message a = new Message(TOPICNAME, "TagA", "OrderID001", "Hello world 0".getBytes());
        messages.add(a);
        messages.add(new Message(TOPICNAME, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(TOPICNAME, "TagA", "OrderID003", "Hello world 2".getBytes()));
        producer.send(messages);
        
        
    }
	
	public static void transactionProductor() throws Exception {
		TransactionMQProducer productor = new TransactionMQProducer("b");
		productor.setNamesrvAddr(NAMESRVADDR);
		productor.start();
		Message message = new Message(TOPICNAME, "TagA", "OrderID001", "test transaction".getBytes());
		productor.setTransactionListener(new Listener());
		productor.sendMessageInTransaction(message,null);
		message = new Message(TOPICNAME, "TagA", "OrderID001", "test transaction2".getBytes());
	//	productor.setTransactionListener(new Listener1());
		productor.sendMessageInTransaction(message,null);
		
		/* productor.setTransactionCheckListener(new CheckListener()); */
	//	productor.sendMessageInTransaction(message, null);
	}
	
	public static void accept() throws InterruptedException, MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("testGroup");
         
        consumer.setNamesrvAddr(NAMESRVADDR);
        
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 System.out.printf("%s Receive New Messages: %s %s %n", Thread.currentThread().getName(), msgs.size(),new String(msgs.get(0).getBody()));
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
        });

        consumer.start(); 
        consumer.subscribe(TOPICNAME, "*");
        System.out.printf("Consumer Started.%n");
    }
	
	static class Listener implements TransactionListener {
	
		@Override
		public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
				System.out.println(new String(msg.getBody()));
			return LocalTransactionState.UNKNOW;
		}
	
		@Override
		public LocalTransactionState checkLocalTransaction(MessageExt msg) {
				System.out.println("瞎搞搞");
			return LocalTransactionState.COMMIT_MESSAGE;
		}
	}
	
	static class Listener1 implements TransactionListener {
		
		@Override
		public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
				System.out.println("haha"+new String(msg.getBody()));
			return LocalTransactionState.UNKNOW;
		}
	
		@Override
		public LocalTransactionState checkLocalTransaction(MessageExt msg) {
				System.out.println(new String(msg.getBody()));
			return LocalTransactionState.COMMIT_MESSAGE;
		}
	}
}
