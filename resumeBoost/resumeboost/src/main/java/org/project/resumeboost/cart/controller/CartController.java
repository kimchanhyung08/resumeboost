package org.project.resumeboost.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.cart.dto.CartDto;
import org.project.resumeboost.cart.service.impl.CartServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
  private final CartServiceImpl cartServiceImpl;

  // 장바구니 추가
  @PostMapping("/addCart/memberId/{memberId}/id/{id}")
  public ResponseEntity<?> addCart(@PathVariable("memberId") Long memberId, @PathVariable("id") Long id) {
    cartServiceImpl.addCart(memberId, id);
    return ResponseEntity.status(HttpStatus.OK).body("save");
  }

  // 내 장바구니 정보
  @GetMapping("myCart/{memberId}")
  public ResponseEntity<?> myCart(@PathVariable("memberId") Long memberId) {
    CartDto cartDto = cartServiceImpl.myCart(memberId);
    Map<String, Object> map = new HashMap<>();
    map.put("cart", cartDto);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 장바구니 리스트
  @GetMapping("/cartList")
  public ResponseEntity<?> cartList(
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
    Page<CartDto> cartPage = cartServiceImpl.cartList(pageable);
    Map<String, Object> map = new HashMap<>();

    int totalPages = cartPage.getTotalPages();
    int currentPage = cartPage.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("cartList", cartPage);
    map.put("startPage", startPage);
    map.put("endPage", endPage);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/deleteCartItem/memberId/{memberId}/itemId/{itemId}")
  public ResponseEntity<?> deleteCartItem(@PathVariable("memberId") Long memberId,
      @PathVariable("itemId") Long itemId) {
    cartServiceImpl.deleteCartItem(memberId, itemId);
    return ResponseEntity.status(HttpStatus.OK).body("deleteSuccess");
  }

  @GetMapping("deleteAllCartItems/{memberId}")
  public ResponseEntity<?> deleteAllCartItems(@PathVariable("memberId") Long memberId) {
    cartServiceImpl.deleteAllCartItems(memberId);
    return ResponseEntity.status(HttpStatus.OK).body("DeleteSuccess");
  }
}
