package org.jc.backend.config.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalParamException extends Exception {
    private int errorCode;

    public GlobalParamException(String errorMessage) {
        super(errorMessage);
    }

    public GlobalParamException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
