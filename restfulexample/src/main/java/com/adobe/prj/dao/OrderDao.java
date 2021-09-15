package com.adobe.prj.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.adobe.prj.entity.Order;
import com.adobe.prj.entity.ReportDTO;

public interface OrderDao extends JpaRepository<Order, Integer> {
	@Query("select new com.adobe.prj.entity.ReportDTO(o.orderDate, o.total, c.firstName, c.email) from Order o inner join Customer c")
	List<ReportDTO> getReport();
}
