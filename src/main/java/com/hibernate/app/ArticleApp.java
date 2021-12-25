package com.hibernate.app;

import java.util.List;

import com.hibernate.manager.ArticleManager;
import com.hibernate.model.Article;

public class ArticleApp {

	public static void main(String[] args) {
		try {
			ArticleManager articleManager = new ArticleManager();
			
			List<Article> articles = articleManager.getArticles();
					
			for(int i=0; i < articles.size(); i++) {
				Article article = (Article)articles.get(i);
				
				System.out.println(article.getTitle());
			}
			articleManager.createUsers();
			
		} catch (Exception e) {
		} finally {
		}
	}
}
