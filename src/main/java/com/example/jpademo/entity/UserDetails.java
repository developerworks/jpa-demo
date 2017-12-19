package com.example.jpademo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 使用 @NamedQuery 中执行JPQL查询
 * 使用 @NamedNativeQuery 标注执行纯SQL查询
 *
 * 参考资料:
 *
 * - https://github.com/ydsworld/Hibernate_NamedQuery/blob/master/src/namedQuery/UserDetails.java
 *
 * 用法说明:
 *
 * - @NamedNativeQuery 注解可以放在任何实体(@Entity类)上面定义查询名称和SQL文本.
 * - @NamedNativeQuery 注解这种定义结果类, 标识查询结果的封装类型
 * - 还可以给 @NamedNativeQuery 注解的SQL查询命名, 给它一个名字, 可以使用EntityManager.createNamedQuery创建和执行查询,
 *   比如: 下面我们定义了一个名称为 UserDetails.byName 的查询, 让后通过 EntityManager.createNamedQuery 来创建查询,并且使用
 *
 *
 */

@Entity
@NamedNativeQueries(value = {
    @NamedNativeQuery(
        name="UserDetailsByUsername",
        query="SELECT * FROM user_details WHERE username = ?",
        resultClass=UserDetails.class
    )
})
@Getter
@Setter
public class UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String username;
}