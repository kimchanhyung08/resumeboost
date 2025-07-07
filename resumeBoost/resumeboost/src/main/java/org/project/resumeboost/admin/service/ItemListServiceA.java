package org.project.resumeboost.admin.service;

import org.project.resumeboost.itemList.dto.ItemListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemListServiceA {
  Page<ItemListDto> ListAll(Pageable pageable, Long id);
}
