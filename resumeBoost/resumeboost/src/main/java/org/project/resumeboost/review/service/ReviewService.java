package org.project.resumeboost.review.service;

import java.util.List;

import org.project.resumeboost.review.dto.ReviewDto;

public interface ReviewService {
  List<ReviewDto> reviewList();

  List<ReviewDto> mentorReview(Long mentorId);

  void deleteReview(Long reviewId);

  List<ReviewDto> memberReview(Long memberId);

  void updateReview(Long id, ReviewDto reviewDto);
}
