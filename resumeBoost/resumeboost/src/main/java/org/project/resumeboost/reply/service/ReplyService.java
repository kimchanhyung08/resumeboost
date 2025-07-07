package org.project.resumeboost.reply.service;

import java.util.List;

import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.reply.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyService {
  Page<ReplyDto> boardReply(Long id, Pageable pageable);

  void replyInsert(ReplyDto replyDto);

  void replyDelete(Long replyId);

  Page<ReplyDto> replyMyList(Pageable pageable, Long memberId);

  List<ReplyDto> replyList();
}
