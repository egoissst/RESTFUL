package com.adobe.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adobe.demo.dao.EmployeeDao;

@Service
public class SampleService {
	@Autowired
	private EmployeeDao empDao;
	
	@Autowired
	private EmailService em;
	
	public void insertEmployee() {
		empDao.addEmployee();
	}
	
	public void sendEmail(String msg) {
		em.sendEmail(msg);
	}
}
