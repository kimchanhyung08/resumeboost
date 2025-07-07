package org.project.resumeboost.pay.service;

import java.util.List;

import org.project.resumeboost.pay.dto.PayDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayService {
  void addPay(PayDto payDto);
  List<PayDto> myPay(Long id);
  Page<PayDto> payList(Pageable pageable,String subject,String search);
}
