package com.zkjg.baas.base.bean;

import java.io.Serializable;

/**
 * @author wylu
 * @date 2022/4/15 下午 11:18
 */
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = -2471004208584398385L;

    protected Integer code;
    protected String message;
    protected T data;

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResult(Integer code) {
        this.code = code;
    }

    public BaseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public static <T> BaseResult<T> create(Integer code) {
        return new BaseResult(code);
    }
}

