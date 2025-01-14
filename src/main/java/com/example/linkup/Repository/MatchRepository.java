package com.example.linkup.Repository;

import com.example.linkup.Entities.match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<match, Integer> {
    Optional<match> findById(Long matchId);
}