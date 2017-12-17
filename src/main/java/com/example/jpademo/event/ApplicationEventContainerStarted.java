package com.example.jpademo.event;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Spring 容器启动完成
 * <p>
 * 参考资料
 * <p>
 * - http://zhaoshijie.iteye.com/blog/1974682
 */

@Log
public class ApplicationEventContainerStarted implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器 ApplicationContext
        if (event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {

        }
        // 子容器, 比如 WebApplicationContext
        else {

        }
    }
}
