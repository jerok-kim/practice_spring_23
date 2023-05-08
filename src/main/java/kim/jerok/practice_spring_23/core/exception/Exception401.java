package kim.jerok.practice_spring_23.core.exception;

import kim.jerok.practice_spring_23.dto.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 인증 안됨
@Getter
public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }

    public ResponseDTO<?> body() {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.UNAUTHORIZED, "unAuthorized", getMessage());
        return responseDTO;
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
