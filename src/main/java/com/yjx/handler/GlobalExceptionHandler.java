package com.yjx.handler;

import com.yjx.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 使用 @RestControllerAdvice 注解，可以捕获所有 @RestController 抛出的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 引入日志记录器，用于在服务器端记录详细错误信息
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 通用异常处理方法
     * 这是最后的防线，捕获所有未被特定处理器捕获的异常。
     * @param e 异常对象
     * @return 统一的错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        // 在服务器日志中记录详细的异常堆栈信息，便于调试
        log.error("服务器发生未知错误: {}", e.getMessage(), e);

        // 创建一个对前端友好的通用错误响应
        Result<Void> result = Result.fail("服务器内部错误，请联系管理员", 500);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 针对数据库约束冲突的特定异常处理器
     * 例如，当尝试插入一个已存在的用户名或配件名时，数据库会抛出此异常。
     * @param e DataIntegrityViolationException 异常对象
     * @return 统一的错误响应
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Result<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        // 在服务器日志中记录具体错误
        log.warn("数据库操作失败，违反了数据完整性约束: {}", e.getMessage());

        // 根据错误信息判断是哪个唯一约束冲突
        // 您的数据库中存在 part_name 和 user_name 的 UNIQUE 约束
        if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("for key 'yjx_user.user_name'")) {
            Result<Void> result = Result.fail("用户名已存在，请更换一个", 409); // 409 Conflict
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("for key 'yjx_parts.part_name'")) {
            Result<Void> result = Result.fail("配件名称已存在，请勿重复添加", 409); // 409 Conflict
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }

        // 其他类型的数据库约束冲突
        Result<Void> result = Result.fail("数据冲突，操作失败", 409); // 409 Conflict
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    /**
     * (推荐) 自定义业务异常处理器
     * 您可以创建一个自定义的 BusinessException 类来处理业务逻辑中的已知错误。
     * @param e BusinessException 异常对象
     * @return 统一的错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        // 业务异常通常不需要记录堆栈，因为它们是预期内的
        log.warn("业务逻辑校验失败: {}", e.getMessage());

        Result<Void> result = Result.fail(e.getMessage(), e.getCode());
        return new ResponseEntity<>(result, HttpStatus.valueOf(e.getCode()));
    }
}