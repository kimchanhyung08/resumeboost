package org.project.resumeboost.event.service;

import java.util.List;

import org.project.resumeboost.event.dto.EventDto;

public interface EventService {

  void eventInsert(EventDto eventDto);

  List<EventDto> eventList(Long memberId);

  void eventDelete(Long eventId);

  EventDto eventDetail(Long eventId);

}
