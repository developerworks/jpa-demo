package com.example.jpademo.entity;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 参考资料
 * <p>
 * - http://blog.csdn.net/buffet518/article/details/1767706
 * - http://blog.sina.com.cn/s/blog_74d509430102wfx6.html
 */

@Component
public class UserDetailsQuery {
    /**
     * 注解 @PersistenceContext 表示实体对象实例所构成的集合, 实体管理器跟踪这个集合中所有实体的状态(CRUD)
     * 可以指定刷新模式, 把状态变化的对象同步到数据库中.
     * <p>
     * - 注意 @PersistenceContext 实体对象集合是在内存中操作的, 需要通过刷新操作同步到数据库.
     * <p>
     * - @PersistenceContext 结合中的对象是受托管的, 也就是被 EntityManager 管理的实体对象. 当 @PersistenceContext 被关闭时,
     * 受托管的实体对象全部脱离实体管理器的管理, 成为非托管对象(游离状态). 这个时候任何之前的受托管对象的状态变化不再会同步数据库.
     */

    // TODO::
    // 1. PersistenceContext 的机制还没有搞清楚, 现在暂时使用 @Autowired
    // 2. 弄清楚 注解为 PersistenceContext 的实体管理器和 @Autowired 注入的有什么不同?

    @PersistenceContext
    //    @Autowired
    EntityManager entityManager;

    public List<UserDetails> findUserDetailsByUsername(String username) {
        return entityManager
            .createNamedQuery("UserDetailsByUsername", UserDetails.class)
            .setParameter(1, username)
            .getResultList();
    }
}
