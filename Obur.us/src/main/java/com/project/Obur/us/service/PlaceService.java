package com.project.Obur.us.service;

import com.project.Obur.us.dto.PlaceDTO;
import com.project.Obur.us.persistence.entity.Place;
import com.project.Obur.us.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional(readOnly = true)
    public PlaceDTO getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
        return convertToDTO(place);
    }

    @Transactional(readOnly = true)
    public List<PlaceDTO> getAllPlaces() {
        return placeRepository.findByIsActiveTrueOrderByAverageRatingDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceDTO> getNearbyPlaces(Double latitude, Double longitude, Double radiusKm) {
        Point userLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        double radiusMeters = radiusKm * 1000;

        return placeRepository.findNearbyPlaces(userLocation, radiusMeters)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceDTO> searchPlaces(String searchTerm) {
        return placeRepository.searchByName(searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceDTO> getPlacesByCuisine(String cuisine) {
        return placeRepository.findByCuisineAndIsActiveTrue(cuisine)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceDTO> getPlacesByPriceRange(String priceRange) {
        return placeRepository.findByPriceRangeAndIsActiveTrue(priceRange)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PlaceDTO convertToDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setDescription(place.getDescription());
        dto.setCuisine(place.getCuisine());
        dto.setPriceRange(place.getPriceRange());
        dto.setAveragePrice(place.getAveragePrice());

        if (place.getLocationGeo() != null) {
            dto.setLatitude(place.getLocationGeo().getY());
            dto.setLongitude(place.getLocationGeo().getX());
        }

        dto.setAddress(place.getAddress());
        dto.setCity(place.getCity());
        dto.setPhoneNumber(place.getPhoneNumber());
        dto.setWebsiteUrl(place.getWebsiteUrl());
        dto.setImageUrl(place.getImageUrl());
        dto.setOpeningHours(place.getOpeningHours());
        dto.setAverageRating(place.getAverageRating());
        dto.setTotalReviews(place.getTotalReviews());
        dto.setCreatedAt(place.getCreatedAt());

        return dto;
    }
}
