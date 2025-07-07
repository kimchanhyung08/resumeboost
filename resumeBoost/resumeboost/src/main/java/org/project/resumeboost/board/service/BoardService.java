package org.project.resumeboost.board.service;

import java.io.IOException;

import org.project.resumeboost.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
  public void boardInsert(BoardDto boardDto) throws IOException;

  public void boardUpdate(BoardDto boardDto) throws IOException;

  public Page<BoardDto> boardList(Pageable pageable, String subject, String search);

  public Page<BoardDto> boardListLetter(Pageable pageable, String subject, String search);

  public Page<BoardDto> boardListResume(Pageable pageable, String subject, String search);

  public Page<BoardDto> boardListInterview(Pageable pageable, String subject, String search);

  public Page<BoardDto> boardListFreedom(Pageable pageable, String subject, String search);

  public void boardDelete(Long boardId);

  public void BoardViewCount(Long id);

  public BoardDto boardDetail(Long boardId);

  Page<BoardDto> boardMyList(Pageable pageable, Long memberId);
}
