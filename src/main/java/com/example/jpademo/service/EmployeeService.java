package com.example.jpademo.service;

import com.example.jpademo.entity.Employee;
import com.example.jpademo.util.EmployeeSearchCriteria;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    Page<Employee> search(EmployeeSearchCriteria criteria);
}
