package com.panshi.dta;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author huangxiaolei
 */
@Getter
@Setter
public class Message<T> implements Serializable {

    private static final long serialVersionUID = 758412354L;

    private int code;

    private T message;

    public Message(int code, T message) {
        this.code = code;
        this.message = message;
    }

    public Message() {
    }
}
