package com.example.linkup.Repository;

import com.example.linkup.Entities.Academie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademieRepository extends JpaRepository<Academie, Long> {
}
