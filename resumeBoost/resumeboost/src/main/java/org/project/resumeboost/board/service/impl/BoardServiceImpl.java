package org.project.resumeboost.board.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.project.resumeboost.board.dto.BoardDto;
import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.board.entity.BoardImgEntity;
import org.project.resumeboost.board.repository.BoardImgRepository;
import org.project.resumeboost.board.repository.BoardRepository;
import org.project.resumeboost.board.service.BoardService;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.project.resumeboost.reply.entity.ReplyEntity;
import org.project.resumeboost.reply.repository.ReplyRepository;
import org.project.resumeboost.s3.S3UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

  private final BoardRepository boardRepository;
  private final BoardImgRepository boardImgRepository;
  private final MemberRepository memberRepository;
  private final ReplyRepository replyRepository;

  private final S3UploadService s3UploadService;

  @Value("${file.path}")
  String saveFile;

  public void myPostCount(Long memberId) {
    memberRepository.myPostCount(memberId);
  }

  @Override
  public void boardInsert(BoardDto boardDto) throws IOException {

    // 1. member 있는지 확인
    Long memberId = memberRepository.findById(boardDto.getMemberId()).orElseThrow(
        IllegalArgumentException::new).getId();

    // 2. 이미지 있는지 확인
    MultipartFile boardFile = boardDto.getBoardImgFile();

    if (boardFile == null || boardFile.isEmpty()) {
      // int i = 1;
      // if (1 == 1) {
      // 내가 작성한 게시글 카운트
      myPostCount(memberId);
      boardRepository.save(BoardEntity.toNotFileInsert(boardDto));
    } else {
      String oldImgName = boardFile.getOriginalFilename();
      // 암호화
      // UUID uuid = UUID.randomUUID();
      // String newImgName = uuid + "(ง •_•)ง" + oldImgName;
      // String saveFilePath = saveFile + "board/" + newImgName;
      // boardFile.transferTo(new File(saveFilePath));

      // S3 upload
      String uploadedImgUrl = s3UploadService.upload(boardFile, "images");
      String newImgName = uploadedImgUrl.substring(uploadedImgUrl.indexOf("images/") + "images/".length());

      BoardEntity boardEntity = BoardEntity.toYesFileInsert(boardDto);
      // 내가 작성한 게시글 카운트
      myPostCount(memberId);
      // 게시글 저장
      Long boardId = boardRepository.save(boardEntity).getId();
      BoardEntity entity = boardRepository.findById(boardId).orElseThrow(
          IllegalArgumentException::new);

      // 이미지 저장
      boardImgRepository.save(BoardImgEntity.builder()
          .newImgName(newImgName)
          .oldImgName(oldImgName)
          .boardEntity(entity)
          .build());
    }
  }

  @Override
  public void boardUpdate(BoardDto boardDto) throws IOException {

    Optional<BoardImgEntity> optionalBoardImgEntities = boardImgRepository.findByBoardEntityId(boardDto.getId());

    boardRepository.findById(boardDto.getId()).orElseThrow(
        IllegalArgumentException::new);

    // 1. member 있는지 확인
    memberRepository.findById(boardDto.getMemberId()).orElseThrow(
        IllegalArgumentException::new);

    // 2. 이미지 있는지 확인
    MultipartFile boardFile = boardDto.getBoardImgFile();
    String existingImgName = boardDto.getNewImgName();

    if ((boardFile == null || boardFile.isEmpty()) && existingImgName != null) {
      boardRepository.save(BoardEntity.toYesFileUpdate(boardDto));
    } else if (boardFile == null || boardFile.isEmpty()) {
      boardRepository.save(BoardEntity.toNotFileUpdate(boardDto));
    } else {
      if (optionalBoardImgEntities.isPresent()) {
        String newImgName = optionalBoardImgEntities.get().getNewImgName();
        // String saveFilePath = saveFile + "board/" + newImgName;
        // File deleteFile = new File(saveFilePath);
        // if (deleteFile.exists()) {
        // deleteFile.delete();
        // }

        // S3 기존 이미지 삭제
        s3UploadService.delete("images/" + newImgName);
        boardImgRepository.deleteById(optionalBoardImgEntities.get().getId());
      }
      String oldImgName = boardFile.getOriginalFilename();
      // 암호화
      // UUID uuid = UUID.randomUUID();
      // String newImgName = uuid + "(ง •_•)ง" + oldImgName;
      // String saveFilePath = saveFile + "board/" + newImgName;
      // boardFile.transferTo(new File(saveFilePath));

      // S3 upload
      String uploadedImgUrl = s3UploadService.upload(boardFile, "images");
      String newImgName = uploadedImgUrl.substring(uploadedImgUrl.indexOf("images/") + "images/".length());

      BoardEntity boardEntity = BoardEntity.toYesFileUpdate(boardDto);
      // 게시글 저장
      Long boardId = boardRepository.save(boardEntity).getId();
      BoardEntity entity = boardRepository.findById(boardId).orElseThrow(
          IllegalArgumentException::new);

      // 이미지 저장
      boardImgRepository.save(BoardImgEntity.builder()
          .newImgName(newImgName)
          .oldImgName(oldImgName)
          .boardEntity(entity)
          .build());
    }
  }

  @SuppressWarnings("null")
  @Override
  public Page<BoardDto> boardList(Pageable pageable, String subject, String search) {
    Page<BoardEntity> boardEntities = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      boardEntities = boardRepository.findAll(pageable);
    } else {
      if (subject.equals("title")) {
        boardEntities = boardRepository.findByTitleContaining(pageable, search);
      } else if (subject.equals("content")) {
        boardEntities = boardRepository.findByContentContaining(pageable, search);
      } else if (subject.equals("writer")) {
        boardEntities = boardRepository.findByWriterContaining(pageable, search);
      } else {
        boardEntities = boardRepository.findByTitleOrContentOrWriterContaining(pageable, search);
      }
    }

    return boardEntities.map(BoardDto::toBoardDto);
  }

  String letter;

  @SuppressWarnings("null")
  @Override
  public Page<BoardDto> boardListLetter(Pageable pageable, String subject, String search) {
    Page<BoardEntity> boardEntities = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      boardEntities = boardRepository.findByCategoryContaining(pageable, "자기소개서");
    } else {
      letter = "자기소개서";
      if (subject.equals("title")) {
        boardEntities = boardRepository.findByTitleAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("content")) {
        boardEntities = boardRepository.findByContentAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("writer")) {
        boardEntities = boardRepository.findByWriterAndCategoryContaining(pageable, search, letter);
      } else {
        boardEntities = boardRepository.findByTitleOrContentOrWriterAndLetterContaining(pageable, search, letter);
      }
    }

    return boardEntities.map(BoardDto::toBoardDto);
  }

  @SuppressWarnings("null")
  @Override
  public Page<BoardDto> boardListResume(Pageable pageable, String subject, String search) {
    Page<BoardEntity> boardEntities = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      boardEntities = boardRepository.findByCategoryContaining(pageable, "이력서");
    } else {
      letter = "이력서";
      if (subject.equals("title")) {
        boardEntities = boardRepository.findByTitleAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("content")) {
        boardEntities = boardRepository.findByContentAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("writer")) {
        boardEntities = boardRepository.findByWriterAndCategoryContaining(pageable, search, letter);
      } else {
        boardEntities = boardRepository.findByTitleOrContentOrWriterAndLetterContaining(pageable, search, letter);
      }
    }

    return boardEntities.map(BoardDto::toBoardDto);
  }

  @SuppressWarnings("null")
  @Override
  public Page<BoardDto> boardListInterview(Pageable pageable, String subject, String search) {
    Page<BoardEntity> boardEntities = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      boardEntities = boardRepository.findByCategoryContaining(pageable, "면접");
    } else {
      letter = "면접";
      if (subject.equals("title")) {
        boardEntities = boardRepository.findByTitleAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("content")) {
        boardEntities = boardRepository.findByContentAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("writer")) {
        boardEntities = boardRepository.findByWriterAndCategoryContaining(pageable, search, letter);
      } else {
        boardEntities = boardRepository.findByTitleOrContentOrWriterAndLetterContaining(pageable, search, letter);
      }
    }

    return boardEntities.map(BoardDto::toBoardDto);
  }

  @SuppressWarnings("null")
  @Override
  public Page<BoardDto> boardListFreedom(Pageable pageable, String subject, String search) {
    Page<BoardEntity> boardEntities = null;

    if (subject == null || search == null || search.trim().length() <= 0) {
      boardEntities = boardRepository.findByCategoryContaining(pageable, "자유게시판");
    } else {
      letter = "자유게시판";
      if (subject.equals("title")) {
        boardEntities = boardRepository.findByTitleAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("content")) {
        boardEntities = boardRepository.findByContentAndCategoryContaining(pageable, search, letter);
      } else if (subject.equals("writer")) {
        boardEntities = boardRepository.findByWriterAndCategoryContaining(pageable, search, letter);
      } else {
        boardEntities = boardRepository.findByTitleOrContentOrWriterAndLetterContaining(pageable, search, letter);
      }
    }

    return boardEntities.map(BoardDto::toBoardDto);
  }

  @Override
  public Page<BoardDto> boardMyList(Pageable pageable, Long memberId) {
    Page<BoardEntity> boardEntities = boardRepository.findAllByMemberEntityId(memberId, pageable);
    return boardEntities.map(BoardDto::toBoardDto);
  }

  @Override
  public void BoardViewCount(Long id) {
    boardRepository.BoardViewCount(id);
  }

  public void myPostCountDelete(Long id) {
    memberRepository.myPostCountDelete(id);
  }

  @Override
  public void boardDelete(Long boardId) {

    Optional<BoardImgEntity> optionalBoardImgEntities = boardImgRepository.findByBoardEntityId(boardId);
    if (optionalBoardImgEntities.isPresent()) {
      String newImgName = optionalBoardImgEntities.get().getNewImgName();
      // String saveFilePath = saveFile + "board/" + newImgName;
      // File deleteFile = new File(saveFilePath);
      // if (deleteFile.exists()) {
      // deleteFile.delete();
      // }

      // S3 이미지 삭제
      s3UploadService.delete("images/" + newImgName);
      boardImgRepository.deleteById(optionalBoardImgEntities.get().getId());
    }

    Long id = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."))
        .getMemberEntity().getId();

    List<ReplyEntity> replyEntities = replyRepository.findAllByBoardEntityId(boardId);

    for (ReplyEntity replyEntity : replyEntities) {
      Long memberId = replyEntity.getMemberEntity().getId();
      MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
      memberRepository.myReplyCountDelete(memberEntity.getId());
    }

    myPostCountDelete(id);
    boardRepository.deleteById(boardId);
  }

  @Override
  public BoardDto boardDetail(Long boardId) {
    BoardViewCount(boardId);
    BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
    return BoardDto.toBoardDto(boardEntity);
  }

}
