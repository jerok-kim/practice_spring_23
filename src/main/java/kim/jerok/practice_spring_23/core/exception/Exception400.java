package kim.jerok.practice_spring_23.core.exception;

import kim.jerok.practice_spring_23.dto.ResponseDTO;
import lombok.Getter;

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
        ResponseDTO<ValidDto> responseDTO
    }

}
