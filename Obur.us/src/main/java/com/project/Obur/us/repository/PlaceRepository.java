package com.project.Obur.us.repository;

import com.project.Obur.us.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    /**
     * Find places within a radius (in meters) from a given point
     * Returns places ordered by distance
     */
    @Query(value = """
        SELECT p.id, p.name, p.address, p.categories, p.rating_avg, p.rating_count, p.source,
               ST_Distance(
                   p.location_geo,
                   ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
               ) AS distance_m,
               ST_Y(p.location_geo::geometry) AS lat,
               ST_X(p.location_geo::geometry) AS lng
        FROM places p
        WHERE ST_DWithin(
            p.location_geo,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            :radius
        )
        ORDER BY distance_m ASC
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findNearbyPlaces(
            @Param("lng") Double lng,
            @Param("lat") Double lat,
            @Param("radius") Double radius,
            @Param("limit") Integer limit
    );

    /**
     * Search places by name (case-insensitive)
     */
    @Query("SELECT p FROM Place p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Place> searchByName(@Param("name") String name);

    /**
     * Find places by category
     */
    @Query("SELECT p FROM Place p WHERE LOWER(p.categories) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<Place> findByCategory(@Param("category") String category);

    /**
     * Get places within a radius
     */
    @Query(value = """
        SELECT p.*
        FROM places p
        WHERE ST_DWithin(
            p.location_geo,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            :radius
        )
        AND p.rating_avg >= :minRating
        ORDER BY p.rating_avg DESC, p.rating_count DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Place> findTopRatedNearby(
            @Param("lng") Double lng,
            @Param("lat") Double lat,
            @Param("radius") Double radius,
            @Param("minRating") Double minRating,
            @Param("limit") Integer limit
    );
}
