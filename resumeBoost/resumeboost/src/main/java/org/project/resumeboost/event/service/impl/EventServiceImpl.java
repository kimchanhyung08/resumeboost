package org.project.resumeboost.event.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.project.resumeboost.admin.controller.AdminBoardContollrer;
import org.project.resumeboost.basic.controller.APIRefreshController;
import org.project.resumeboost.event.dto.EventDto;
import org.project.resumeboost.event.entity.EventEntity;
import org.project.resumeboost.event.repository.EventRepository;
import org.project.resumeboost.event.service.EventService;
import org.project.resumeboost.member.entity.MemberEntity;
import org.project.resumeboost.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

  private final EventRepository eventRepository;
  private final MemberRepository memberRepository;

   

  @Override
  public void eventInsert(EventDto eventDto) {
    
    Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(eventDto.getMemberId());
    if (!optionalMemberEntity.isPresent()) {
      throw new NullPointerException("회원이 존재하지 않습니다.");
    }

    System.out.println(">>>>>>>>>>>>>>>>>" + eventDto.getStart());
    System.out.println(eventDto.getEnd());

    eventRepository.save(EventEntity.builder()
    .title(eventDto.getTitle())
    .content(eventDto.getContent())
    .start(eventDto.getStart())
    .end(eventDto.getEnd())
    .memberEntity(MemberEntity.builder().id(eventDto.getMemberId()).build())
    .build());

  }

  @Override
  public List<EventDto> eventList(Long memberId) {
    
    List<EventEntity> eventEntities = eventRepository.findAllByMemberEntityId(memberId);

    return eventEntities.stream().map(el-> EventDto.builder()
    .id(el.getId())
    .title(el.getTitle())
    .content(el.getContent())
    .start(el.getStart())
    .end(el.getEnd())
    .memberId(el.getMemberEntity().getId())
    .build()).collect(Collectors.toList());
  }

  @Override
  public void eventDelete(Long eventId) {
    
    Optional<EventEntity> optionalEventEntity = eventRepository.findById(eventId);
    if (!optionalEventEntity.isPresent()) {
      throw new NullPointerException("삭제할 일정이 없습니다!!");
    }


    eventRepository.deleteById(eventId);

  }

  @Override
  public EventDto eventDetail(Long eventId) {
    
    EventEntity eventEntity = eventRepository.findById(eventId).orElseThrow(()-> new NullPointerException("일정이 존재하지 않습니다."));

    return (
      EventDto.builder()
      .id(eventEntity.getId())
      .title(eventEntity.getTitle())
      .content(eventEntity.getContent())
      .start(eventEntity.getStart())
      .end(eventEntity.getEnd())
      .memberId(eventEntity.getMemberEntity().getId())
      .memberEntity(eventEntity.getMemberEntity())
      .build()
    );


  }
  
}
