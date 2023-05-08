package kim.jerok.practice_spring_23.core.advice;

import kim.jerok.practice_spring_23.core.session.SessionUser;
import kim.jerok.practice_spring_23.model.log.error.ErrorLog;
import kim.jerok.practice_spring_23.model.log.error.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Aspect
@Component
public class MyErrorLogAdvice {

    private final HttpSession session;
    private final ErrorLogRepository errorLogRepository;

    @Pointcut("@annotation(kim.jerok.practice_spring_23.core.annotation.MyErrorLogRecord)")
    public void myErrorLog() {
    }

    @Before("myErrorLog()")
    public void errorLogAdvice(JoinPoint jp) throws HttpMessageNotReadableException {
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            if (arg instanceof Exception) {
                Exception e = (Exception) arg;
                SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
                if (sessionUser != null) {
                    ErrorLog errorLog = ErrorLog.builder().userId(sessionUser.getId()).msg(e.getMessage()).build();
                    errorLogRepository.save(errorLog);
                }
            }
        }
    }

}
