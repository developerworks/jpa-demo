package com.example.jpademo.controller;

import com.example.jpademo.entity.Employee;
import com.example.jpademo.repository.EmployeeRepository;
import com.example.jpademo.util.EmployeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping(path = "/employee/{id}")
    @ResponseBody
    public Employee index(@PathVariable("id") String uuid) {
        Employee employee = employeeRepository.findOne(uuid);
        return employee;
    }
}
