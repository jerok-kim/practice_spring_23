package kim.jerok.practice_spring_23;

import kim.jerok.practice_spring_23.model.order.product.OrderProductRepository;
import kim.jerok.practice_spring_23.model.order.sheet.OrderSheetRepository;
import kim.jerok.practice_spring_23.model.product.ProductRepository;
import kim.jerok.practice_spring_23.model.user.User;
import kim.jerok.practice_spring_23.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class PracticeSpring23Application {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, OrderSheetRepository orderSheetRepository) {
        return (args) -> {
            User jerok = User.builder().username("jerok").password("1234").email("jerok.kim@gmail.com").role("USER").status(true).build();
            User seller = User.builder().username("seller").password("1234").email("seller@email.com").role("SELLER").status(true).build();
            User admin = User.builder().username("admin").password("1234").email("admin@email.com").role("ADMIN").status(true).build();
            userRepository.saveAll(Arrays.asList(jerok, seller, admin));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PracticeSpring23Application.class, args);
    }

}
