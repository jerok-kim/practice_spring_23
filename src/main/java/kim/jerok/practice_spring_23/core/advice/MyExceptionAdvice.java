package kim.jerok.practice_spring_23.core.advice;

import kim.jerok.practice_spring_23.core.annotation.MyErrorLogRecord;
import kim.jerok.practice_spring_23.core.exception.Exception400;
import kim.jerok.practice_spring_23.core.exception.Exception401;
import kim.jerok.practice_spring_23.core.exception.Exception403;
import kim.jerok.practice_spring_23.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class MyExceptionAdvice {

    @MyErrorLogRecord
    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(Exception400 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(Exception401 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(Exception403 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> notFound(NoHandlerFoundException e) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.NOT_FOUND, "notFound", e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    // 나머지 모든 예외는 이 친구에게 다 걸러진다
    @MyErrorLogRecord
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "unknownServerError", e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
