package com.project.Obur.us.service;

import com.project.Obur.us.dto.ReviewDTO;
import com.project.Obur.us.persistence.entity.Place;
import com.project.Obur.us.persistence.entity.Review;
import com.project.Obur.us.persistence.entity.User;
import com.project.Obur.us.repository.PlaceRepository;
import com.project.Obur.us.repository.ReviewRepository;
import com.project.Obur.us.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Place place = placeRepository.findById(reviewDTO.getPlaceId())
                .orElseThrow(() -> new RuntimeException("Place not found"));

        if (reviewRepository.existsByUserIdAndPlaceId(userId, reviewDTO.getPlaceId())) {
            throw new RuntimeException("You have already reviewed this place");
        }

        Review review = new Review();
        review.setUser(user);
        review.setPlace(place);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setVisitDate(reviewDTO.getVisitDate());
        review.setIsVerified(false);

        review = reviewRepository.save(review);

        return convertToDTO(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByPlace(Long placeId) {
        return reviewRepository.findByPlaceIdOrderByCreatedAtDesc(placeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUser(Long userId) {
        return reviewRepository.findUserRecentReviews(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own reviews");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setVisitDate(reviewDTO.getVisitDate());

        review = reviewRepository.save(review);

        return convertToDTO(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setPlaceId(review.getPlace().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setVisitDate(review.getVisitDate());
        dto.setIsVerified(review.getIsVerified());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
