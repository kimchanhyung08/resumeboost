package org.project.resumeboost.admin.service;

import java.util.List;

import org.project.resumeboost.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemServiceA {

  void itemInsert(ItemDto itemDto);

  void itemDelete(Long id);

  Page<ItemDto> ListAll(Pageable pageable);

  ItemDto itemDetail(Long id);

  void itemUpdate(ItemDto itemDto);

  List<ItemDto> itemDetails(Long mentorId);

  List<ItemDto> itemListAll();

}
