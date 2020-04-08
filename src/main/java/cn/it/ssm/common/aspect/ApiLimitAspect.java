package cn.it.ssm.common.aspect;


import cn.it.ssm.common.ExceptionHandler.ApiLimitedException;
import cn.it.ssm.common.annotation.ApiLimit;
import cn.it.ssm.common.service.ApiLimitService;
import cn.it.ssm.common.shiro.util.ShiroUtils;
import cn.it.ssm.sys.domain.auto.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Order(1)
@Component
public class ApiLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiLimitAspect.class);

    /*@Autowired
    private RedisTemplate<Object,Object> redisTemplate;*/

    @Autowired
    private ApiLimitService apiLimitService;

    @Pointcut("@annotation(cn.it.ssm.common.annotation.ApiLimit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        boolean isLimited = isLimited(point);
        if (!isLimited) {
            result = point.proceed();
            return result;
        } else {
            throw new ApiLimitedException();
        }
    }

    private boolean isLimited(ProceedingJoinPoint joinPoint) {
        SysUser user = ShiroUtils.getUserOrAnonymous();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getName();
        ApiLimit apiLimit = method.getAnnotation(ApiLimit.class);

        String policy = apiLimit.value();
        if (StringUtils.isNotBlank(policy)) {
            String[] args = StringUtils.split(policy, ",");
            if (args.length == 2) {
                List<String> keys = new ArrayList<>();
                keys.add("apiLimit:" + className + ":" + methodName + ":" + user.getUsername());
                //List<String> args = new ArrayList<>(Arrays.asList(split));
                boolean isLimited = apiLimitService.isLimited(keys, args[0], args[1]);
                if (isLimited) {
                    logger.warn("用户: [{}] 调用: [{}] 超过 [{}] 的限制", user.getUsername()
                        , className + "." + methodName, policy);
                } else {
                    logger.info("用户: [{}] 调用: [{}] 成功", user.getUsername(), className + "." + methodName);
                }
                return isLimited;
            }
        }
        return true;
    }

}
