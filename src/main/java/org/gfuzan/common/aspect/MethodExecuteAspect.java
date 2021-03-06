package org.gfuzan.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 方法执行log
 * @author GFuZan
 *
 */
@Aspect
@Component
public class MethodExecuteAspect {
    private static final Logger log = LoggerFactory.getLogger(MethodExecuteAspect.class);
    
    private static final String packageName = "org.gfuzan.modules"; 

    @Pointcut("within("+packageName+"..*)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        Signature signature = point.getSignature();
        String typeName = signature.getDeclaringTypeName();
        if (!typeName.startsWith(packageName + ".")) {
            return point.proceed();
        }
        try {
            log.info(signature + " start");
            result = point.proceed();
        } catch (Throwable e) {
            log.error(signature + " error", e);
            throw e;
        }finally {
            log.info(signature + " end");
        }

        return result;
    }
}
