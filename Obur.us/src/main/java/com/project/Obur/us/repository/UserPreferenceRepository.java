package com.project.Obur.us.repository;

import com.project.Obur.us.persistence.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    List<UserPreference> findByUserId(Long userId);

    List<UserPreference> findByUserIdOrderByPreferenceWeightDesc(Long userId);
}
