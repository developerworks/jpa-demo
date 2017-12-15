package com.example.jpademo.controller;

import com.example.jpademo.entity.Employee;
import com.example.jpademo.repository.EmployeeRepository;
import com.example.jpademo.service.EmployeeService;
import com.example.jpademo.util.EmployeeSearchCriteria;
import com.example.jpademo.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参考资料
 * <p>
 * - https://stackoverflow.com/questions/12168624/pagination-response-payload-from-a-restful-api
 * - https://segmentfault.com/q/1010000000603084/a-1020000000635842
 * - http://blog.csdn.net/ie8848520/article/details/8161986
 * - https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.data.domain.Page
 * - http://www.importnew.com/24514.html
 * - https://github.com/eugenp/tutorials/blob/master/spring-rest-query-language/src/main/java/org/baeldung/web/util/SearchCriteria.java
 * - ResponseEntity
 * https://stackoverflow.com/questions/33953287/spring-boot-rest-webservice-jpa-pageable-and-filter
 * - http://www.baeldung.com/rest-api-search-language-spring-data-specifications
 * - http://www.cnblogs.com/derry9005/p/6282571.html
 */
@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    EntityManager entityManager;

    @GetMapping(path = "/{id}")
    @ResponseBody
    public Employee index(@PathVariable("id") String uuid) {
        Employee employee = employeeRepository.findOne(uuid);
        return employee;
    }

    /**
     * 如果在URL查询串中包含 metadata 参数, 则分页元数据包含在响应体中. 否则包含在响应头中.
     *
     * @param uuid
     * @param name
     * @param metadata
     * @return
     */
    @GetMapping(path = "/search")
    public ResponseEntity<Object> search(
        @Param("uuid") String uuid,
        @Param("name") String name,
        @Param("metadata") String metadata
    ) {
        logger.info("search param: uuid=" + uuid);
        logger.info("search param: name=" + name);
        logger.info("search param: metadata=" + metadata);

        // 查询条件

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setName(name);
        criteria.setUuid(uuid);
        criteria.setMetadata(metadata);

        // 服务层搜索

        Page<Employee> page = employeeService.search(criteria);

        // 自定义 ResponseBody

        ResponseEntity<Object> responseEntity;

        // 是否在响应头中包含分页元数据

        if (StringUtils.isNoneBlank(criteria.getMetadata())) {
            responseEntity = new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/employees/search");
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            responseEntity = new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }

        return responseEntity;
    }

    /**
     * 但行单列
     * <p>
     * SELECT uuid FROM employee WHERE uuid = :uuid
     *
     * @param uuid
     * @return
     */
    @GetMapping(path = "/single-value-expression")
    public ResponseEntity<String> singleExpression(@Param("uuid") String uuid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // 单值表达式, 因为返回的只是 name 属性, 而不是一个 员工对象
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        // 投影
        criteriaQuery.select(employeeRoot.<String>get("uuid"));

        // WHERE 条件
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNoneBlank(uuid)) {
            // 参数表达式
            ParameterExpression<String> expression = criteriaBuilder.parameter(String.class, "uuid");
            predicates.add(criteriaBuilder.equal(employeeRoot.get("uuid"), expression));
        }
        if (predicates.size() == 0) {
            throw new RuntimeException("需要参数UUID");
        } else if (predicates.size() == 1) {
            criteriaQuery.where(predicates.get(0));
        } else {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        //
        TypedQuery<String> q = entityManager.createQuery(criteriaQuery);
        // 设置命名参数
        // SELECT name FROM employee WHERE uuid = :uuid
        q.setParameter("uuid", uuid);
        String s = q.getSingleResult();

        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    /**
     * 单行多列: 查询结果为元组方式
     * <p>
     * SELECT uuid, name FROM employee WHERE uuid = :uuid
     *
     * @param uuid
     * @return
     */
    @GetMapping(path = "/multiple-columns-expression-tuple")
    public ResponseEntity<Map<String, Object>> multipleColumnsExpressionTuple(@Param("uuid") String uuid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // 查询结果为元组类型
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Employee> employeeRoot = query.from(Employee.class);
        // 投影
        query.select(cb.tuple(
            employeeRoot.<String>get("uuid").alias("uuid"),
            employeeRoot.<String>get("name").alias("name")
        ));
        // WHERE 条件
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNoneBlank(uuid)) {
            ParameterExpression<String> expression = cb.parameter(String.class, "uuid");
            predicates.add(cb.equal(employeeRoot.get("uuid"), expression));
        }
        if (predicates.size() == 0) {
            throw new RuntimeException("必须提供查询参数UUID");
        } else if (predicates.size() == 1) {
            query.where(predicates.get(0));
        } else {
            query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        TypedQuery<Tuple> q = entityManager.createQuery(query);
        // 设置命名参数
        // SELECT name FROM employee WHERE uuid = :uuid
        q.setParameter("uuid", uuid);
        Tuple t = q.getSingleResult();

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> by_position = new HashMap<>();
        by_position.put("description", "通过元组索引访问元组元素");
        by_position.put("uuid", t.get(0));
        by_position.put("name", t.get(1));

        Map<String, Object> by_alias = new HashMap<>();
        by_alias.put("description", "通过别名访问元组元素");
        by_alias.put("uuid", t.get("uuid"));
        by_alias.put("name", t.get("name"));

        result.put("by_position", by_position);
        result.put("by_alias", by_alias);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 单行多列: 查询结果为对象
     * <p>
     * SELECT uuid, name FROM employee WHERE uuid = :uuid
     *
     * @param uuid
     * @return
     */
    @GetMapping(path = "multiple-columns-expression-object")
    public ResponseEntity multipleColumnsExpressionObjectArray(@Param("uuid") String uuid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // 查询结果为对象数组对象
        CriteriaQuery<Object> query = cb.createQuery(Object.class);
        Root<Employee> employeeRoot = query.from(Employee.class);
        // 投影
        query.multiselect(
            employeeRoot.<String>get("uuid").alias("uuid"),
            employeeRoot.<String>get("name").alias("name")
        );
        // WHERE条件集合
        List<Predicate> predicates = new ArrayList<>();
        // 动态条件添加
        if (StringUtils.isNoneBlank(uuid)) {
            ParameterExpression<String> expression = cb.parameter(String.class, "uuid");
            predicates.add(cb.equal(employeeRoot.get("uuid"), expression));
        }
        if (predicates.size() == 0) {
            throw new RuntimeException("必须至少提供查询参数UUID");
        } else if (predicates.size() == 1) {
            query.where(predicates.get(0));
        } else {
            query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        TypedQuery<Object> q = entityManager.createQuery(query);
        // 设置命名参数
        // SELECT name FROM employee WHERE uuid = :uuid
        q.setParameter("uuid", uuid);
        Object object = q.getSingleResult();

        return new ResponseEntity<>(object, HttpStatus.OK);
    }
}
