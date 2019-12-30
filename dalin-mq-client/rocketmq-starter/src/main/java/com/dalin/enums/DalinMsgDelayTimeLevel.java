package com.dalin.enums;

/**
 * 
 * @ClassName: DalinMsgDelayTimeLevel
 * @Description: 消息延迟级别
 * @author 18801
 * @date 2019年12月27日
 */
public enum DalinMsgDelayTimeLevel {
	ONE_LEVEL(1,"1S"),
	TWO_LEVEL(2,"5S"),
	THREE_LEVEL(3,"10S"),
	FOUR_LEVEL(4,"30S"),
	FIVE_LEVEL(5,"1M"),
	SIX_LEVEL(6,"2M"),
	SEVEN_LEVEL(7,"3M"),
	EIGHT_LEVEL(8,"4M"),
	NINE_LEVEL(9,"5M"),
	TEN_LEVEL(10,"6M"),
	ELEVEN_LEVEL(11,"7M"),
	TWELVE_LEVEL(12,"8M"),
	THIRTEEN_LEVEL(13,"9M"),
	FOURTEEN_LEVEL(14,"10M"),
	FIFTEEN_LEVEL(15,"20M"),
	SIXTEEN_LEVEL(16,"30M"),
	SEVENTEEN_LEVEL(17,"1H"),
	EIGHTEEN_LEVEL(18,"2H"),
	
	;
	
	//延时级别
	private int level;
	//延时时长
	private String descript;
	
	DalinMsgDelayTimeLevel(int level,String descript){
		this.level = level;
		this.descript = descript;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getDescript() {
		return descript;
	}
}
