package com.example.linkup.Repository;

import com.example.linkup.Entities.Academie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademieRepository extends JpaRepository<Academie, Long> {
    Optional<Academie> findById(Long id);

    List<Academie> findAllByIdIn(List<Long> ids);


}
