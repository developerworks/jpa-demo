package com.example.jpademo.aop.exception;

import lombok.Data;

@Data
public class Result<T> {
    private Integer status;
    private String msg;
    private T data;

    @Override
    public String toString() {
        return "Result{" +
            "status=" + status +
            ", msg='" + msg + '\'' +
            ", data=" + data +
            '}';

    }

}
