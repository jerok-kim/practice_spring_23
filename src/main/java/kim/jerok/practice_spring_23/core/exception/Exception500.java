package kim.jerok.practice_spring_23.core.exception;

import kim.jerok.practice_spring_23.dto.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 서버 에러
@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }

    public ResponseDTO<?> body() {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "serverError", getMessage());
        return responseDTO;
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
