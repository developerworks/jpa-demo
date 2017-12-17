package com.example.jpademo.entity;

import com.example.jpademo.util.EmployeeType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 参考资料:
 * <p>
 * - http://blog.csdn.net/lvshuchangyin/article/details/68065775
 */
@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@GenericGenerator(strategy = "uuid", name = "system-uuid")
@org.hibernate.annotations.Table(appliesTo = "employee", comment = "员工信息")
public class Employee {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @NonNull
    private String uuid;

    @NonNull
    private String name;

    @NonNull
    private Integer salary;

    /**
     * 员工类型是一个枚举类型, 如果不指定 @Enumerated 标注, 插入数据的值为从0开始的数字, 如果使用数字, 会有位置的问题,
     * 位置变化后对应的值也会跟着发生变化, 针对这种情况有两种解决办法
     * <p>
     * 1. 声明枚举类型的时候明确的指定值
     * 2. 使用字符串作为值
     */
    @Enumerated(EnumType.STRING)
    private EmployeeType type;
}
