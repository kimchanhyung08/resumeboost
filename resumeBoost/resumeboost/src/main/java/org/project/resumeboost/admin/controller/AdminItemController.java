package org.project.resumeboost.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.project.resumeboost.admin.service.ItemServiceA;
import org.project.resumeboost.admin.service.impl.ItemServiceImplA;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/item")
@RequiredArgsConstructor
public class AdminItemController {

  private final ItemServiceImplA itemService;  

  @PostMapping("/insert")
  public void insert(@RequestBody ItemDto itemDto) {

    System.out.println(">>>>>>>>" +  itemDto.getMemberId());

    itemService.itemInsert(itemDto);

  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable(name = "id") Long id) {

    itemService.itemDelete(id);

  }

  @GetMapping("")
  public ResponseEntity<?> itemList(@PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {


    Page<ItemDto> itemDtos = itemService.ListAll(pageable);

    Map<String, Page<ItemDto>> map = new HashMap<>();

    map.put("item", itemDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }


  @GetMapping("/itemList")
  public ResponseEntity<?> itemListNoPaging() {


    List<ItemDto> itemDtos = itemService.itemListAll();

    Map<String, List<ItemDto>> map = new HashMap<>();

    map.put("item", itemDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }





  @GetMapping("/detail/{id}")
  public ResponseEntity<?> itemDetail(@PathVariable(name = "id") Long id) {

    System.out.println(">>>>id:>>>" + id);

    ItemDto itemDto = itemService.itemDetail(id);

    Map<String, ItemDto> map = new HashMap<>();

    map.put("item", itemDto);


    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PutMapping("/update")
  public void itemUpdate(@RequestBody ItemDto itemDto) {

    System.out.println(">>>>>>PRICE>>>>>" + itemDto.getItemPrice()); 



    itemService.itemUpdate(itemDto);

  }



  @GetMapping("/details/{mentorId}")
  public ResponseEntity<?> itemDetails(@PathVariable(name = "mentorId") Long mentorId) {


    List<ItemDto> itemDtos = itemService.itemDetails(mentorId);

    Map<String, List<ItemDto>> map = new HashMap<>();

    map.put("item", itemDtos);


    return ResponseEntity.status(HttpStatus.OK).body(map);
  }



}
