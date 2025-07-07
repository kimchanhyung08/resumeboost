package org.project.resumeboost.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.project.resumeboost.admin.service.ItemServiceA;
import org.project.resumeboost.item.dto.ItemDto;
import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.item.repository.ItemRepository;
import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImplA implements ItemServiceA {

  private final ItemRepository itemRepository;

  @Override
  public void itemInsert(ItemDto itemDto) {

    ItemEntity itemEntity = ItemEntity.builder()
        .category(itemDto.getCategory())
        .itemPrice(itemDto.getItemPrice())
        .memberEntity(MemberEntity.builder().id(itemDto.getMemberId()).build())
        .build();

    itemRepository.save(itemEntity);

  }

  @Override
  public void itemDelete(Long id) {

    itemRepository.deleteById(id);

  }

  @Override
  public Page<ItemDto> ListAll(Pageable pageable) {

    Page<ItemEntity> itemEntities = itemRepository.findAll(pageable);

    return itemEntities.map(el -> ItemDto.builder()
        .id(el.getId())
        .category(el.getCategory())
        .itemPrice(el.getItemPrice())
        .memberId(el.getMemberEntity().getId())
        .memberEntity(el.getMemberEntity())
        .createTime(el.getCreateTime())
        .updateTime(el.getUpdateTime())
        .build());
  }

  @Override
  public ItemDto itemDetail(Long id) {

    ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(() -> new NullPointerException("수정할 상품이 없습니다!!"));

    return ItemDto.builder()
        .id(itemEntity.getId())
        .itemPrice(itemEntity.getItemPrice())
        .category(itemEntity.getCategory())
        .memberId(itemEntity.getMemberEntity().getId())
        .memberEntity(itemEntity.getMemberEntity())
        .createTime(itemEntity.getCreateTime())
        .updateTime(itemEntity.getUpdateTime())
        .build();

  }

  @Override
  public void itemUpdate(ItemDto itemDto) {

    Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemDto.getId());
    if (!optionalItemEntity.isPresent()) {
      throw new NullPointerException("상품이 존재하지 않습니다!");
    }

    ItemEntity itemEntity = ItemEntity.builder()
        .id(itemDto.getId())
        .category(itemDto.getCategory())
        .itemPrice(itemDto.getItemPrice())
        .memberEntity(MemberEntity.builder().id(itemDto.getMemberId()).build())
        .build();

    itemRepository.save(itemEntity);

  }

  @Override
  public List<ItemDto> itemDetails(Long mentorId) {

    List<ItemEntity> itemEntities = itemRepository.findByMemberEntityId(mentorId);

    return itemEntities.stream().map(el -> ItemDto.builder()
        .id(el.getId())
        .category(el.getCategory())
        .itemPrice(el.getItemPrice())
        .memberId(el.getMemberEntity().getId())
        .memberEntity(el.getMemberEntity())
        .createTime(el.getCreateTime())
        .updateTime(el.getUpdateTime())
        .build()).collect(Collectors.toList());

  }

  @Override
  public List<ItemDto> itemListAll() {

    List<ItemEntity> itemEntities = itemRepository.findAll();

    return itemEntities.stream().map(el -> ItemDto.builder()
        .id(el.getId())
        .category(el.getCategory())
        .itemPrice(el.getItemPrice())
        .memberId(el.getMemberEntity().getId())
        .memberEntity(el.getMemberEntity())
        .createTime(el.getCreateTime())
        .updateTime(el.getUpdateTime())
        .build()).collect(Collectors.toList());
  }

}
