package com.hibernate;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hibernate.manager.ArticleManager;
import com.hibernate.model.Article;

public class ArticleTest {
	
	@Test
	public void testAddArticle() {
		
		ArticleManager articleManager = new ArticleManager();
		assertEquals(true, articleManager instanceof ArticleManager);

		Integer rowCount = articleManager.getSize();
		
		Integer articleId = articleManager.addArticle("title1", "내용1");
		
		Article article = articleManager.getArticle(articleId);
		assertEquals("title1", article.getTitle());
		assertEquals(new Integer(rowCount + 1), articleManager.getSize());

		articleManager.updateArticle(articleId, "title2");
		article = articleManager.getArticle(articleId);
		assertEquals("title2", article.getTitle());
		
		articleManager.deleteArticle(articleId);
		assertEquals(rowCount, articleManager.getSize());
		
		
		articleManager.addArticles(1000);
		assertEquals(new Integer(1000), articleManager.getSize());
		
	}
	
}
