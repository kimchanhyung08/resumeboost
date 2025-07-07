package org.project.resumeboost.admin.service;

import java.util.List;

import org.project.resumeboost.reply.dto.ReplyDto;

public interface ReplyServiceA {
  List<ReplyDto> ListAll(Long boardId);
  void replyDelete(Long replyId);
}
