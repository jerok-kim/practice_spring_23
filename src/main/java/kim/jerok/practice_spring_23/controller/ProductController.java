package kim.jerok.practice_spring_23.controller;

import kim.jerok.practice_spring_23.core.annotation.MySessionStore;
import kim.jerok.practice_spring_23.core.exception.Exception400;
import kim.jerok.practice_spring_23.core.session.SessionUser;
import kim.jerok.practice_spring_23.dto.ResponseDTO;
import kim.jerok.practice_spring_23.dto.product.ProductRequest;
import kim.jerok.practice_spring_23.model.product.Product;
import kim.jerok.practice_spring_23.model.product.ProductRepository;
import kim.jerok.practice_spring_23.model.user.User;
import kim.jerok.practice_spring_23.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 상품 등록, 상품목록보기, 상품상세보기, 상품수정하기, 상품삭제하기
 */
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HttpSession session;

    @PostMapping("/seller/products")
    public ResponseEntity<?> save(@RequestBody @Valid ProductRequest.SaveDTO saveDTO, Errors errors, @MySessionStore SessionUser sessionUser) {
        // 1. 판매자 찾기
        User sellerPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(
                        () -> new Exception400("id", "판매자를 찾을 수 없습니다")
                );

        // 2. 상품 등록하기
        Product productPS = productRepository.save(saveDTO.toEntity(sellerPS));
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/products")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        // 1. 상품 찾기
        Page<Product> productPagePS = productRepository.findAll(pageable);

        // 2. 응답하기
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPagePS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        // 1. 상품 찾기
        Product productPS = productRepository.findById(id).orElseThrow(() -> new Exception400("id", "해당 상품을 찾을 수 없습니다"));

        // 2. 응답하기
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Transactional  // 더티체킹 하고 싶다면 붙이기!!
    @PutMapping("/seller/products/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ProductRequest.UpdateDTO updateDTO, Errors errors) {
        // 1. 상품 찾기
        Product productPS = productRepository.findById(id).orElseThrow(() -> new Exception400("id", "해당 상품을 찾을 수 없습니다"));

        // 2. Update 더티체킹
        productPS.update(updateDTO.getName(), updateDTO.getPrice(), updateDTO.getQty());

        // 3. 응답하기
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/seller/products/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Product productPS = productRepository.findById(id).orElseThrow(() -> new Exception400("id", "해당 상품을 찾을 수 없습니다"));
        productRepository.delete(productPS);
        ResponseDTO<?> responseDTO = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDTO);
    }
}
