package kim.jerok.practice_spring_23.controller;

import kim.jerok.practice_spring_23.core.annotation.MySameUserIdCheck;
import kim.jerok.practice_spring_23.core.exception.Exception400;
import kim.jerok.practice_spring_23.core.jwt.JwtProvider;
import kim.jerok.practice_spring_23.dto.ResponseDTO;
import kim.jerok.practice_spring_23.dto.user.UserRequest;
import kim.jerok.practice_spring_23.model.log.login.LoginLog;
import kim.jerok.practice_spring_23.model.log.login.LoginLogRepository;
import kim.jerok.practice_spring_23.model.user.User;
import kim.jerok.practice_spring_23.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 회원가입, 로그인, 유저상세보기
 */
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinDTO joinDTO, Errors errors) {
        User userPS = userRepository.save(joinDTO.toEntity());
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(userPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpServletRequest request) {
        User userPS = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(
                        () -> new Exception400("username", "유저네임을 찾을 수 없습니다")
                );

        // 1. 패스워드 검증하기
        if (!userPS.getPassword().equals(loginDTO.getPassword())) {
            throw new Exception400("password", "패스워드가 잘못 입력되었습니다");
        }

        // 2. JWT 생성하기
        String jwt = JwtProvider.create(userPS);

        // 3. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
        userPS.setUpdatedAt(LocalDateTime.now());

        // 4. 로그 테이블 기록
        LoginLog loginLog = LoginLog.builder()
                .userId(userPS.getId())
                .userAgent(request.getHeader("User-Agent"))
                .clientIP(request.getRemoteAddr())
                .build();
        loginLogRepository.save(loginLog);

        // 5. 응답 DTO 생성
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(userPS);
        return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDTO);
    }

    @MySameUserIdCheck
    @GetMapping("/users/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        User userPS = userRepository.findById(id).orElseThrow(
                () -> new Exception400("id", "유저를 찾을 수 없습니다")
        );
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(userPS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
