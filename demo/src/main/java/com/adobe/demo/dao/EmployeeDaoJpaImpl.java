package com.adobe.demo.dao;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDaoJpaImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("added using JPA!!!");
	}

}
