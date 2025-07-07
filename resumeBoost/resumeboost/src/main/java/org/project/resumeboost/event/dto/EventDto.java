package org.project.resumeboost.event.dto;

import java.sql.Date;

import org.project.resumeboost.member.entity.MemberEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
  

  private Long id;
  
  private String title;
  private String content;

  
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
  private Date start;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
  private Date end;

  private Long memberId;
  private MemberEntity memberEntity;  


}
