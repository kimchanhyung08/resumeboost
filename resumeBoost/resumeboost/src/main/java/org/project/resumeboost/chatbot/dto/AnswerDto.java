package org.project.resumeboost.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// import org.project.resumeboost.openapi.bus.BusDto;
// import org.project.resumeboost.openapi.movie.chabot.MovieDto;
// import org.project.resumeboost.openapi.weather.Sys;
// import org.project.resumeboost.openapi.weather.WeatherApiDto;
// import org.project.resumeboost.openapi.weather.WeatherDto;

import java.util.List;

import org.project.resumeboost.member.dto.MemberDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnswerDto {

  private long no;
  private String content;
  private String keyword; // 키워드
  private MemberDto mentor;
  private List<MemberDto> mentorList;

  public AnswerDto mentor(MemberDto mentor) {
    this.mentor = mentor;
    return this;
  }

  public AnswerDto mentorList(List<MemberDto> mentorList) {
    this.mentorList = mentorList;
    return this;
  }

  // public AnswerDto country(Sys country) {
  // this.country = country;
  // return this;
  // }

  // public AnswerDto weatherDto(WeatherDto weatherDto) {
  // this.weatherDto = weatherDto;
  // return this;
  // }

  // public AnswerDto busDto(List<BusDto> busDto) {
  // this.busDto = busDto;
  // return this;
  // }
}
