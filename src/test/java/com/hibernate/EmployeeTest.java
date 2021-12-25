package com.hibernate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.hibernate.manager.EmpManager;
import com.hibernate.model.Department;
import com.hibernate.model.Employee;

public class EmployeeTest {
	
	@Test
	public void testAddDepartment() {
		//ArticleManager articleManager = new ArticleManager();
		//assertEquals(new Integer(0), articleManager.getSize());
		
		//Department dept = new Department("1", "Sales");
		/*
		DepartmentManager departmentManager = new DepartmentManager();
		departmentManager.addDepartment(dept);
		
		assertEquals(new Integer(1), departmentManager.getSize());
		*/
		
		EmpManager empManager = new EmpManager();
		//Employee emp = new Employee((long) 2,"EMP2", dept);
		//empManager.add(emp);
		
		List<Employee> emps = empManager.getEmps();
		assertEquals(2, emps.size());
	}
}
