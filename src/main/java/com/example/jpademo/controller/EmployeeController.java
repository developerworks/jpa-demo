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

/**
 * 参考资料
 *
 * - https://stackoverflow.com/questions/12168624/pagination-response-payload-from-a-restful-api
 * - https://segmentfault.com/q/1010000000603084/a-1020000000635842
 * - http://blog.csdn.net/ie8848520/article/details/8161986
 * - https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.data.domain.Page
 * - http://www.importnew.com/24514.html
 * - https://github.com/eugenp/tutorials/blob/master/spring-rest-query-language/src/main/java/org/baeldung/web/util/SearchCriteria.java
 * - ResponseEntity
 * https://stackoverflow.com/questions/33953287/spring-boot-rest-webservice-jpa-pageable-and-filter
 * - http://www.baeldung.com/rest-api-search-language-spring-data-specifications
 *
 */
@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

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
}
