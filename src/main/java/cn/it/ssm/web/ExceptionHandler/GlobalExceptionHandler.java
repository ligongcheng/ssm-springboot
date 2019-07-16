package cn.it.ssm.web.ExceptionHandler;

import cn.it.ssm.common.vo.ConResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 全局处理 系统自定义 未知异常
     * @param e
     * @return
     */
    @ExceptionHandler(AppException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handleAppException(Exception e) {
        log.error(e.getMessage(), e);
        String msg = "应用错误";
        if (StringUtils.isNotBlank(e.getMessage())) {
            msg = e.getMessage();
        }
        return ConResult.error().addMsg(msg);
    }


    /**
     * @return cn.it.ssm.common.vo.ConResult
     * @Author cheng tao
     * @Description //TODO
     * @Date 2019/3/15 12:16
     * @Param [e]
     **/


    @ExceptionHandler(UnauthorizedException.class)
    public ConResult handleUnauthorizedException(Exception e) {
        log.error(e.getMessage(), e);
        return ConResult.error().addMsg("没有权限！");
    }

    /**
     * 处理 账户 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AccountChangeException.class)
    public ConResult handleAccountChangeException(Exception e) {
        log.error(e.getMessage(), e);
        return ConResult.error().addMsg(e.getMessage());
    }

    /**
     * 处理 接口 限制异常
     * @param e
     * @return
     */
    @ExceptionHandler(ApiLimitedException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handleApiLimitedException(Exception e) {
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
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handle(Exception e, BindingResult bindingResult) {
        HashMap<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.error("校验错误: [{}]", map.toString());
        return ConResult.error().addMsg("用户名、密码必须为4-12位的字母、数字、下划线");
    }


    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ConResult.error().addMsg("系统异常");
    }

}
