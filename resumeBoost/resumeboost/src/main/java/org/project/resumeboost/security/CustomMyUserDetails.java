package org.project.resumeboost.security;

import java.util.ArrayList;
import java.util.Collection;

import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomMyUserDetails implements UserDetails { // 사용자의 정보를 담는 인터페이스

  private MemberEntity memberEntity;

  public CustomMyUserDetails(MemberEntity memberEntity) {
    this.memberEntity = memberEntity; // User (전역 객체) -> memberEntity 담아서 전역으로 사용 -> principal
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collectRoles = new ArrayList<>();

    collectRoles.add(new GrantedAuthority() {

      @Override
      public String getAuthority() {

        return "ROLE_" + memberEntity.getRole().toString();
      }

    });

    return collectRoles;
  }

  @Override
  public String getPassword() {

    return memberEntity.getUserPw();
  }

  public String getMemberId() {

    return memberEntity.getId().toString();
  }

  @Override
  public String getUsername() {

    return memberEntity.getUserEmail();
  }

  public String getNickName() {
    return memberEntity.getNickName(); // 직접 커스텀도 가능
  }

  public boolean isSocial() {
    return memberEntity.getSocial();
  }

  @Override
  public boolean isAccountNonExpired() {
    // 계정 만료 여부 // true : 만료 X
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // 계정 잠김 여부 // true : 잠김 x
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // 비밀번호 만료 여부 // true : 만료료 X
    return true;
  }

  @Override
  public boolean isEnabled() {
    // 사용자 활성화 여부 // true : 활성화
    return true;
  }

}
