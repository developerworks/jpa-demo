package com.example.jpademo.entity;

import lombok.extern.java.Log;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.validation.ValidationException;

/**
 * 实体监听器, 负责监听实体对象的状态变化,作用可通过POSTMAN请求: http://localhost:10001/userdetails?username=${username}
 * 进行观察, ${username} 使用实际的数据库值进行查询.
 */
@Log
public class UserDetailsListener {
    @PostLoad
    public void postLoad(UserDetails userDetails) {
        log.info("从数据库加载实体, UserDetails, ID=" + userDetails.getId() + ", USERNAME=" + userDetails.getUsername());
    }

    @PrePersist
    public void validate(UserDetails userDetails) {
        if (userDetails.getUsername().length() < 6) {
            throw new ValidationException("The length of username at least 6 chars.");
        }
    }

}