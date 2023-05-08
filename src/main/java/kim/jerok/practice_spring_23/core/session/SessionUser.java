package kim.jerok.practice_spring_23.core.session;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionUser {
    private Long id;
    private String role;
    
    @Builder
    public SessionUser(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
