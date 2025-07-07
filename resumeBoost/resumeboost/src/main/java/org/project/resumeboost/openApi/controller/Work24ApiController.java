package org.project.resumeboost.openApi.controller;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

import org.project.resumeboost.openApi.service.impl.Work24ApiServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Work24ApiController {
  private final Work24ApiServiceImpl work24ApiServiceImpl;

  @GetMapping("/work24")
  public ResponseEntity<JsonNode> work24(
      @RequestParam(name = "startPage", defaultValue = "1") int startPage,
      @RequestParam(name = "display", defaultValue = "18") int display) {
    JsonNode response = work24ApiServiceImpl.getWork24Data(startPage, display);
    return ResponseEntity.ok(response);
  }
}
