package kim.jerok.practice_spring_23.core.exception;

import kim.jerok.practice_spring_23.dto.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 리소스 없음
@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ResponseDTO<?> body() {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.NOT_FOUND, "notFound", getMessage());
        return responseDTO;
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
