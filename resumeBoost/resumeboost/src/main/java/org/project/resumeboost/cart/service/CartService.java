package org.project.resumeboost.cart.service;

import org.project.resumeboost.cart.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {
  void addCart(Long memberId, Long id);

  CartDto myCart(Long memberId);

  Page<CartDto> cartList(Pageable pageable);

  void deleteCartItem(Long memberId, Long itemId);

  void deleteAllCartItems(Long memberId);
}
