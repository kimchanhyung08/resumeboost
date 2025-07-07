package org.project.resumeboost.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlovalExceptionHandler {

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<?> IllegalArgumentException(Exception e) {

    String html = e.getMessage();

    return ResponseEntity.status(HttpStatus.OK).body(html);
  }

  @ExceptionHandler(value = NumberFormatException.class)
  public ResponseEntity<?> NumberFormatException(Exception e) {

    String html = "검색어를 입력해주세요.";

    return ResponseEntity.status(HttpStatus.OK).body(html);
  } 
  
}