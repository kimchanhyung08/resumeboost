package org.project.resumeboost.reply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.service.impl.BoardServiceImpl;
import org.project.resumeboost.reply.dto.ReplyDto;
import org.project.resumeboost.reply.service.impl.ReplyServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyServiceImpl replyServiceImpl;
  private final BoardServiceImpl boardServiceImpl;

  @PostMapping("/insert")
  public ResponseEntity<?> replyInsert(@RequestBody ReplyDto replyDto) {
    Map<String, String> map = new HashMap<>();
    replyServiceImpl.replyInsert(replyDto);
    map.put("reply", "성공");
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/replyList/{id}")
  public ResponseEntity<?> boardReplyList(@PathVariable("id") Long id,
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<ReplyDto> pagingList = replyServiceImpl.boardReply(id, pageable);

    int totalPages = pagingList.getTotalPages(); // 총 페이지수
    int currentPage = pagingList.getPageable().getPageNumber();
    int block = 5;

    int startPage = (int) ((Math.floor(currentPage / block) * block) + 1 <= totalPages
        ? (Math.floor(currentPage / block) * block) + 1
        : totalPages);
    int endPage = (startPage + block) - 1 < totalPages ? (startPage + block) - 1 : totalPages;

    Map<String, Object> map = new HashMap<>();

    map.put("content", pagingList.getContent());
    map.put("currentPage", currentPage);
    map.put("totalPages", totalPages);
    map.put("startPage", startPage);
    map.put("endPage", endPage);
    map.put("totalElements", pagingList.getTotalElements());

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/replyList/my/{id}")
  public ResponseEntity<?> replyListMy(
      @PathVariable("id") Long memberId,
      @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<ReplyDto> pagingList = replyServiceImpl.replyMyList(pageable, memberId);

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
  public ResponseEntity<?> replyDelete(@PathVariable("id") Long replyId) {
    Map<String, String> map = new HashMap<>();
    replyServiceImpl.replyDelete(replyId);
    map.put("replyDelete", "성공");
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/replyList")
  public ResponseEntity<?> replyList() {
    Map<String, List<ReplyDto>> map = new HashMap<>();

    List<ReplyDto> replyDtos = replyServiceImpl.replyList();

    map.put("replyList", replyDtos);
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

}
