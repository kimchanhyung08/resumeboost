package org.project.resumeboost.review.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.project.resumeboost.review.dto.ReviewDto;
import org.project.resumeboost.review.entity.ReviewEntity;
import org.project.resumeboost.review.repository.ReviewRepository;
import org.project.resumeboost.review.service.ReviewService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;

  @Override
  public List<ReviewDto> reviewList() {

    List<ReviewEntity> reviewEntities = reviewRepository.findAll();

    if (reviewEntities.isEmpty()) {
      throw new IllegalArgumentException();
    }

    return reviewEntities.stream().map(el -> ReviewDto.builder()
        .content(el.getContent())
        .id(el.getId())
        .memberEntity(el.getMemberEntity())
        .mentorId(el.getMentorId())
        .mentorNickName(el.getMentorNickName())
        .memberId(el.getMemberEntity().getId())
        .build()).collect(Collectors.toList());
  }

  @Override
  public List<ReviewDto> mentorReview(Long mentorId) {
    List<ReviewEntity> reviewEntities = reviewRepository.findByMentorId(mentorId);

    return reviewEntities.stream().map(el -> ReviewDto.builder()
        .content(el.getContent())
        .memberEntity(el.getMemberEntity())
        .id(el.getId())
        .memberId(el.getMemberEntity().getId())
        .mentorId(el.getMentorId())
        .mentorNickName(el.getMentorNickName())
        .createTime(el.getCreateTime())
        .updateTime(el.getUpdateTime())
        .build()).collect(Collectors.toList());
  }

  @Override
  public void deleteReview(Long reviewId) {
    reviewRepository.findById(reviewId).orElseThrow(IllegalArgumentException::new);
    reviewRepository.deleteById(reviewId);
  }

  @Override
  public List<ReviewDto> memberReview(Long memberId) {
    List<ReviewEntity> reviewEntities = reviewRepository.findByMemberEntityId(memberId);

    return reviewEntities.stream().map(el -> ReviewDto.builder()
        .content(el.getContent())
        .memberEntity(el.getMemberEntity())
        .id(el.getId())
        .memberId(el.getMemberEntity().getId())
        .mentorId(el.getMentorId())
        .mentorNickName(el.getMentorNickName())
        .createTime(el.getCreateTime())
        .updateTime(el.getUpdateTime())
        .build()).collect(Collectors.toList());
  }

  @Override
  public void updateReview(Long id, ReviewDto reviewDto) {
    ReviewEntity reviewEntity = reviewRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    reviewEntity.setContent(reviewDto.getContent());
    reviewRepository.save(reviewEntity);
  }

}
