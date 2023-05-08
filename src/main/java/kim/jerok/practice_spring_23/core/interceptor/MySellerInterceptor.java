package kim.jerok.practice_spring_23.core.interceptor;

import kim.jerok.practice_spring_23.core.exception.Exception403;
import kim.jerok.practice_spring_23.core.session.SessionUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class MySellerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");

        if (sessionUser.getRole().equals("SELLER") || sessionUser.getRole().equals("ADMIN")) {
            return true;
        } else {
            throw new Exception403("권한이 없습니다");
        }
    }
}
