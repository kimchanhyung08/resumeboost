package org.project.resumeboost.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.project.resumeboost.item.dto.ItemDto;
import org.project.resumeboost.item.service.impl.ItemServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
  private final ItemServiceImpl itemServiceImpl;

  // item 추가
  @PostMapping("/insert")
  public ResponseEntity<?> itemInsert(@RequestBody ItemDto itemDto) {
    System.out.println("price------------>" + itemDto.getItemPrice());
    System.out.println("id------------>" + itemDto.getMemberId());
    System.out.println("category------------>" + itemDto.getCategory());
    itemServiceImpl.itemInsert(itemDto);
    return ResponseEntity.status(HttpStatus.OK).body("save");
  }

  // item 목록
  @GetMapping("/itemList")
  public ResponseEntity<?> itemList(
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam(name = "category", required = false) String category
  // @RequestParam(name = "search",required = false)String search
  ) {

    Page<ItemDto> itemPage = itemServiceImpl.itemList(pageable, category);
    Map<String, Object> map = new HashMap<>();

    int totalPages = itemPage.getTotalPages();
    int currentPage = itemPage.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("itemList", itemPage);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("category", category);
    // map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 내 item 목록
  @GetMapping("/myItemList/{id}")
  public ResponseEntity<?> myItemList(@PathVariable("id") Long id,
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

    Page<ItemDto> itemPage = itemServiceImpl.myItemList(pageable, id);
    Map<String, Object> map = new HashMap<>();

    int totalPages = itemPage.getTotalPages();
    int currentPage = itemPage.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("itemList", itemPage);
    map.put("startPage", startPage);
    map.put("endPage", endPage);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // item 상세정보
  @GetMapping("/itemDetail/{id}")
  public ResponseEntity<?> itemDetail(@PathVariable("id") Long id) {
    ItemDto itemDto = itemServiceImpl.itemDetail(id);
    Map<String, Object> map = new HashMap<>();
    map.put("item", itemDto);
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // item 수정
  @PostMapping("/update")
  public ResponseEntity<?> itemUpdate(@RequestBody ItemDto itemDto) {
    itemServiceImpl.itemUpdate(itemDto);

    return ResponseEntity.status(HttpStatus.OK).body("save");
  }

  // item 삭제
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> itemDelete(@PathVariable("id") Long id) {
    itemServiceImpl.itemDelete(id);

    return ResponseEntity.status(HttpStatus.OK).body("save");
  }
}
