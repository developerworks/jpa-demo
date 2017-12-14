package com.example.jpademo.entity;

import com.example.jpademo.util.EmployeeType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Setter
@Getter
@GenericGenerator(strategy = "uuid", name = "system-uuid")
public class Employee {
    @Id
    @GeneratedValue(generator = "system-uuid")
    private String uuid;

    private String name;

    private Integer salary;

    /**
     * 员工类型是一个枚举类型, 如果不指定标注, 插入数据的值为从0开始的数字, 如果使用数字, 会有位置的问题,
     * 位置变化后响应的值也会跟着发生变化, 针对这种情况有两种解决办法
     * <p>
     * 1. 声明枚举类型的时候明确的指定值
     * 2. 使用字符串作为值
     */
    @Enumerated(EnumType.STRING)
    private EmployeeType type;
}
