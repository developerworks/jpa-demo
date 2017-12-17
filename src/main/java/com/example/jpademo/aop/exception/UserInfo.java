package com.example.jpademo.aop.exception;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserInfo {
    private String username;
    private String tel;

    public void test() {
        log.info("test");
    }
}
