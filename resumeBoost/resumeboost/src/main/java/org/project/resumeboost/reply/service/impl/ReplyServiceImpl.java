package org.project.resumeboost.reply.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.repository.BoardRepository;
import org.project.resumeboost.member.repository.MemberRepository;
import org.project.resumeboost.reply.dto.ReplyDto;
import org.project.resumeboost.reply.entity.ReplyEntity;
import org.project.resumeboost.reply.repository.ReplyRepository;
import org.project.resumeboost.reply.service.ReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
  private final ReplyRepository replyRepository;
  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;

  public void replyCountFn(Long boardId) {
    boardRepository.replyCountFn(boardId);
  }

  public void replyCountDeleteFn(Long boardId) {
    boardRepository.replyCountDeleteFn(boardId);
  }

  public void myReplyCount(Long memberId) {
    memberRepository.myReplyCount(memberId);
  }

  @Override
  public Page<ReplyDto> boardReply(Long id, Pageable pageable) {
    Page<ReplyEntity> replyEntities = null;

    replyEntities = replyRepository.findAllByBoardEntity_Id(id, pageable);

    return replyEntities.map(ReplyDto::toReplyDto);
  }

  @Override
  public void replyInsert(ReplyDto replyDto) {
    Long memberId = memberRepository.findById(replyDto.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")).getId();
    myReplyCount(memberId);
    Long boardId = boardRepository.findById(replyDto.getBoardId())
        .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다.")).getId();
    replyCountFn(boardId);

    replyRepository.save(ReplyEntity.toInsertReplyEntity(replyDto));
  }

  public void myReplyCountDelete(Long memberId) {
    memberRepository.myReplyCountDelete(memberId);
  }

  public void boardReplyCountDelete(Long boardId) {
    boardRepository.replyCountDeleteFn(boardId);
  }

  @Override
  public void replyDelete(Long replyId) {

    ReplyEntity reply = replyRepository.findById(replyId)
        .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

    Long memberId = reply.getMemberEntity().getId();
    Long boardId = reply.getBoardEntity().getId();

    myReplyCountDelete(memberId);
    boardReplyCountDelete(boardId);

    replyRepository.deleteById(replyId);
  }

  @Override
  public Page<ReplyDto> replyMyList(Pageable pageable, Long memberId) {
    Page<ReplyEntity> replyEntities = replyRepository.findAllByMemberEntityId(memberId, pageable);
    return replyEntities.map(reply -> ReplyDto.builder()
        .boardEntity(reply.getBoardEntity())
        .boardId(reply.getBoardEntity().getId())
        .content(reply.getContent())
        .createTime(reply.getCreateTime())
        .id(reply.getId())
        .memberEntity(reply.getMemberEntity())
        .memberId(reply.getMemberEntity().getId())
        .build());
  }

  @Override
  public List<ReplyDto> replyList() {
    List<ReplyEntity> replyEntities = replyRepository.findAll();

    if (replyEntities.isEmpty()) {
      throw new IllegalArgumentException();
    }

    return replyEntities.stream().map(el -> ReplyDto.builder()
        .id(el.getId())
        .memberEntity(el.getMemberEntity())
        .content(el.getContent())
        .boardId(el.getBoardEntity().getId())
        .build()).collect(Collectors.toList());
  }
}
