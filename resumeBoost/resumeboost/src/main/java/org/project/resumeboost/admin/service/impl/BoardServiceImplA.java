package org.project.resumeboost.admin.service.impl;


import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.project.resumeboost.admin.service.BoardSeviceA;
import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.board.entity.BoardImgEntity;
import org.project.resumeboost.board.repository.BoardImgRepository;
import org.project.resumeboost.board.repository.BoardRepository;
import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplA implements BoardSeviceA{

  private final BoardRepository boardRepository;
  private final BoardImgRepository boardImgRepository;

  @Value("${file.path}")
  String saveFile;

  @Override
  public Page<BoardDto> ListAll(Pageable pageable, String subject, String search) {
    
    System.out.println(">>>>>>>Search!!" + subject + ":" + search);
    
    // Page<BoardEntity> boardEntities = boardRepository.findAll(pageable);

  
    Page<BoardEntity> boardEntities = null;
    
    if (subject == null & search == null) {
      boardEntities = boardRepository.findAll(pageable);
    } else if (subject.equals("title")) {
      boardEntities = boardRepository.findByTitleContaining(pageable, search);
    } else if (subject.equals("writer")) {
      boardEntities = boardRepository.findByWriterContaining(pageable, search);
    } else {
      boardEntities = boardRepository.findAll(pageable);
    }

    return (
      boardEntities.map(el -> BoardDto.toBoardDto(el))
    );

  }

  @Override
  public BoardDto boardDetail(Long boardId) {
    
    BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("게시글이 존재하지 않습니다!!"));

    return BoardDto.toBoardDto(boardEntity);
  }

  @Override
  public void boardDelete(Long boardId) {
    
    Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardId);
    if(!optionalBoardEntity.isPresent()) {
      throw new NullPointerException("삭제할 게시글이 존재하지 않습니다!");
    }

    boardRepository.deleteById(boardId);

  }

  @Override
  public void boardUpdate(BoardDto boardDto) throws IllegalStateException, IOException {
    
    Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardDto.getId());
    if (!optionalBoardEntity.isPresent()) {
      throw new NullPointerException("수정할 게시글이 없습니다!");
    }


    Optional<BoardImgEntity> optionalBoardImgEntity = boardImgRepository.findByBoardEntity(BoardEntity.builder().id(boardDto.getId()).build());
    // 수정 시 기존 파일 무조건 삭제 됨 주의
    if (optionalBoardImgEntity.isPresent()) { 
      String newImgName = optionalBoardImgEntity.get().getNewImgName();
      String saveFilePath = saveFile + "/board/" + newImgName;

      File deleteFile = new File(saveFilePath); // 이미 있으면 삭제

      if (deleteFile.exists()) {
        deleteFile.delete();
        System.out.println("파일을 삭제 합니다");
      } else {
        System.out.println("파일이 존재하지 않습니다!");
      }

      // DB 에서 삭제
      boardImgRepository.deleteById(optionalBoardImgEntity.get().getId());


    }

    if (boardDto.getBoardImgFile() == null) { 

      boardRepository.save(BoardEntity.builder()
      .id(boardDto.getId())
      .title(boardDto.getTitle())
      .content(boardDto.getContent())
      .category(boardDto.getCategory())
      .viewCount(boardDto.getViewCount())      
      .replyCount(boardDto.getReplyCount())
      .attachFile(0)
      .writer(boardDto.getWriter())
      .memberEntity(MemberEntity.builder().id(boardDto.getMemberId()).build())
      
      .build());

    } else { // 파일을 넣었을 때 -> 기존 파일 삭제 후 -> 새 파일 저장

      MultipartFile boardImgFile = boardDto.getBoardImgFile();
      UUID uuid = UUID.randomUUID();

      String oldImgName = boardImgFile.getOriginalFilename();
      String newImgName = uuid + oldImgName;

      String saveFilePath = saveFile + "/board/" + newImgName;
      boardImgFile.transferTo(new File(saveFilePath));

      boardRepository.save(BoardEntity.builder()
      .id(boardDto.getId())
      .title(boardDto.getTitle())
      .content(boardDto.getContent())
      .category(boardDto.getCategory())
      .viewCount(boardDto.getViewCount())      
      .replyCount(boardDto.getReplyCount())
      .attachFile(1)
      .writer(boardDto.getWriter())
      .memberEntity(MemberEntity.builder().id(boardDto.getMemberId()).build())
      .build());


      BoardImgEntity boardImgEntity = BoardImgEntity.builder()
      .newImgName(newImgName)
      .oldImgName(oldImgName)
      .boardEntity(optionalBoardEntity.get())
      .build();

      boardImgRepository.save(boardImgEntity);

    }



  }

 
}
