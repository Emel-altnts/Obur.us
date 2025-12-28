package com.project.Obur.us.repository;

import com.project.Obur.us.persistence.entity.Place;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByIsActiveTrueOrderByAverageRatingDesc();

    List<Place> findByCuisineAndIsActiveTrue(String cuisine);

    List<Place> findByPriceRangeAndIsActiveTrue(String priceRange);

    @Query(value = "SELECT p.* FROM places p " +
            "WHERE p.is_active = true " +
            "AND ST_DWithin(p.location_geo, :location, :radiusMeters) " +
            "ORDER BY ST_Distance(p.location_geo, :location)",
            nativeQuery = true)
    List<Place> findNearbyPlaces(
            @Param("location") Point location,
            @Param("radiusMeters") double radiusMeters
    );

    @Query(value = "SELECT p.*, ST_Distance(p.location_geo, :location) as distance " +
            "FROM places p " +
            "WHERE p.is_active = true " +
            "AND p.cuisine = :cuisine " +
            "AND ST_DWithin(p.location_geo, :location, :radiusMeters) " +
            "ORDER BY distance",
            nativeQuery = true)
    List<Place> findNearbyCuisineSpecific(
            @Param("location") Point location,
            @Param("cuisine") String cuisine,
            @Param("radiusMeters") double radiusMeters
    );

    @Query("SELECT p FROM Place p WHERE p.isActive = true " +
            "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Place> searchByName(@Param("searchTerm") String searchTerm);
}
