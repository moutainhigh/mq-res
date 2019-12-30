package com.dalin.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: DalinBatchMesage
 * @Description: 批量信息
 * @author 18801
 * @date 2019年12月23日
 */
public class DalinBatchMessage extends DalinMessage{
	
	private List<String> msgs= new ArrayList<String>();
	
	public List<String> getMsgs() {
		return msgs;
	}
	
	public void add(String message) {
		msgs.add(message);
	}
}
