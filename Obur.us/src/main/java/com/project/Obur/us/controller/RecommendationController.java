package com.project.Obur.us.controller;

import com.project.Obur.us.model.dto.PlaceDTO;
import com.project.Obur.us.service.PlaceService;
import com.project.Obur.us.service.RecommenderService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recommendations", description = "Personalized restaurant recommendation endpoints")
public class RecommendationController {

    private final PlaceService placeService;
    private final RecommenderService recommenderService;

    @GetMapping
    @Operation(summary = "Get personalized recommendations",
            description = "Get restaurant recommendations based on location and user preferences")
    public ResponseEntity<Map<String, Object>> getRecommendations(
            @Parameter(description = "Latitude", required = true)
            @RequestParam Double lat,

            @Parameter(description = "Longitude", required = true)
            @RequestParam Double lng,

            @Parameter(description = "User ID (optional)")
            @RequestParam(required = false) String userId,

            @Parameter(description = "Search radius in kilometers", example = "5.0")
            @RequestParam(defaultValue = "5.0")
            @Min(1) @Max(50) Double radiusKm,

            @Parameter(description = "Maximum number of recommendations", example = "10")
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(50) Integer topK
    ) {
        log.info("GET /api/v1/recommendations - lat: {}, lng: {}, userId: {}, radiusKm: {}, topK: {}",
                lat, lng, userId, radiusKm, topK);

        try {
            // Get nearby candidates from PostgreSQL/PostGIS
            List<PlaceDTO> candidates = placeService.findNearbyPlaces(
                    lat, lng, radiusKm * 1000, 200 // Convert km to meters
            );

            if (candidates.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "message", "No candidates found in this area",
                        "count", 0,
                        "items", List.of()
                ));
            }

            // Get recommendations from Python service
            Map<String, Object> recommendations = recommenderService.getRecommendations(
                    userId, lat, lng, candidates
            );

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error getting recommendations", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to get recommendations",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user-specific recommendations",
            description = "Get personalized recommendations based on user's historical preferences")
    public ResponseEntity<Map<String, Object>> getUserRecommendations(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId,

            @Parameter(description = "Latitude", required = true)
            @RequestParam Double lat,

            @Parameter(description = "Longitude", required = true)
            @RequestParam Double lng,

            @Parameter(description = "Search radius in kilometers", example = "5.0")
            @RequestParam(defaultValue = "5.0") Double radiusKm,

            @Parameter(description = "Maximum number of recommendations", example = "10")
            @RequestParam(defaultValue = "10") Integer topK,

            @Parameter(description = "Price range preference (1-4)")
            @RequestParam(required = false)
            @Min(1) @Max(4) Integer priceRange,

            @Parameter(description = "Category preferences (comma-separated)", example = "coffee,dessert")
            @RequestParam(required = false) String prefs
    ) {
        log.info("GET /api/v1/recommendations/users/{} - lat: {}, lng: {}, prefs: {}",
                userId, lat, lng, prefs);

        try {
            Map<String, Object> recommendations = recommenderService.getUserRecommendations(
                    userId, lat, lng, radiusKm, topK, priceRange, prefs
            );

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error getting user recommendations", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to get user recommendations",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Check recommender service health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean healthy = recommenderService.isRecommenderHealthy();
        return ResponseEntity.ok(Map.of(
                "recommenderService", healthy ? "healthy" : "unhealthy",
                "timestamp", System.currentTimeMillis()
        ));
    }
}
