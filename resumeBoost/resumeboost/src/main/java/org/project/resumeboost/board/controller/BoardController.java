package org.project.resumeboost.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.service.impl.BoardServiceImpl;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private final BoardServiceImpl boardServiceImpl;

  @PostMapping("/insert")
  public ResponseEntity<?> boardInsert(BoardDto boardDto) throws IOException {

    Map<String, String> map = new HashMap<>();

    boardServiceImpl.boardInsert(boardDto);

    map.put("boardInsert", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @PutMapping("/update")
  public ResponseEntity<?> boardUpdate(BoardDto boardDto) throws IOException {

    Map<String, String> map = new HashMap<>();

    boardServiceImpl.boardUpdate(boardDto);

    map.put("boardUpdate", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/boardList")
  public ResponseEntity<?> boardList(
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<BoardDto> pagingList = boardServiceImpl.boardList(pageable, subject, search);

    Map<String, Object> map = new HashMap<>();

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("boardList", pagingList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/boardList/letter")
  public ResponseEntity<?> boardListLetter(
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<BoardDto> pagingList = boardServiceImpl.boardListLetter(pageable, subject, search);

    Map<String, Object> map = new HashMap<>();

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("boardList", pagingList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/boardList/resume")
  public ResponseEntity<?> boardListResume(
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<BoardDto> pagingList = boardServiceImpl.boardListResume(pageable, subject, search);

    Map<String, Object> map = new HashMap<>();

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("boardList", pagingList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/boardList/interview")
  public ResponseEntity<?> boardListInterview(
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<BoardDto> pagingList = boardServiceImpl.boardListInterview(pageable, subject, search);

    Map<String, Object> map = new HashMap<>();

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("boardList", pagingList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/boardList/freedom")
  public ResponseEntity<?> boardListFreedom(
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(name = "subject", required = false) String subject,
      @RequestParam(name = "search", required = false) String search) {

    Page<BoardDto> pagingList = boardServiceImpl.boardListFreedom(pageable, subject, search);

    Map<String, Object> map = new HashMap<>();

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("boardList", pagingList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("subject", subject);
    map.put("search", search);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/boardList/my/{id}")
  public ResponseEntity<?> boardListMy(
      @PathVariable("id") Long memberId,
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<BoardDto> pagingList = boardServiceImpl.boardMyList(pageable, memberId);

    Map<String, Object> map = new HashMap<>();

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    map.put("boardList", pagingList);
    map.put("startPage", startPage);
    map.put("endPage", endPage);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> boardDelete(@PathVariable("id") Long id) {

    Map<String, String> map = new HashMap<>();

    boardServiceImpl.boardDelete(id);

    map.put("boardDelete", "성공");

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<?> getMethodName(@PathVariable("id") Long id) {

    Map<String, Object> map = new HashMap<>();

    BoardDto boardDto = boardServiceImpl.boardDetail(id);

    map.put("boardDetail", boardDto);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

}
