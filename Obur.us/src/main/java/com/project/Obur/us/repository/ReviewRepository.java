package com.project.Obur.us.repository;

import com.project.Obur.us.persistence.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

    List<Review> findByPlaceId(Long placeId);

    List<Review> findByPlaceIdOrderByCreatedAtDesc(Long placeId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId " +
            "ORDER BY r.createdAt DESC")
    List<Review> findUserRecentReviews(@Param("userId") Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.place.id = :placeId")
    Double calculateAverageRating(@Param("placeId") Long placeId);

    Boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}
