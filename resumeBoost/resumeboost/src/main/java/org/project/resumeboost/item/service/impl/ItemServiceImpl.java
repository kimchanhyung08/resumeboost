package org.project.resumeboost.item.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.project.resumeboost.item.dto.ItemDto;
import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.item.repository.ItemRepository;
import org.project.resumeboost.item.service.ItemService;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final MemberRepository memberRepository;
  private final ItemRepository itemRepository;

  @Override
  public void itemInsert(ItemDto itemDto) {
    MemberEntity memberEntity = memberRepository.findById(itemDto.getMemberId())
        .orElseThrow(IllegalArgumentException::new);

    itemRepository.save(ItemEntity.builder()
        .category(itemDto.getCategory())
        .itemPrice(itemDto.getItemPrice())
        .memberEntity(memberEntity)
        .build());
  }

  @Override
  public Page<ItemDto> itemList(Pageable pageable, String category) {
    Page<ItemEntity> itemEntities = null;

    if (category == null || category == "") {
      itemEntities = itemRepository.findAll(pageable);
    } else {
      if (category.equals("자기소개서")) {
        itemEntities = itemRepository.findByCategoryContaining(pageable, category);
      } else if (category.equals("이력서")) {
        itemEntities = itemRepository.findByCategoryContaining(pageable, category);
      } else if (category.equals("면접컨설팅")) {
        itemEntities = itemRepository.findByCategoryContaining(pageable, category);
      }
    }
    return itemEntities.map(item -> ItemDto.builder()
        .id(item.getId())
        .category(item.getCategory())
        .itemPrice(item.getItemPrice())
        .memberEntity(item.getMemberEntity())
        .memberId(item.getMemberEntity().getId())
        .createTime(item.getCreateTime())
        .updateTime(item.getUpdateTime())
        .build());
  }

  @Override
  public Page<ItemDto> myItemList(Pageable pageable, Long id) {
    MemberEntity memberEntity = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

    Page<ItemEntity> itemEntities = itemRepository.findByMemberEntity(pageable, memberEntity);
    return itemEntities.map(item -> ItemDto.builder()
        .id(item.getId())
        .category(item.getCategory())
        .itemPrice(item.getItemPrice())
        .memberEntity(memberEntity)
        .memberId(memberEntity.getId())
        .createTime(item.getCreateTime())
        .updateTime(item.getUpdateTime())
        .build());
  }

  @Override
  public ItemDto itemDetail(Long id) {
    ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(IllegalArgumentException::new);

    return ItemDto.builder()
        .id(itemEntity.getId())
        .category(itemEntity.getCategory())
        .itemPrice(itemEntity.getItemPrice())
        .memberEntity(itemEntity.getMemberEntity())
        .memberId(itemEntity.getMemberEntity().getId())
        .createTime(itemEntity.getCreateTime())
        .updateTime(itemEntity.getUpdateTime())
        .build();
  }

  @Override
  public void itemUpdate(ItemDto itemDto) {
    MemberEntity memberEntity = memberRepository.findById(itemDto.getMemberId())
        .orElseThrow(IllegalArgumentException::new);
    ItemEntity itemEntity = itemRepository.findById(itemDto.getId()).orElseThrow(IllegalArgumentException::new);

    itemRepository.save(ItemEntity.builder()
        .id(itemDto.getId())
        .category(itemDto.getCategory())
        .itemPrice(itemDto.getItemPrice())
        .memberEntity(memberEntity)
        .build());
  }

  @Override
  public void itemDelete(Long id) {
    ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(IllegalArgumentException::new);

    itemRepository.deleteById(id);
  }

}
