package com.hibernate.manager;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.hibernate.model.Employee;
import com.hibernate.util.HibernateUtil;

public class EmpManager {

	@SuppressWarnings("unchecked")
	public List<Employee> getEmps() {
		List<Employee> emps = null;
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			emps = (List<Employee>)session.createQuery("SELECT A.name, B.departmentName FROM Employee A LEFT JOIN A.department B").list();
			HibernateUtil.commitTransaction();
		} catch(HibernateException e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return emps;	
	}

	public void add(Employee emp) {
		try {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			session.saveOrUpdate(emp);
			
			HibernateUtil.commitTransaction();
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			HibernateUtil.rollbackTransaction();
			throw ex;
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
