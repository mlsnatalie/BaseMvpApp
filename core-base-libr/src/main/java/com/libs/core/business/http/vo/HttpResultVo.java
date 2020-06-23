package com.libs.core.business.http.vo;

import java.io.Serializable;

/**
 * 自定义标准接口json返回格式类
 * 该类需要根据每个公司具体的接口规范文档而定
 *
 * @author zhang.zheng
 * @version 2017-11-22
 */
public class HttpResultVo<T> implements Serializable {

    // 返回码
    private int code;
    // 提示信息
    private String message;
    // 数据
    private T result;

    private T data; // 2.5.2新增  数据
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
