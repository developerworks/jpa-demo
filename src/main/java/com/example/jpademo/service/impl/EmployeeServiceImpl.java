package com.example.jpademo.service.impl;

import com.example.jpademo.entity.Employee;
import com.example.jpademo.repository.EmployeeRepository;
import com.example.jpademo.service.EmployeeService;
import com.example.jpademo.util.EmployeeSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * http://blog.csdn.net/wncnke/article/details/54408394
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Page<Employee> search(EmployeeSearchCriteria criteria) {

        /**
         * 断言查询条件非空
         */
        Assert.notNull(criteria, "The employee search conditions must not be empty");

        // 分页
        Pageable pageable = new PageRequest(0, 5, new Sort(Sort.Direction.DESC, new String[]{"uuid"}));

        // 查询条件
        Specification<Employee> specification = new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 条件集合
                List<Predicate> list = new ArrayList<>();
                // 判断名称参数是否为空, 如果不为空,添加到搜索条件列表
                if (StringUtils.isNoneBlank(criteria.getName())) {
                    list.add(cb.equal(root.get("name").as(String.class), criteria.getName()));
                }
                if (StringUtils.isNoneBlank(criteria.getUuid())) {
                    list.add(cb.equal(root.get("uuid").as(String.class), criteria.getUuid()));
                }
                // 断言(Predicate)对象列表
                Predicate[] predicates = new Predicate[list.size()];

                // 使用AND连接各个条件
                // SELECT * FROM employees WHERE name = 'a' AND id = 1
                // 上述两个条件可以表达为
                // list.add(cb.equal(root.get("name").as(String.class)))
                // list.add(cb.equal(root.get("id").as(Integer.class)))

                return cb.and(list.toArray(predicates));
            }
        };

        // 查询
        Page<Employee> result = employeeRepository.findAll(specification, pageable);

        // 返回
        return result;
    }
}
