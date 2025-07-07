package org.project.resumeboost.security;

import java.util.Optional;

import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService{
  
  private final MemberRepository memberRepository;
  
  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException { // 시큐리티가 적용하면 -> 사용자 정보 조회 & 인증 권한 처리리
    System.out.println(userEmail);
    Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserEmail(userEmail);

    if (!optionalMemberEntity.isPresent()) {

      throw new NullPointerException("이메일이 존재하지 않습니다!!");
    }

    
    return new CustomMyUserDetails(optionalMemberEntity.get());
  }
  
}
