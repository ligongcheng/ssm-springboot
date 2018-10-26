package cn.it.ssm.common.aspect;

import cn.it.ssm.common.annotation.Log;
import cn.it.ssm.common.shiro.util.HttpContextUtils;
import cn.it.ssm.common.shiro.util.IPUtils;
import cn.it.ssm.common.shiro.util.ShiroUtils;
import cn.it.ssm.common.shiro.util.SysLog;
import cn.it.ssm.domain.auto.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Order(10)
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

	/*@Autowired
	private LogService logService;*/

	/*@Autowired
	ObjectMapper mapper;*/

    @Pointcut("@annotation(cn.it.ssm.common.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            result = point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long time = System.currentTimeMillis() - beginTime;
        saveLog(point, time);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        SysUser user = ShiroUtils.getUserOrAnonymous();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog log = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            log.setOperation(logAnnotation.value());
        }
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        Object[] args = joinPoint.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
            }
            log.setParams(params.toString());
        }
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setIp(IPUtils.getIpAddr(request));
        log.setUsername(user.getUsername());
        log.setTime(time);
        log.setCreateTime(new Date());
        //log.setLocation(AddressUtils.getRealAddressByIP(log.getIp(), mapper));
        //日志
        logger.debug(log.toString());
        //存入redis
        ListOperations<Object, Object> opsForList = redisTemplate.opsForList();
        opsForList.rightPush("log:" + user.getUsername(), log);
    }
}
