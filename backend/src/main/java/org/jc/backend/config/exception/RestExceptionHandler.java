package org.jc.backend.config.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = GlobalParamException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult handleInternalError(GlobalParamException e) {
        logger.error(e.getMessage());
        return new ExceptionResult(e.getErrorCode(), e.getMessage());
    }

    /*-------------- validation exception ---------------*/
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult handleMethodArgumentNotValidError(MethodArgumentNotValidException e) {
        logger.debug(e.getMessage());
        String s = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        logger.info(s);
        return new ExceptionResult(400, "incorrect para: " + s);
    }

    /*-------------- shiro exceptions ---------------*/
    @ExceptionHandler(value = IncorrectCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResult handleIncorrectCredentialError(IncorrectCredentialsException e) {
        logger.info(e.getMessage());
        return new ExceptionResult(401, e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResult handleAuthenticationError(AuthenticationException e) {
        logger.info(e.getMessage());
        return new ExceptionResult(401, e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResult handleAuthorizationException(AuthorizationException e) {
        logger.info(e.getMessage());
        return new ExceptionResult(403, e.getMessage());
    }
}
