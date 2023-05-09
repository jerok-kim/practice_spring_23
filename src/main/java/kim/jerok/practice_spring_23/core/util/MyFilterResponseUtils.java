package kim.jerok.practice_spring_23.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.jerok.practice_spring_23.dto.ResponseDTO;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filter는 예외 핸들러로 처리 못한다
public class MyFilterResponseUtils {
    public static void badRequest(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(400);
        resp.setContentType("application/json; charset=utf-8");

        ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(HttpStatus.BAD_REQUEST, "badRequest", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDTO);
        resp.getWriter().println(responseBody);
    }
    
    public static void unAuthorized(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(401);
        resp.setContentType("application/json; charset=utf-8");
        
        ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(HttpStatus.UNAUTHORIZED, "unAuthorized", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDTO);
        resp.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(403);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(HttpStatus.FORBIDDEN, "forbidden", e.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDTO);
        resp.getWriter().println(responseBody);
    }
}
