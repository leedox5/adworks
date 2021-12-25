package com.hibernate.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegsitry;
	static {
		try {
			Configuration configuration = new Configuration().configure("hibernate.oracle.cfg.xml");
			serviceRegsitry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = new Configuration().configure().buildSessionFactory(serviceRegsitry);
			new SchemaExport(configuration).create(true, true);
		} catch (Throwable ex) {
			System.err.println("Error creating Session: " + ex);
			throw ex;
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Transaction beginTransaction() {
		return sessionFactory.getCurrentSession().beginTransaction();
	}

	public static void commitTransaction() {
		sessionFactory.getCurrentSession().getTransaction().commit();
	}

	public static void rollbackTransaction() {
		if(sessionFactory.getCurrentSession().isOpen()) {
			Transaction tx = sessionFactory.getCurrentSession().getTransaction();
			if(tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public static void closeSession() {
		sessionFactory.getCurrentSession().close();
	}

	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
