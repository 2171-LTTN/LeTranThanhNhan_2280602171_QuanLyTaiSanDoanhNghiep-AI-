package com.lttn.quanlytaisan.repository;

import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.AssetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {

    Page<Asset> findByStatus(AssetStatus status, Pageable pageable);

    List<Asset> findByAssignedTo(String userId);

    Page<Asset> findByAssignedTo(String userId, Pageable pageable);
}
