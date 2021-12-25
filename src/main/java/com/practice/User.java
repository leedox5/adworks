package com.practice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMP")
public class User {
	private String userid;
	private String username;
	
	public User(String userId, String userName) {
		this.userid = userId;
		this.username = userName;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	@Id
	@Column(name="USER_ID", length=20)
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Column(name="USER_NAME", length=20)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
