package com.example.linkup.Repository;

import com.example.linkup.Entities.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {
}