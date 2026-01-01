package com.project.Obur.us.service;

import com.project.Obur.us.model.dto.PlaceDTO;
import com.project.Obur.us.model.entity.Place;
import com.project.Obur.us.repository.PlaceRepository;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;

    /**
     * Find nearby places within radius (in meters)
     */
    @Cacheable(value = "nearbyPlaces", key = "#lat + '_' + #lng + '_' + #radius + '_' + #limit")
    public List<PlaceDTO> findNearbyPlaces(Double lat, Double lng, Double radius, Integer limit) {
        log.debug("Finding nearby places: lat={}, lng={}, radius={}m, limit={}", lat, lng, radius, limit);

        List<Object[]> results = placeRepository.findNearbyPlaces(lng, lat, radius, limit);
        List<PlaceDTO> places = new ArrayList<>();

        for (Object[] row : results) {
            PlaceDTO dto = PlaceDTO.builder()
                    .id(((BigDecimal) row[0]).longValue())
                    .name((String) row[1])
                    .address((String) row[2])
                    .categories((String) row[3])
                    .ratingAvg(row[4] != null ? ((Number) row[4]).doubleValue() : null)
                    .ratingCount(row[5] != null ? ((Number) row[5]).intValue() : null)
                    .source((String) row[6])
                    .distanceM(row[7] != null ? ((Number) row[7]).doubleValue() : null)
                    .lat(row[8] != null ? ((Number) row[8]).doubleValue() : null)
                    .lng(row[9] != null ? ((Number) row[9]).doubleValue() : null)
                    .build();
            places.add(dto);
        }

        log.debug("Found {} nearby places", places.size());
        return places;
    }

    /**
     * Search places by name
     */
    public List<Place> searchByName(String name) {
        log.debug("Searching places by name: {}", name);
        return placeRepository.searchByName(name);
    }

    /**
     * Find places by category
     */
    public List<Place> findByCategory(String category) {
        log.debug("Finding places by category: {}", category);
        return placeRepository.findByCategory(category);
    }

    /**
     * Get  places nearby
     */
    public List<Place> getTopRatedNearby(Double lat, Double lng, Double radius, Double minRating, Integer limit) {
        log.debug("Getting top rated places: lat={}, lng={}, radius={}m, minRating={}, limit={}",
                lat, lng, radius, minRating, limit);
        return placeRepository.findTopRatedNearby(lng, lat, radius, minRating, limit);
    }

    /**
     * Find place by ID
     */
    public Place findById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
    }

    /**
     * Get all places
     */
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }
}
