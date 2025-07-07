package org.project.resumeboost.admin.service;

import java.io.IOException;

import org.project.resumeboost.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSeviceA {
  
  Page<BoardDto> ListAll(Pageable pageable, String subject, String search);


  BoardDto boardDetail(Long boardId);

  void boardDelete(Long boardId);

  void boardUpdate(BoardDto boardDto) throws IllegalStateException, IOException;


}
