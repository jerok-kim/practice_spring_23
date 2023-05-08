package kim.jerok.practice_spring_23.dto.product;

import kim.jerok.practice_spring_23.model.product.Product;
import kim.jerok.practice_spring_23.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @Getter
    @Setter
    public static class SaveDTO {
        @NotEmpty
        private String name;

        // 숫자는 NotNull로 검증한다
        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer price;

        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer qty;

        public Product toEntity(User seller) {
            return Product.builder()
                    .name(name)
                    .price(price)
                    .qty(qty)
                    .seller(seller)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UpdateDTO {
        @NotEmpty
        private String name;
        
        @Digits(fraction = 0,integer = 9)
        @NotNull
        private Integer price;
        
        @Digits(fraction = 0,integer = 9)
        @NotNull
        private Integer qty;
    }

}
