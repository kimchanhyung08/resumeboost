package org.project.resumeboost.cart.service.impl;

import java.util.List;
import java.util.Optional;

import org.project.resumeboost.cart.dto.CartDto;
import org.project.resumeboost.cart.entity.CartEntity;
import org.project.resumeboost.cart.repository.CartRepository;
import org.project.resumeboost.cart.service.CartService;
import org.project.resumeboost.item.entity.ItemEntity;
import org.project.resumeboost.item.repository.ItemRepository;
import org.project.resumeboost.itemList.entity.ItemListEntity;
import org.project.resumeboost.itemList.repository.ItemListRepository;
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
public class CartServiceImpl implements CartService {
  private final MemberRepository memberRepository;
  private final ItemRepository itemRepository;
  private final ItemListRepository itemListRepository;
  private final CartRepository cartRepository;

  @Override
  public void addCart(Long memberId, Long id) {
    MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(IllegalArgumentException::new);

    Optional<CartEntity> optionalCartEntity = cartRepository.findByMemberEntity(memberEntity);
    CartEntity cartEntity = null;

    if (!optionalCartEntity.isPresent()) {
      cartEntity = cartRepository.save(CartEntity.builder().memberEntity(memberEntity).build());
    } else {
      cartEntity = optionalCartEntity.get();
    }

    List<ItemListEntity> itemListEntities = itemListRepository.findByCartEntityAndItemEntity(cartEntity, itemEntity);

    if (itemListEntities.isEmpty()) {
      itemListRepository.save(ItemListEntity.builder()
          .itemEntity(itemEntity)
          .cartEntity(cartEntity)
          .build());
    }
  }

  @Override
  public CartDto myCart(Long memberId) {
    MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    CartEntity cartEntity = cartRepository.findByMemberEntity(memberEntity).orElseThrow(IllegalArgumentException::new);

    return CartDto.builder()
        .id(cartEntity.getId())
        .memberEntity(memberEntity)
        .memberId(memberId)
        .itemListEntities(cartEntity.getItemListEntities())
        .createTime(cartEntity.getCreateTime())
        .updateTime(cartEntity.getUpdateTime())
        .build();
  }

  @Override
  public void deleteCartItem(Long memberId, Long itemId) {
    MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    CartEntity cartEntity = cartRepository.findByMemberEntity(memberEntity).orElseThrow(IllegalArgumentException::new);
    ItemEntity itemEntity = itemRepository.findById(itemId).orElseThrow(IllegalArgumentException::new);

    List<ItemListEntity> itemListEntities = itemListRepository.findByCartEntityAndItemEntity(cartEntity, itemEntity);

    if (!itemListEntities.isEmpty()) {
      itemListRepository.deleteAllByCartEntityAndItemEntity(cartEntity, itemEntity);
    } else {
      throw new IllegalArgumentException("Item not found in the cart for member: " + memberId);
    }
  }

  @Override
  public void deleteAllCartItems(Long memberId) {
    MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    CartEntity cartEntity = cartRepository.findByMemberEntity(memberEntity).orElseThrow(IllegalArgumentException::new);

    List<ItemListEntity> itemListEntities = itemListRepository.findAllByCartEntityId(cartEntity.getId());

    if (!itemListEntities.isEmpty()) {
      itemListRepository.deleteAll(itemListEntities);
    }
  }

  @Override
  public Page<CartDto> cartList(Pageable pageable) {
    Page<CartEntity> cartEntities = cartRepository.findAll(pageable);
    return cartEntities.map(cart -> CartDto.builder()
        .id(cart.getId())
        .memberEntity(cart.getMemberEntity())
        .memberId(cart.getMemberEntity().getId())
        .itemListEntities(cart.getItemListEntities())
        .createTime(cart.getCreateTime())
        .updateTime(cart.getUpdateTime())
        .build());
  }
}
