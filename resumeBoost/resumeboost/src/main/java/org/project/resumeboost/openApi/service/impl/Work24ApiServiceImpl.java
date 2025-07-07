package org.project.resumeboost.openApi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.project.resumeboost.openApi.service.Work24ApiService;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class Work24ApiServiceImpl implements Work24ApiService {

  private static final String BASE_URL = "https://www.work24.go.kr/cm/openApi/call/wk/callOpenApiSvcInfo210L21.do";
  private static final String AUTH_KEY = "";

  private final RestTemplate restTemplate;

  @Override
  public JsonNode getWork24Data(int page, int display) {
    String url = String.format("%s?authKey=%s&callTp=L&returnType=XML&startPage=%d&display=%d",
        BASE_URL, AUTH_KEY, page, display);

    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    String xmlData = response.getBody();

    try {
      // XML을 JSON으로 변환
      XmlMapper xmlMapper = new XmlMapper();
      return xmlMapper.readTree(xmlData);
    } catch (Exception e) {
      throw new RuntimeException("XML to JSON 변환 중 오류 발생", e);
    }
  }
}
