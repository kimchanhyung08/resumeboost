package org.project.resumeboost.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.project.resumeboost.admin.service.impl.MemberServiceImplA;
import org.project.resumeboost.member.dto.MemberDto;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final MemberServiceImplA memberServiceA;
  


  @GetMapping("/member")
  public ResponseEntity<?> memberList(@PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
    @RequestParam(name = "subject", required = false) String subject,
    @RequestParam(name = "search", required = false) String search
  ) {
    
    Page<MemberDto> memberList = memberServiceA.ListAll(pageable, subject, search);
    
    Map<String, Page<MemberDto>> map = new HashMap<>();

    map.put("member", memberList);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/member/detail/{id}")
  public ResponseEntity<?> memberDetail(@PathVariable("id") Long id) {

    MemberDto member = memberServiceA.memberDetail(id);

    Map<String, Object> map = new HashMap<>();

    map.put("member", member);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }
  
  @DeleteMapping("/member/delete/{id}")
  public ResponseEntity<?> memberDelete(@PathVariable("id") Long id) {

    memberServiceA.memberDelete(id);

    return ResponseEntity.status(HttpStatus.OK).body(true);
  }

  // @PutMapping("/member/update")
  // public void memberUpdate(@RequestBody MemberDto memberDto) throws IllegalStateException, IOException {

    
  //   memberServiceA.memberUpdate(memberDto);

    
  // }

  @PutMapping("/member/update")
  public void memberUpdate(@ModelAttribute MemberDto memberDto) throws IllegalStateException, IOException {

    
    memberServiceA.memberUpdate(memberDto);

    
  }



  @GetMapping("/member/mentorList")
  public ResponseEntity<?> mentorList(@PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<MemberDto> mentorList = memberServiceA.mentorListAll(pageable);
   
    Map<String, Page<MemberDto>> map = new HashMap<>();

    map.put("mentor", mentorList);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }






}
