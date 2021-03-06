package com.adobe.demo.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
//@Profile("prod")
@ConditionalOnProperty(name = "dao", havingValue = "jpa")
public class EmployeeDaoJpaImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("added using JPA!!!");
	}

}
