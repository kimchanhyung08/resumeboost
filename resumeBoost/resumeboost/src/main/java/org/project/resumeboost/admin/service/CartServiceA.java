package org.project.resumeboost.admin.service;

import org.project.resumeboost.cart.dto.CartDto;
import org.project.resumeboost.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartServiceA {
  
  boolean addCart(ItemDto itemDto);

  Page<CartDto> ListAll(Pageable pageable);
  
}
