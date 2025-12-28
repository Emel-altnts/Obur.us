package com.project.Obur.us.controller;

import com.project.Obur.us.dto.PlaceDTO;
import com.project.Obur.us.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PlaceDTO>> getNearbyPlaces(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm) {
        return ResponseEntity.ok(placeService.getNearbyPlaces(latitude, longitude, radiusKm));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaceDTO>> searchPlaces(@RequestParam String query) {
        return ResponseEntity.ok(placeService.searchPlaces(query));
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<PlaceDTO>> getPlacesByCuisine(@PathVariable String cuisine) {
        return ResponseEntity.ok(placeService.getPlacesByCuisine(cuisine));
    }

    @GetMapping("/price-range/{priceRange}")
    public ResponseEntity<List<PlaceDTO>> getPlacesByPriceRange(@PathVariable String priceRange) {
        return ResponseEntity.ok(placeService.getPlacesByPriceRange(priceRange));
    }
}
