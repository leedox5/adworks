package com.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.practice.User;

public class DemoUser {
	public static void main(String[] args) {
		Configuration config = new Configuration(); 
		config.configure("hibernate.cfg.xml");
		//config.addAnnotatedClass(User.class);
		
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		SessionFactory factory = config.buildSessionFactory(serviceRegistry);

		new SchemaExport(config).create(true, true);
		
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		
		User user = new User();
		user.setUserid("2");
		user.setUsername("Lee");
		
		session.persist(user);
		
		t.commit();
		session.close();
	}
}
