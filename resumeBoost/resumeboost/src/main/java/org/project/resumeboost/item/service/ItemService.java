package org.project.resumeboost.item.service;

import java.util.List;

import org.project.resumeboost.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {
  void itemInsert(ItemDto itemDto);

  Page<ItemDto> itemList(Pageable pageable, String category);

  Page<ItemDto> myItemList(Pageable pageable, Long id);

  ItemDto itemDetail(Long id);

  void itemUpdate(ItemDto itemDto);

  void itemDelete(Long id);
}
