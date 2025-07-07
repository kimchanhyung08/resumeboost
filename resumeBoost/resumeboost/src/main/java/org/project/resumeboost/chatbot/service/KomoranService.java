package org.project.resumeboost.chatbot.service;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

import org.project.resumeboost.basic.common.Role;
import org.project.resumeboost.chatbot.dto.AnswerDto;
import org.project.resumeboost.chatbot.dto.MessageDto;
import org.project.resumeboost.chatbot.entity.AnswerEntity;
import org.project.resumeboost.chatbot.entity.IntentionEntity;
import org.project.resumeboost.chatbot.repository.IntentionRepository;
import org.project.resumeboost.member.dto.MemberDto;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KomoranService {

  @Autowired
  private Komoran komoran; // 등록 Bean
  // NLP(Natural Language Processing, 자연어 처리)는 인공지능의 한 분야로서 머신러닝을 사용하여 텍스트와 데이터를
  // 처리하고 해석

  public MessageDto nlpAnalyze(String message) {
    System.out.println("message>>>:" + message);
    // 입력 문장에 대한 형태소 분석을 수행 -> 입력 문장에 대한 형태소 분석 결과를 KomoranResult 객체로 반환
    KomoranResult result = komoran.analyze(message);// komoran message 분석
    // 문자에서 명사들(리스트)만 추출한 목록 중복제거해서 set
    Set<String> nouns = result.getNouns().stream().collect(Collectors.toSet());

    nouns.forEach((noun) -> {
      System.out.println(">>> >> :" + noun);
    });
    ;// 메세지에서 명사추출 noun
    System.out.println(analyzeToken(nouns) + "  << result");
    return analyzeToken(nouns);
  }

  // 추출된 명사를 이용하여 DB 접근
  private MessageDto analyzeToken(Set<String> nouns) {
    // 시간 설정
    LocalDateTime today = LocalDateTime.now();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a H:mm");
    MessageDto messageDto = MessageDto.builder()
        .time(today.format(timeFormatter))
        .build();

    // 1차 -> 존재하는지
    for (String token : nouns) {

      System.out.println(">>> >> :" + token.toString());
      Optional<IntentionEntity> result = decisionTree(token, null);

      if (result.isEmpty())
        continue;// 존재하지 않으면 다음토큰 검색
      // 1차 토근확인시 실행
      System.out.println(">>>>1차:" + token);
      // 1차목록 복사
      Set<String> next = nouns.stream().collect(Collectors.toSet());
      // 목록에서 1차토큰 제거
      next.remove(token);

      // 2차분석 메서드
      AnswerDto answer = analyzeToken(next, result).toAnswerDTO();

      if (token.contains("멘토 추천") || token.contains("멘토") || token.contains("리뷰")) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        messageDto.today(today.format(dateFormatter));// 처음 접속할때만 날짜표기
        System.out.println(token + " 멘토 추천  token");
        List<MemberDto> mentor = analyzeTokenIsReplyMentor();
        if (mentor != null) {
          answer.mentorList(mentor);
        }
      } else if (token.contains("조회수 추천") || token.contains("조회")) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        messageDto.today(today.format(dateFormatter));// 처음 접속할때만 날짜표기
        List<MemberDto> mentor = analyzeTokenIsViewMentor();
        if (mentor != null) {
          answer.mentorList(mentor);
        }
      } else if (token.contains("검색") || token.contains("닉네임")) {
        MemberDto mentor = findMentorByNickName(next);
        if (mentor != null) {
          answer.mentor(mentor);
        }
      }

      messageDto.answer(answer);// 토근에대한 응답정보
      return messageDto;
    }

    // 분석 명사들이 등록한 의도와 일치하는게 존재하지 않을경우 "기타나 null일 경우"
    Optional<IntentionEntity> defaultIntention = decisionTree("기타", null);

    if (defaultIntention.isPresent()) {
      // "기타" 의도가 존재하는 경우
      AnswerDto answer = defaultIntention.get().getAnswer().toAnswerDTO();
      messageDto.answer(answer);
    } else {
      // "기타" 의도가 데이터베이스에 없는 경우 기본 응답 생성
      AnswerDto fallbackAnswer = AnswerDto.builder()
          .content("죄송합니다.\\n 이해하지 못했습니다.\\n 다른 질문을 해보세요! \\n" +
              " \\n" +
              "\"조회수 높은 멘토 추천\"\\n" +
              " \"리뷰 많은 멘토 추천\" \\n" +
              "\"멘토 정보 검색\"\\n" +
              "\\n" +
              "이러한 항목에 도움을 줄 수 있어요!")
          .build();
      messageDto.answer(fallbackAnswer);
    }

    return messageDto;
  }

  // 1차
  @Autowired
  IntentionRepository intention;

  // 의도가 존재하는지 DB에서 파악 -> 키워드 값 존재 하는 지 ->DB에 키워드가 있는지 확인
  private Optional<IntentionEntity> decisionTree(String token, IntentionEntity upper) {
    return intention.findByNameAndUpper(token, upper);
  }

  // 1차의도가 존재하면
  // 하위의도가 존재하는지 파악
  private AnswerEntity analyzeToken(Set<String> next, Optional<IntentionEntity> upper) {
    // upper가 비어있는지 먼저 확인 (방어적 코딩)
    if (upper.isEmpty()) {
      throw new IllegalArgumentException("상위 의도 엔티티가 없습니다.");
    }

    System.out.println("2차 의도-> " + next);

    for (String token : next) {
      // 1차의도를 부모로하는 토큰이 존재하는지 파악
      System.out.println("2차 의도2 -> " + next);
      Optional<IntentionEntity> result = decisionTree(token, upper.get());
      if (result.isEmpty())
        continue;
      return result.get().getAnswer(); // 1차-2차 존재하는경우 답변
    }
    return upper.get().getAnswer(); // 1차만 존재하는 답변
  }

  @Autowired
  MemberRepository memberRepository;
  Pageable top5 = PageRequest.of(0, 5);

  // replyCount 기준으로 멘토 추천
  private List<MemberDto> analyzeTokenIsReplyMentor() {
    Page<MemberEntity> mentorEntities = memberRepository.findByRoleOrderByReplyCountDesc(Role.MENTOR, top5);
    System.out.println("Mentor List: " + mentorEntities);
    return mentorEntities.stream().map(this::mentorToDto).collect(Collectors.toList());
  }

  // viewCount 기준으로 멘토 추천
  private List<MemberDto> analyzeTokenIsViewMentor() {
    Page<MemberEntity> mentorEntities = memberRepository.findByRoleOrderByViewCountDesc(Role.MENTOR, top5);
    System.out.println("Mentor List: " + mentorEntities);
    return mentorEntities.stream().map(this::mentorToDto).collect(Collectors.toList());
  }

  // 닉네임으로 멘토 검색
  private MemberDto findMentorByNickName(Set<String> next) {
    for (String nickName : next) {
      System.out.println(nickName);

      Optional<MemberEntity> mentor = memberRepository.findByNickName(nickName);
      System.out.println("Mentor List: " + mentor);
      if (mentor.isPresent()) {
        MemberEntity mentorEntity = mentor.get();
        return mentorToDto(mentorEntity);
      }
    }
    return null;
  }

  private MemberDto mentorToDto(MemberEntity mentor) {
    return MemberDto.toMemberDto(mentor);
  }
}