package com.project.Obur.us.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {
    private Long id;
    private String name;
    private String description;
    private String cuisine;
    private String priceRange;
    private BigDecimal averagePrice;
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String phoneNumber;
    private String websiteUrl;
    private String imageUrl;
    private String openingHours;
    private Double averageRating;
    private Integer totalReviews;
    private Double distance; // Distance from user location in kilometers
    private LocalDateTime createdAt;
}
