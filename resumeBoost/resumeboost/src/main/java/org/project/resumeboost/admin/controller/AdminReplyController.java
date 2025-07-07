package org.project.resumeboost.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.project.resumeboost.admin.service.impl.ReplyServiceImplA;
import org.project.resumeboost.reply.dto.ReplyDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/reply")
@RequiredArgsConstructor
public class AdminReplyController {

  private final ReplyServiceImplA replyService;

  @GetMapping("/{boardId}")
  public ResponseEntity<?> replys(@PathVariable(name = "boardId") Long boardId) {

    List<ReplyDto> replyDtos = replyService.ListAll(boardId);

    Map<String, List<ReplyDto>> map = new HashMap<>();

    map.put("reply", replyDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }


  @DeleteMapping("/delete/{replyId}")
  public void replyDelete(@PathVariable(name = "replyId") Long replyId) {

    replyService.replyDelete(replyId);

  }


}
