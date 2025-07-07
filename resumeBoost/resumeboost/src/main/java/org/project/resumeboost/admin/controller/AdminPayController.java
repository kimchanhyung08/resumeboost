package org.project.resumeboost.admin.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.project.resumeboost.admin.service.impl.PayServiceImplA;
import org.project.resumeboost.pay.dto.PayDto;
import org.project.resumeboost.pay.entity.PayEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/admin/pay")
@RequiredArgsConstructor
public class AdminPayController {
  
  private final PayServiceImplA payService;

  @PostMapping("/insert")
  public void postMethodName(@RequestBody PayDto payDto) {
    
    payService.insertPay(payDto);
    
  }

  @GetMapping("")
  public ResponseEntity<?> payList(@PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
    @RequestParam(name = "subject", required = false) String subject,
    @RequestParam(name = "search", required = false) String search) {

    Page<PayDto> payDtos = payService.payList(pageable, subject, search);

    Map<String, Page<PayDto>> map = new HashMap<>();

    map.put("pay", payDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/detail/{payId}")
  public ResponseEntity<?> payDetail(@PathVariable(name = "payId") Long payId) {

    PayDto payDto = payService.payDetail(payId);

    Map<String, PayDto> map = new HashMap<>();

    map.put("pay", payDto);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }
  

}
