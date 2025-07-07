package org.project.resumeboost.admin.service.impl;

import java.util.List;
import java.util.Optional;

import javax.swing.text.Caret;

import org.project.resumeboost.admin.service.CartServiceA;
import org.project.resumeboost.cart.dto.CartDto;
import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.cart.repository.CartRepository;
import org.project.resumeboost.item.dto.ItemDto;
import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.item.repository.ItemRepository;
import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.itemList.repository.ItemListRepository;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImplA implements CartServiceA {

  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final ItemRepository itemRepository;
  private final ItemListRepository itemListRepository;

  @Override
  public boolean addCart(ItemDto itemDto) {
    
    MemberEntity memberEntity = memberRepository.findById(itemDto.getMemberId()).orElseThrow(() -> new NullPointerException("회원이 존재하지 않습니다!!"));

    Optional<CartEntity> optionalCartEntity = cartRepository.findByMemberEntityId(memberEntity.getId());


    CartEntity cartEntity = null;

    if (!optionalCartEntity.isPresent()) { // 장바구니가 없다면 -> 생성
      cartEntity = cartRepository.save(CartEntity.builder()
      .memberEntity(memberEntity)
      .build());
    } else {
      cartEntity = optionalCartEntity.get(); // 있으면 그대로
    }

    
    ItemEntity itemEntity = 
    itemRepository.findById(itemDto.getId()).orElseThrow(() -> new NullPointerException("상품이 존재하지 않습니다!"));
    
    
    List<ItemListEntity> itemListEntities = 
      itemListRepository.findByCartEntityIdAndItemEntityId(cartEntity.getId(), itemEntity.getId());

    
    if (itemListEntities.isEmpty()) { 
      ItemListEntity itemListEntity = ItemListEntity.builder()
      .itemEntity(itemEntity)
      .cartEntity(cartEntity)
      .build();

      itemListRepository.save(itemListEntity);

      return false;

    } else {

      return true;
      
    }



  }

  @Override
  public Page<CartDto> ListAll(Pageable pageable) {
    
    Page<CartEntity> cartEntities = cartRepository.findAll(pageable);

  
    return cartEntities.map(el -> CartDto.builder()
    .id(el.getId())
    .memberId(el.getMemberEntity().getId())
    .memberEntity(el.getMemberEntity())
    .itemListEntities(el.getItemListEntities())
    .createTime(el.getCreateTime())
    .updateTime(el.getUpdateTime())
    .build());
  }
  
}
