/**
 * @author Cesar A. Nogueira 
 */
package com.exchange.student.bean;

import android.content.Context;

/**
 * TO that contains USER data to be persisted
 * 
 * @author Cesar A. Nogueira
 * 
 */
public class UserBean implements Comparable<UserBean> {

	/**
	 * User id
	 */
	private Long userId;

	/**
	 * SSN
	 */
	private String ssn;

	/**
	 * Profile id
	 */
	private Integer userProfileId;

	/**
	 * Profile id
	 */
	private Integer groupId;

	/**
	 * Profile id
	 */
	private Integer chatId;

	/**
	 * Student id
	 */
	private Integer studentId;

	/**
	 * User username
	 */
	private String username;

	/**
	 * User password
	 */
	private String password;

	/**
	 * User password
	 */
	private String encryptedPassword;

	public UserBean() {

	}

	public UserBean(Long userId, String ssn, Integer userProfileId,
			Integer groupId, Integer chatId, Integer studentId,
			String username, String password, Context ctx) {
		super();
		this.userId = userId;
		this.ssn = ssn;
		this.userProfileId = userProfileId;
		this.groupId = groupId;
		this.chatId = chatId;
		this.studentId = studentId;
		this.username = username;
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	@Override
	public int compareTo(UserBean another) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatId == null) ? 0 : chatId.hashCode());
		result = prime
				* result
				+ ((encryptedPassword == null) ? 0 : encryptedPassword
						.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
		result = prime * result
				+ ((studentId == null) ? 0 : studentId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((userProfileId == null) ? 0 : userProfileId.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBean other = (UserBean) obj;
		if (chatId == null) {
			if (other.chatId != null)
				return false;
		} else if (!chatId.equals(other.chatId))
			return false;
		if (encryptedPassword == null) {
			if (other.encryptedPassword != null)
				return false;
		} else if (!encryptedPassword.equals(other.encryptedPassword))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (ssn == null) {
			if (other.ssn != null)
				return false;
		} else if (!ssn.equals(other.ssn))
			return false;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userProfileId == null) {
			if (other.userProfileId != null)
				return false;
		} else if (!userProfileId.equals(other.userProfileId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserBean [userId=" + userId + ", ssn=" + ssn
				+ ", userProfileId=" + userProfileId + ", groupId=" + groupId
				+ ", chatId=" + chatId + ", studentId=" + studentId
				+ ", username=" + username + ", password=" + password
				+ ", encryptedPassword=" + encryptedPassword + "]";
	}

}
