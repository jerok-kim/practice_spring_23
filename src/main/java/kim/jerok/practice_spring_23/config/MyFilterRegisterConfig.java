package kim.jerok.practice_spring_23.config;

import kim.jerok.practice_spring_23.core.filter.MyJwtVerifyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyFilterRegisterConfig {
    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<MyJwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyJwtVerifyFilter());
        registration.addUrlPatterns("/user/*");
        registration.setOrder(1);
        return registration;
    }
}
