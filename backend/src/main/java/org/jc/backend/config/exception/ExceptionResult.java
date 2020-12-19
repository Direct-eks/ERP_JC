package org.jc.backend.config.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResult {
    private int code;
    private String msg;

    public ExceptionResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
