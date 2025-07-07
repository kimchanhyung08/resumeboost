package org.project.resumeboost.admin.service.impl;

import org.project.resumeboost.admin.service.ItemListServiceA;
import org.project.resumeboost.itemList.dto.ItemListDto;
import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.itemList.repository.ItemListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemListServiceImplA implements ItemListServiceA {

  private final ItemListRepository itemListRepository;

  @Override
  public Page<ItemListDto> ListAll(Pageable pageable, Long id) {

    Page<ItemListEntity> itemsList = itemListRepository.findByCartEntityId(pageable, id);

    return itemsList.map(el -> ItemListDto.builder()
        .id(el.getId())
        .cartEntity(el.getCartEntity())
        .itemEntity(el.getItemEntity())
        .build());
  }

}
