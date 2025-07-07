package org.project.resumeboost.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class MessageDto { // ë©”ì‹œì§? ì¶œë ¥

  private String today;

  private String time;

  private AnswerDto answer;

  public MessageDto today(String today) {
    this.today = today;
    return this;
  }

  public MessageDto answer(AnswerDto answer) { // ?‹µë³? , ?‚¤?›Œ?“œ , ? „?™” ë²ˆí˜¸
    this.answer = answer;
    return this;
  }
}
