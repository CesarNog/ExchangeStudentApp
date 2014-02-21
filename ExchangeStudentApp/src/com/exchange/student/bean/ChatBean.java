/**
 * @author Cesar A. Nogueira 
 */
package com.exchange.student.bean;


/**
 * TO that contains USER data to be persisted
 * 
 * @author Cesar A. Nogueira
 * 
 */
public class ChatBean implements Comparable<ChatBean> {

	/**
	 * User id
	 */
	private Long chatId;

	/**
	 * User 1
	 */
	private Long fkUser1;

	/**
	 * User 2
	 */
	private Long fkUser2;

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public Long getFkUser1() {
		return fkUser1;
	}

	public void setFkUser1(Long fkUser1) {
		this.fkUser1 = fkUser1;
	}

	public Long getFkUser2() {
		return fkUser2;
	}

	public void setFkUser2(Long fkUser2) {
		this.fkUser2 = fkUser2;
	}

	@Override
	public int compareTo(ChatBean another) {
		// TODO Auto-generated method stub
		return 0;
	}	
}
