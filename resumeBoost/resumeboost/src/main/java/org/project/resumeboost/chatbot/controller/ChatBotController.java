package org.project.resumeboost.chatbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.chatbot.service.KomoranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotController {

  @Autowired
  private KomoranService komoranService;

  @PostMapping("/chatbot")
  public ResponseEntity<?> message(@RequestBody Map<String, String> request) throws Exception {
    String message = request.get("message"); // JSON에서 "message" 값 가져오기

    if (message == null || message.trim().isEmpty()) {
      return ResponseEntity.badRequest().body("메시지가 비어있습니다.");
    }

    Map<String, Object> response = new HashMap<>();

    response.put("message", komoranService.nlpAnalyze(message));

    return ResponseEntity.ok(response);
  }
}
