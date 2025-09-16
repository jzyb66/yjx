package com.yjx.handler;

import com.yjx.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 * 使用 @RestControllerAdvice 注解，可以捕获所有 @Controller 和 @RestController
 * 中抛出的异常，并进行统一处理，返回标准化的JSON响应。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 通用异常处理方法。
     * 此方法会捕获所有未被其他 @ExceptionHandler 方法处理的 Exception 及其子类。
     *
     * @param e 捕获到的异常对象
     * @return 返回给前端的标准化错误结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 在服务器后台记录详细的错误日志，用于开发人员排查问题。
        // 使用 e 作为最后一个参数，Slf4j会打印出完整的堆栈跟踪信息。
        log.error("服务器捕获到未知异常: {}", e.getMessage(), e);

        // 向前端返回一个通用的、友好的错误提示，隐藏服务器内部细节。
        return Result.fail("服务器内部错误，请联系管理员", 500);
    }
}