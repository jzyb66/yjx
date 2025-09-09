package com.yjx.util;

import lombok.Data;

/**
 * 这是一个通用响应结果类 的通用对象
 * @param <T>
 */
@Data
public class Result<T> {
    private Integer code; // 状态码
    private String msg;   // 提示信息
    private T data;       // 返回数据
    private long timestamp; // 请求的时间戳

    /**
     * 成功响应的默认方法
     * @param data 返回的数据
     * @return 返回Result对象
     * @param <T> 返回的数据类型
     */
    public static <T> Result<T> success( String msg,T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
    // 成功返回（仅带数据，消息默认“操作成功”）
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }
    /**
     * 失败响应的默认方法
     * @param msg 失败提示信息
     * @param code  失败状态码
     * @return 返回Result对象
     * @param <T> 返回的数据类型
     */
    public static <T> Result<T> fail(String msg, Integer code) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}