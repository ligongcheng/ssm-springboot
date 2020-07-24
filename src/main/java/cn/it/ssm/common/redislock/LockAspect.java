package cn.it.ssm.common.redislock;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(-1)
public class LockAspect {

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final TemplateParserContext templateParserContext = new TemplateParserContext();

    @Pointcut("@annotation(cn.it.ssm.common.redislock.Lock)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        RedisLock lock = RedisLock.instance();
        Lock annotation = getAnnotation(point, Lock.class);
        lock.setAcquireIdle(annotation.acquireIdle());
        lock.setCheckIdle(annotation.checkIdle());
        lock.setCheck(annotation.isCheck());
        Object proceed = null;
        try {
            Boolean aBoolean = lock(lock, point);
            if (aBoolean) {
                proceed = point.proceed();
            } else {
                throw new RuntimeException("系统繁忙，请稍后再试！");
            }
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            unlock(lock, point);
        }
        return proceed;
    }

    private void unlock(RedisLock lock, ProceedingJoinPoint point) {
        Lock annotation = getAnnotation(point, Lock.class);
        String key = parseString(annotation.key(), point);
        String value = parseString(annotation.value(), point);
        lock.unlock(key, value);
    }

    private Boolean lock(RedisLock lock, ProceedingJoinPoint point) {
        Lock annotation = getAnnotation(point, Lock.class);
        String key = parseString(annotation.key(), point);
        //System.out.println(key);
        String value = parseString(annotation.value(), point);
        //System.out.println(value);
        return lock.lock(key, value, annotation.expire(), annotation.timeOut());
    }

    private String parseString(String key, ProceedingJoinPoint point) {
        String[] paramNames = getParamterNames(getMethod(point));
        Object[] args = point.getArgs();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        Expression expression = this.parser.parseExpression(key);
        return expression.getValue(context, String.class);
    }

    private <T extends Annotation> T getAnnotation(ProceedingJoinPoint point, Class<T> annotationClass) {
        Method method = getMethod(point);
        return method.getAnnotation(annotationClass);
    }

    private Method getMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getMethod();
    }

    private String[] getParamterNames(Method method) {
        return paramNameDiscoverer.getParameterNames(method);
    }


}
