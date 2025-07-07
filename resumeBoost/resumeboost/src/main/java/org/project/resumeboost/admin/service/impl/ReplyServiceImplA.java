package org.project.resumeboost.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.project.resumeboost.admin.service.ReplyServiceA;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.board.repository.BoardRepository;
import org.project.resumeboost.reply.dto.ReplyDto;
import org.project.resumeboost.reply.entity.ReplyEntity;
import org.project.resumeboost.reply.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImplA implements ReplyServiceA {

  private final ReplyRepository replyRepository;
  private final BoardRepository boardRepository;


  @Override
  public List<ReplyDto> ListAll(Long boardId) {
    
    List<ReplyEntity> replyEntities = replyRepository.findAllByBoardEntityId(boardId);

    return replyEntities.stream().map(el -> ReplyDto.toReplyDto(el)).collect(Collectors.toList());
    
  }

  @Override
  public void replyDelete(Long replyId) {
    
    Optional<ReplyEntity> optionalReplyEntity = replyRepository.findById(replyId);

    if (!optionalReplyEntity.isPresent()) {
      throw new NullPointerException("삭제할 덧글이 존재하지 않습니다!!");
    }


    BoardEntity boardEntity = optionalReplyEntity.get().getBoardEntity();
    boardEntity.setReplyCount(boardEntity.getReplyCount() - 1); // 특정 필드만 수정하고 싶을 때 -> 그 값만 변경
    boardRepository.save(boardEntity);

    replyRepository.deleteById(replyId);

  }
  
}
