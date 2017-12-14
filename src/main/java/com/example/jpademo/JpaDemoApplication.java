package com.example.jpademo;

import com.example.jpademo.entity.Employee;
import com.example.jpademo.repository.EmployeeRepository;
import com.example.jpademo.util.EmployeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Employee employee = new Employee();
        employee.setName("苍老师");
        employee.setSalary(800000);
        employee.setType(EmployeeType.CONTRACT);
        employeeRepository.save(employee);
    }
}
