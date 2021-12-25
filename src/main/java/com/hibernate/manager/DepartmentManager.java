package com.hibernate.manager;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.hibernate.model.Department;
import com.hibernate.util.HibernateUtil;

public class DepartmentManager {

	public void addDepartment(Department dept) {

		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			
			session.saveOrUpdate(dept);
			
			HibernateUtil.commitTransaction();
		} catch(Exception ex) {
			HibernateUtil.rollbackTransaction();
			throw ex;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Integer getSize() {
		Integer size = null;
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			List<?> depts = session.createQuery("from Department").list();
			size = depts.size();
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return size;	
	}
}
