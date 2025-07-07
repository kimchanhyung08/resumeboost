package org.project.resumeboost.admin.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.admin.service.impl.BoardServiceImplA;
import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.service.impl.BoardServiceImpl;
import org.project.resumeboost.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardContollrer {
  
  private final BoardServiceImplA boardServiceA;


  @GetMapping("")
  public ResponseEntity<?> boardList(
    @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
    @RequestParam(name = "subject", required = false) String subject,
    @RequestParam(name = "search", required = false) String search 
  ) {

    Page<BoardDto> boardList = boardServiceA.ListAll(pageable, subject, search);

    Map<String, Page<BoardDto>> map = new HashMap<>();

    map.put("board", boardList);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/detail/{boardId}")
  public ResponseEntity<?> boardDetail(@PathVariable("boardId") Long boardId) {

    BoardDto boardDto = boardServiceA.boardDetail(boardId);

    Map<String, BoardDto> map = new HashMap<>();

    map.put("board", boardDto);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @DeleteMapping("/delete/{boardId}")
  public void boardDelete(@PathVariable("boardId") Long boardId) {

    boardServiceA.boardDelete(boardId);
  }

  @PutMapping("/update")
  public void boardUpdate(@ModelAttribute BoardDto boardDto) throws IllegalStateException, IOException {

    boardServiceA.boardUpdate(boardDto);
  }  


}
