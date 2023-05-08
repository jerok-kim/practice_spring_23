package kim.jerok.practice_spring_23.core.advice;

import kim.jerok.practice_spring_23.core.exception.Exception403;
import kim.jerok.practice_spring_23.core.session.SessionUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Aspect
@Component
public class MySameUserIdAdvice {

    private final HttpSession session;

    // 깃발에 별칭주기
    @Pointcut("@annotation(kim.jerok.practice_spring_23.core.annotation.MySameUserIdCheck)")
    public void mySameUserId() {
    }

    @Before("mySameUserId()")
    public void sameUserIdAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();

        IntStream.range(0, parameters.length).forEach(
                (i) -> {
                    if (parameters[i].getName().equals("id") && parameters[i].getType() == Long.class) {
                        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
                        Long id = (Long) args[i];
                        if (sessionUser.getId() != id) {
                            throw new Exception403("해당 id에 접근할 권한이 없습니다");
                        }
                    }
                }
        );
    }

}
