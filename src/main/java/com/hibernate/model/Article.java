package com.hibernate.model;

public class Article {

	private int id;
	private String title;
	private String content;

	public Article() {
		
	}
	
	public Article(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		
	}

	public void setId(int id) {
		this.id = id;
		
	}

	public int getId() {
		return id;
	}

	public void setContent(String content) {
		// TODO Auto-generated method stub
		this.content = content;
	}
	
}
