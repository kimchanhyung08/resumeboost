package org.project.resumeboost.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.project.resumeboost.admin.service.impl.MemberServiceImplA;
import org.project.resumeboost.basic.controller.APIRefreshController;
import org.project.resumeboost.event.dto.EventDto;
import org.project.resumeboost.event.service.impl.EventServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/event")
@RequiredArgsConstructor
public class AdminEventController {

  private final EventServiceImpl eventService;

  @GetMapping("/eventList/{memberId}")
  public ResponseEntity<?> eventList(@PathVariable(name = "memberId") Long memberId) {

    List<EventDto> eventDtos = eventService.eventList(memberId);

    Map<String, List<EventDto>> map = new HashMap<>();

    map.put("event", eventDtos);

    return ResponseEntity.status(HttpStatus.OK).body(map);

  }

  @PostMapping("/insert")
  public void eventInsert(
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") @RequestBody EventDto eventDto) {

    eventService.eventInsert(eventDto);

  }

  @DeleteMapping("/delete/{eventId}")
  public void eventDelete(@PathVariable(name = "eventId") Long eventId) {

    eventService.eventDelete(eventId);

  }

  @GetMapping("/detail/{eventId}")
  public ResponseEntity<?> eventDetail(@PathVariable(name = "eventId") Long eventId) {

    EventDto eventDto = eventService.eventDetail(eventId);

    Map<String, EventDto> map = new HashMap<>();

    map.put("event", eventDto);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

}
