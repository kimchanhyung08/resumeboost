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
// // �亯 ������ ����
// AnswerEntity mentorRecommend = answerRepo.save(new AnswerEntity(0, "���並 
// õ�� �帱�Կ�.", "���� ��õ"));
// AnswerEntity viewRecommend = answerRepo.save(new AnswerEntity(0, "��ȸ���� ����
// ���並 ��õ�մϴ�.", "��ȸ�� ��õ"));
// AnswerEntity replyRecommend = answerRepo.save(new AnswerEntity(0, "���䰡 ���� ����
//  ��õ�մϴ�.", "����� ��õ"));
// AnswerEntity mentorSearch = answerRepo.save(new AnswerEntity(0, "ã�� ���� ����
//  �г����� �Է��ϼ���.", "���� �˻�"));
// AnswerEntity nicknameSearch = answerRepo.save(new AnswerEntity(0, "�ش� 
// ������ ���� ������ ��ȸ�մϴ�.", "�г��� �˻�"));

// // "�ȳ�"�� ���� �亯 ������ ����
// AnswerEntity helloAnswer = answerRepo.save(new AnswerEntity(0, "�ȳ��ϼ���! ����
//  ���͵帱���?", "�ȳ�"));

// // 1�� �ǵ� ���� (���� ����)
// IntentionEntity mentorIntent = intentionRepo.save(new IntentionEntity(0,
// "���� ��õ", mentorRecommend, null));
// IntentionEntity searchIntent = intentionRepo.save(new IntentionEntity(0,
// "���� �˻�", mentorSearch, null));

// // "�ȳ�"�� ���� �ǵ� ����
// IntentionEntity helloIntent = intentionRepo.save(new IntentionEntity(0,
// "�ȳ�", helloAnswer, null));

// // 2�� �ǵ� ���� (���� ���信 ����)
// intentionRepo.save(new IntentionEntity(0, "��ȸ�� ��õ", viewRecommend, me
// torIntent));
// intentionRepo.save(new IntentionEntity(0, "����� ��õ", replyRecommend, me
// torIntent));
// intentionRepo.save(new IntentionEntity(0, "�г��� �˻�", nicknameSearch, se
// rchIntent));

// System.out.println("? ê�� �ʱ� ������ ���� �Ϸ�!");
// };
// }
// }
