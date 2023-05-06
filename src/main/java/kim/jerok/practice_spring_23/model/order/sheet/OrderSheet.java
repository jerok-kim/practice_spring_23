package kim.jerok.practice_spring_23.model.order.sheet;

import kim.jerok.practice_spring_23.model.order.product.OrderProduct;
import kim.jerok.practice_spring_23.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter  // DTO 만들면 삭제해야됨
@Table(name = "order_sheet_tb")
@Entity
public class OrderSheet {  // 주문서

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;  // 주문자

    // checkpoint -> 무한참조
    @OneToMany(mappedBy = "ordersheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>();  // 총 주문 상품 리스트

    @Column(nullable = false)
    private Integer totalPrice;  // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProductList.add(orderProduct);
        orderProduct.syncOrderSheet(this);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        orderProductList.remove(orderProduct);
        orderProduct.syncOrderSheet(null);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 연관관계 메서드 구현 필요

    @Builder
    public OrderSheet(Long id, User user, List<OrderProduct> orderProductList, Integer totalPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.orderProductList = orderProductList;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
