package com.lttn.quanlytaisan.repository;

import com.lttn.quanlytaisan.model.AssetRequest;
import com.lttn.quanlytaisan.model.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRequestRepository extends MongoRepository<AssetRequest, String> {

    List<AssetRequest> findByRequestedByUserIdOrderByCreatedAtDesc(String userId);

    Page<AssetRequest> findByRequestedByUserId(String userId, Pageable pageable);

    Page<AssetRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status, Pageable pageable);

    Page<AssetRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByStatus(RequestStatus status);

    long countByRequestedByUserIdAndStatus(String userId, RequestStatus status);
}
