package com.example.linkup.Repository;

import com.example.linkup.Entities.Tournoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournoiRepository extends JpaRepository<Tournoi, Long> {
}
