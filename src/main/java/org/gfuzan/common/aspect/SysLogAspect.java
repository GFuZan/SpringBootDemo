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
public class SysLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String packageName = "com.bhh.common"; 

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
            logger.info(signature + " start");
            result = point.proceed();
        } catch (Throwable e) {
            logger.error(signature + " error", e);
            throw e;
        }finally {
            logger.info(signature + " end");
        }

        return result;
    }
}
