package org.project.resumeboost.event.entity;

import java.sql.Date;

import org.project.resumeboost.member.entity.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "event_tb")
public class EventEntity {


  @Id
  @Column(name = "event_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 제목
  @Column(nullable = false)
  private String title;

  // 내용
  @Column(nullable = false)
  private String content;

  // 일정 시작 시간
  @Column(nullable = false)
  private Date start;

  // 일정 종료 시간
  @Column(nullable = false)
  private Date end;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

}
