package com.sakura.pmp.server.exception;

import com.sakura.pmp.common.response.BaseResponse;
import com.sakura.pmp.common.response.StatusCode;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 通用异常处理器
 * @author : lzl
 * @date : 16:35 2020/5/21
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(AuthorizationException.class)
    public BaseResponse handleAuthorizationException(AuthorizationException e){
        return new BaseResponse(StatusCode.CurrUserHasNotPermission);
    }
}
