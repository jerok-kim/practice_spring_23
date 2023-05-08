package kim.jerok.practice_spring_23.core.exception;

import kim.jerok.practice_spring_23.dto.ResponseDTO;
import kim.jerok.practice_spring_23.dto.ValidDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// username = ajsdkfljweoiquigwqigjksajgksaj
// "username" : "username의 길이가 너무 깁니다"
// 유효성 검사 실패, 잘못된 파라메터 요청
@Getter
public class Exception400 extends RuntimeException {

    private String key;
    private String value;

    public Exception400(String key, String value) {
        super(value);
        this.key = key;
        this.value = value;
    }

    public ResponseDTO<?> body() {
        ResponseDTO<ValidDTO> responseDTO = new ResponseDTO<>();
        ValidDTO validDTO = new ValidDTO(key, value);
        responseDTO.fail(HttpStatus.BAD_REQUEST, "badRequest", validDTO);
        return responseDTO;
    }

    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

}
