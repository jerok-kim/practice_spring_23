package kim.jerok.practice_spring_23.core.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kim.jerok.practice_spring_23.core.exception.Exception400;
import kim.jerok.practice_spring_23.core.jwt.JwtProvider;
import kim.jerok.practice_spring_23.core.session.SessionUser;
import kim.jerok.practice_spring_23.core.util.MyFilterResponseUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MyJwtVerifyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String prefixJwt = req.getHeader(JwtProvider.HEADER);

        if (prefixJwt == null) {
            MyFilterResponseUtils.unAuthorized(resp, new Exception400("authorization", "토큰이 전달되지 않았습니다"));
            return;
        }

        String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "");
        try {
            DecodedJWT decodedJWT = JwtProvider.verify(jwt);
            Long id = decodedJWT.getClaim("id").asLong();
            String role = decodedJWT.getClaim("role").asString();

            // 세션을 사용하는 이유는 role(권한) 처리를 하기 위해서이다.
            HttpSession session = req.getSession();
            SessionUser sessionUser = SessionUser.builder().id(id).role(role).build();
            session.setAttribute("sessionUser", sessionUser);
            System.out.println("세션 생성됨");
            chain.doFilter(req, resp);
        } catch (SignatureVerificationException sve) {
            MyFilterResponseUtils.unAuthorized(resp, sve);
        } catch (TokenExpiredException tee) {
            MyFilterResponseUtils.unAuthorized(resp, tee);
        }
    }
}
