package com.project.Obur.us.controller;

import com.project.Obur.us.model.dto.PlaceDTO;
import com.project.Obur.us.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Places", description = "Restaurant and place management endpoints")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    @Operation(summary = "Get nearby places",
            description = "Find restaurants within a specified radius from coordinates")
    public ResponseEntity<List<PlaceDTO>> getNearbyPlaces(
            @Parameter(description = "Latitude", required = true)
            @RequestParam Double lat,

            @Parameter(description = "Longitude", required = true)
            @RequestParam Double lng,

            @Parameter(description = "Search radius in meters", example = "1500")
            @RequestParam(defaultValue = "1500")
            @Min(100) @Max(50000) Double radius,

            @Parameter(description = "Maximum number of results", example = "50")
            @RequestParam(defaultValue = "50")
            @Min(1) @Max(200) Integer limit
    ) {
        log.info("GET /api/v1/places - lat: {}, lng: {}, radius: {}m, limit: {}",
                lat, lng, radius, limit);

        try {
            List<PlaceDTO> places = placeService.findNearbyPlaces(lat, lng, radius, limit);
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            log.error("Error fetching nearby places", e);
            throw new RuntimeException("Error fetching places: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search places by name")
    public ResponseEntity<?> searchPlaces(
            @Parameter(description = "Search query")
            @RequestParam String query
    ) {
        log.info("GET /api/v1/places/search - query: {}", query);
        return ResponseEntity.ok(placeService.searchByName(query));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Find places by category")
    public ResponseEntity<?> getPlacesByCategory(
            @Parameter(description = "Category name")
            @PathVariable String category
    ) {
        log.info("GET /api/v1/places/category/{}", category);
        return ResponseEntity.ok(placeService.findByCategory(category));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get place by ID")
    public ResponseEntity<?> getPlaceById(
            @Parameter(description = "Place ID")
            @PathVariable Long id
    ) {
        log.info("GET /api/v1/places/{}", id);
        return ResponseEntity.ok(placeService.findById(id));
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top rated places nearby")
    public ResponseEntity<?> getTopRatedNearby(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "5000") Double radius,
            @RequestParam(defaultValue = "4.0") Double minRating,
            @RequestParam(defaultValue = "20") Integer limit
    ) {
        log.info("GET /api/v1/places/top-rated - lat: {}, lng: {}, radius: {}m, minRating: {}",
                lat, lng, radius, minRating);
        return ResponseEntity.ok(
                placeService.getTopRatedNearby(lat, lng, radius, minRating, limit)
        );
    }
}
