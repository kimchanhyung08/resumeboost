// package org.project.resumeboost.chatbot.config;

// import org.project.resumeboost.chatbot.entity.AnswerEntity;
// import org.project.resumeboost.chatbot.entity.IntentionEntity;
// import org.project.resumeboost.chatbot.repository.AnswerRepository;
// import org.project.resumeboost.chatbot.repository.IntentionRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.annotation.Transactional;

// @Configuration
// public class ChatbotDataInitializer {

// @Bean
// @Transactional
// public CommandLineRunner initData(AnswerRepository answerRepo,
// IntentionRepository intentionRepo) {
// return args -> {
// // 답변 데이터 저장
// AnswerEntity mentorRecommend = answerRepo.save(new AnswerEntity(0, "멘토를 
// 천해 드릴게요.", "멘토 추천"));
// AnswerEntity viewRecommend = answerRepo.save(new AnswerEntity(0, "조회수가 많은
// 멘토를 추천합니다.", "조회수 추천"));
// AnswerEntity replyRecommend = answerRepo.save(new AnswerEntity(0, "리뷰가 많은 멘토
//  추천합니다.", "리뷰수 추천"));
// AnswerEntity mentorSearch = answerRepo.save(new AnswerEntity(0, "찾고 싶은 멘토
//  닉네임을 입력하세요.", "멘토 검색"));
// AnswerEntity nicknameSearch = answerRepo.save(new AnswerEntity(0, "해당 
// 네임의 멘토 정보를 조회합니다.", "닉네임 검색"));

// // "안녕"에 대한 답변 데이터 저장
// AnswerEntity helloAnswer = answerRepo.save(new AnswerEntity(0, "안녕하세요! 무엇
//  도와드릴까요?", "안녕"));

// // 1차 의도 저장 (상위 개념)
// IntentionEntity mentorIntent = intentionRepo.save(new IntentionEntity(0,
// "멘토 추천", mentorRecommend, null));
// IntentionEntity searchIntent = intentionRepo.save(new IntentionEntity(0,
// "멘토 검색", mentorSearch, null));

// // "안녕"에 대한 의도 저장
// IntentionEntity helloIntent = intentionRepo.save(new IntentionEntity(0,
// "안녕", helloAnswer, null));

// // 2차 의도 저장 (상위 개념에 종속)
// intentionRepo.save(new IntentionEntity(0, "조회수 추천", viewRecommend, me
// torIntent));
// intentionRepo.save(new IntentionEntity(0, "리뷰수 추천", replyRecommend, me
// torIntent));
// intentionRepo.save(new IntentionEntity(0, "닉네임 검색", nicknameSearch, se
// rchIntent));

// System.out.println("? 챗봇 초기 데이터 저장 완료!");
// };
// }
// }
