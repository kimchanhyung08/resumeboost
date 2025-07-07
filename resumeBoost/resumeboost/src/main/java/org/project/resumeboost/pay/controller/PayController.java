package org.project.resumeboost.pay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.project.resumeboost.pay.dto.PayDto;
import org.project.resumeboost.pay.service.impl.PayServiceImpl;
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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {
  private final PayServiceImpl payService;

  @PostMapping("/addPay")
  public ResponseEntity<?> addPay(@RequestBody PayDto payDto) {
    payService.addPay(payDto);
    return ResponseEntity.status(HttpStatus.OK).body("save");
  }

  @GetMapping("/myPay/{id}")
  public ResponseEntity<?> myPay(@PathVariable("id") Long id) {
    List<PayDto> payDtos = payService.myPay(id);
    Map<String, Object> map = new HashMap<>();
    map.put("payList", payDtos);
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/payList")
  public ResponseEntity<?> payList(
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<PayDto> payPage = payService.payList(pageable, subject, search);
    Map<String, Object> map = new HashMap<>();

    int totalPages = payPage.getTotalPages();
    int currentPage = payPage.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("payList", payPage);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

}
