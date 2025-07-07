package org.project.resumeboost.admin.service;

import java.util.List;

import org.project.resumeboost.pay.dto.PayDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayServiceA {

  void insertPay(PayDto payDto);

  Page<PayDto> payList(Pageable pageable, String subject, String search);

  PayDto payDetail(Long payId);
  
}
