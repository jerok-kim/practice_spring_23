package kim.jerok.practice_spring_23.controller;

import kim.jerok.practice_spring_23.core.annotation.MySessionStore;
import kim.jerok.practice_spring_23.core.exception.Exception400;
import kim.jerok.practice_spring_23.core.exception.Exception403;
import kim.jerok.practice_spring_23.core.session.SessionUser;
import kim.jerok.practice_spring_23.dto.ResponseDTO;
import kim.jerok.practice_spring_23.dto.order.OrderRequest;
import kim.jerok.practice_spring_23.model.order.product.OrderProduct;
import kim.jerok.practice_spring_23.model.order.product.OrderProductRepository;
import kim.jerok.practice_spring_23.model.order.sheet.OrderSheet;
import kim.jerok.practice_spring_23.model.order.sheet.OrderSheetRepository;
import kim.jerok.practice_spring_23.model.product.Product;
import kim.jerok.practice_spring_23.model.product.ProductRepository;
import kim.jerok.practice_spring_23.model.user.User;
import kim.jerok.practice_spring_23.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 주문하기(고객), 주문목록보기(고객), 주문목록보기(판매자), 주문취소하기(고객), 주문취소하기(판매자)
 */
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<?> save(@RequestBody @Valid OrderRequest.SaveDTO saveDTO, Errors errors, @MySessionStore SessionUser sessionUser) {
        // 1. 세션값으로 유저 찾기
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception400("id", "해당 유저를 찾을 수 없습니다")
                );

        // 2. 상품 찾기
        List<Product> productListPS = productRepository.findAllById(saveDTO.getIds());

        // 3. 주문 상품
        List<OrderProduct> orderProductListPS = saveDTO.toEntity(productListPS);

        // 4. 주문서 만들기
        Integer totalPrice = orderProductListPS.stream().mapToInt((orderProduct) -> orderProduct.getOrderPrice()).sum();
        OrderSheet orderSheet = OrderSheet.builder()
                .user(userPS)
                .totalPrice(totalPrice)
                .build();
        OrderSheet orderSheetPS = orderSheetRepository.save(orderSheet);

        // 5. 주문서에 상품추가하고 재고감소하기
        orderProductListPS.stream().forEach(
                (orderProductPS -> {
                    orderSheetPS.addOrderProduct(orderProductPS);
                    orderProductPS.getProduct().updateQty(orderProductPS.getCount());
                })
        );

        // 6. 응답하기
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(orderSheetPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 유저 주문서 조회
    @GetMapping("/orders")
    public ResponseEntity<?> findByUserId(@MySessionStore SessionUser sessionUser) {
        List<OrderSheet> orderSheetListPS = orderSheetRepository.findByUserId(sessionUser.getId());
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(orderSheetListPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 판매자 주문서 조회
    @GetMapping("/seller/orders")
    public ResponseEntity<?> findBySellerId() {
        // 판매자는 한명이기 때문에 orderProductRepository.findAll() 해도 된다.
        List<OrderSheet> orderSheetListPS = orderSheetRepository.findAll();
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(orderSheetListPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    // 유저 주문 취소
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @MySessionStore SessionUser sessionUser) {
        // 1. 주문서 찾기
        OrderSheet orderSheetPS = orderSheetRepository.findById(id).orElseThrow(
                () -> new Exception400("id", "해당 주문을 찾을 수 없습니다")
        );

        // 2. 해당 주문서의 주인 여부 확인
        if (!orderSheetPS.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403("권한이 없습니다");
        }

        // 3. 재고 변경하기
        orderSheetPS.getOrderProductList().stream().forEach(orderProduct -> {
            orderProduct.getProduct().rollbackQty(orderProduct.getCount());
        });

        // 4. 주문서 삭제하기 (casecade 옵션)
        orderSheetRepository.delete(orderSheetPS);

        // 5. 응답하기
        ResponseDTO<?> responseDTO = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 판매자 주문 취소
    @DeleteMapping("/seller/orders/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id) {
        // 1. 주문서 찾기
        OrderSheet orderSheetPS = orderSheetRepository.findById(id).orElseThrow(
                () -> new Exception400("id", "해당 주문을 찾을 수 없습니다")
        );

        // 2. 재고 변경하기
        orderSheetPS.getOrderProductList().stream().forEach(orderProduct -> {
            orderProduct.getProduct().rollbackQty(orderProduct.getCount());
        });

        // 3. 주문서 삭제하기 (casecade 옵션)
        orderSheetRepository.delete(orderSheetPS);

        // 4. 응답하기
        ResponseDTO<?> responseDTO = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDTO);
    }
}
