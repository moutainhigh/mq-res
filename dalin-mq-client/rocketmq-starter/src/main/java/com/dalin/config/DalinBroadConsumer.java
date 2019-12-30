package com.dalin.config;

import java.util.List;

import com.dalin.dto.DalinSubscribe;
import com.dalin.service.ISubscribeCallback;

public interface DalinBroadConsumer {
	
	void subcsribe(String bizId,DalinSubscribe subscribe,ISubscribeCallback callback);

	void subscribe(String bizId,List<DalinSubscribe> subscribe,ISubscribeCallback callback);
}
