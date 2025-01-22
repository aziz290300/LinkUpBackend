package com.example.linkup.Repository;

import com.example.linkup.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Optional<Match> findById(Long matchId);
}