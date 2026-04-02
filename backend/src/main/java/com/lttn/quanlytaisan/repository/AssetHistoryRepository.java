package com.lttn.quanlytaisan.repository;

import com.lttn.quanlytaisan.model.AssetHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetHistoryRepository extends MongoRepository<AssetHistory, String> {

    List<AssetHistory> findByAssetId(String assetId, Sort sort);

    List<AssetHistory> findByUserId(String userId, Sort sort);
}
