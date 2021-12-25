package com.hibernate.manager;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import com.hibernate.model.Article;
import com.hibernate.util.HibernateUtil;
import com.practice.User;


public class ArticleManager {

	public Integer addArticle(String title, String content) {
		Article article = null;
		Integer articleId = null;
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
		
			article = new Article();
			article.setTitle(title);
			article.setContent(content);
			articleId = (Integer) session.save(article);
			
			HibernateUtil.commitTransaction();
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			HibernateUtil.rollbackTransaction();
			throw ex;
		} finally {
			HibernateUtil.closeSession();
		}
		return articleId;
	}

	public Article getArticle(Integer articleId) {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getCurrentSession();
		
		Article article = (Article) session.get(Article.class, articleId); 
		HibernateUtil.closeSession();
		return article;
	}

	@SuppressWarnings("unchecked")
	public List<Article> getArticles() {
		List<Article> articles = null;
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			articles = (List<Article>)session.createQuery("FROM Article").list();
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return articles;
	}

	public Integer getSize() {
		Integer size = null;
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			List<?> articles = session.createQuery("FROM Article").list();
			size = articles.size();
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return size;
	}

	public void updateArticle(Integer articleId, String title) {
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			
			Article article = (Article) session.get(Article.class, articleId);
			article.setTitle(title);
			
			session.update(article);
			
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void deleteArticle(Integer articleId) {
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			
			Article article = (Article) session.get(Article.class, articleId);
			
			session.delete(article);
			
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
			
	}

	public void addArticles(int rows) {
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			
			for(int i=0; i < rows; i++) {
				String title = "Title " + i;
				String content = "Content " + i;
				Article article = new Article(title, content);
				
				session.save(article);
				if(i % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public void createUsers() {
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			
			Query query = session.createQuery("DELETE FROM User");
			query.executeUpdate();
			
			List<?> articles = session.createQuery("FROM Article").list();
			
			
			for(int i=0; i < articles.size(); i++) {
				Article article = (Article) articles.get(i);
				
				User user = new User("user-" + i, article.getContent());
				
				session.save(user);
				if(i % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		
	}

}
