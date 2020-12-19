package org.jc.backend.config.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalException extends Exception {
    private int errorCode;

    public GlobalException(String errorMessage) {
        super(errorMessage);
    }

    public GlobalException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
