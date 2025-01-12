package com.example.linkup.Repository;

import com.example.linkup.Entities.ImageData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface StorageRepository extends CrudRepository<ImageData,Long> {
    Optional<ImageData> findByName(String fileName);

}
