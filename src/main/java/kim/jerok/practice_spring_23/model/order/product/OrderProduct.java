package kim.jerok.practice_spring_23.model.order.product;

import kim.jerok.practice_spring_23.model.order.sheet.OrderSheet;
import kim.jerok.practice_spring_23.model.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter  // DTO 만들면 삭제해야됨
@Table(name = "order_product_tb")
@Entity
public class OrderProduct {  // 주문 상품
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // checkpoint -> 무한참조
    @ManyToOne
    private Product product;
    
    @Column(nullable = false)
    private Integer count;  // 상품 주문 갯수
    
    @Column(nullable = false)
    private Integer orderPrice;  // 상품 주문 금액
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    private OrderSheet orderSheet;
    
    // checkpoint -> 편의 메서드 만드는 이유
    public void syncOrderSheet(OrderSheet orderSheet) {
        this.orderSheet = orderSheet;
    }

    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public OrderProduct(Long id, Product product, Integer count, Integer orderPrice, LocalDateTime createdAt, LocalDateTime updatedAt, OrderSheet orderSheet) {
        this.id = id;
        this.product = product;
        this.count = count;
        this.orderPrice = orderPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderSheet = orderSheet;
    }
}
