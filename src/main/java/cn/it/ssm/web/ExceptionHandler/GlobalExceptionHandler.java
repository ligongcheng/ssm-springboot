package cn.it.ssm.web.ExceptionHandler;

import cn.it.ssm.common.vo.ConResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ConResult.error().addMsg("未知错误");
    }

    /**
     * 全局处理 系统自定义 未知异常
     * @param e
     * @return
     */
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handleAppException(AppException e) {
        log.error(e.getMessage(), e);
        return ConResult.error().addMsg("应用错误!");
    }

    /**
     * 处理 未授权 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ConResult handleUnauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return ConResult.error().addMsg("Unauthorized!");
    }

    /**
     * 处理 接口 限制异常
     * @param e
     * @return
     */
    @ExceptionHandler(ApiLimitedException.class)
    public ConResult handleApiLimitedException(ApiLimitedException e) {
        //log.error(e.getMessage(), e);
        return ConResult.error().addMsg("接口调用太频繁");
    }

    /**
     * 处理 参数 校验错误
     * @param e
     * @param bindingResult
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handle(BindException e, BindingResult bindingResult) {
        HashMap<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.error("校验错误: [{}]", map.toString());
        return ConResult.error().addData("error", map);
    }


}
