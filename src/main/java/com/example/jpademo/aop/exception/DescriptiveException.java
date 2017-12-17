package com.example.jpademo.aop.exception;

/**
 * 参考资料
 *
 * - http://blog.csdn.net/qq_31001665/article/details/71357825
 */
public class DescriptiveException extends RuntimeException {

    private Integer code;

    /**
     * 继承exception，加入错误状态值
     *
     * @param exceptionEnum
     */
    public DescriptiveException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }

    /**
     * 自定义错误信息
     *
     * @param message
     * @param code
     */
    public DescriptiveException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}