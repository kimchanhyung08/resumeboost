package org.project.resumeboost.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.admin.service.ItemListServiceA;
import org.project.resumeboost.admin.service.impl.CartServiceImplA;
import org.project.resumeboost.admin.service.impl.ItemListServiceImplA;
import org.project.resumeboost.cart.dto.CartDto;
import org.project.resumeboost.item.dto.ItemDto;
import org.project.resumeboost.itemList.dto.ItemListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/cart")
@RequiredArgsConstructor
public class AdminCartController {
  
  private final CartServiceImplA cartService;
  private final ItemListServiceImplA itemListService;

  @PostMapping("/addCart")
  public boolean addCart(@RequestBody ItemDto itemDto) {
    
    boolean result = cartService.addCart(itemDto);
    System.out.println(">>>result>>>>" + result);
    return result; // 이미 담겨 있으면 true // 아니면 false
  }

  @GetMapping("")
  public ResponseEntity<?> cartList(@PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<CartDto> cartDtos = cartService.ListAll(pageable);

    Map<String, Page<CartDto>> map = new HashMap<>();

    map.put("cart", cartDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);



  }


  
  @GetMapping("/itemsList/{id}")
  public ResponseEntity<?> itemsList(
    @PathVariable(name = "id") Long id, 
    @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<ItemListDto> itemListDtos = itemListService.ListAll(pageable, id);
    
    Map<String, Page<ItemListDto>> map = new HashMap<>();

    map.put("itemsList", itemListDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);

  }


  


}
