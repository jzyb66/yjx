package com.yjx.handler;

import lombok.Getter;

/**
 * 自定义业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    /**
     * @param message 错误信息，将展示给前端用户
     * @param code    HTTP 状态码
     */
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
}