package com.panshi.exception;

import lombok.Data;

/**
 * @description:自定义异常
 * @author:huangxiaolei
 * @create:2019/07/04
 */
@Data
public class CustomizeException extends RuntimeException {

    private int code;

    private String message;

    public CustomizeException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
