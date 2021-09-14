package com.adobe.prj.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.prj.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, String>{

}
