package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.response.AssetHistoryResponse;
import com.lttn.quanlytaisan.model.AssetAction;
import com.lttn.quanlytaisan.model.AssetHistory;
import com.lttn.quanlytaisan.repository.AssetHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetHistoryService {

    private final AssetHistoryRepository assetHistoryRepository;

    public AssetHistory saveHistory(String assetId, String assetName, String userId, String userName,
                                    String performedBy, AssetAction action, String details) {
        AssetHistory history = AssetHistory.builder()
                .assetId(assetId)
                .userId(userId)
                .performedBy(performedBy)
                .action(action)
                .details(details)
                .build();
        return assetHistoryRepository.save(history);
    }

    public List<AssetHistoryResponse> getAssetHistory(String assetId) {
        List<AssetHistory> histories = assetHistoryRepository
                .findByAssetId(assetId, Sort.by(Sort.Direction.DESC, "timestamp"));

        return histories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AssetHistoryResponse> getUserHistory(String userId) {
        List<AssetHistory> histories = assetHistoryRepository
                .findByUserId(userId, Sort.by(Sort.Direction.DESC, "timestamp"));

        return histories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AssetHistoryResponse mapToResponse(AssetHistory history) {
        return AssetHistoryResponse.builder()
                .id(history.getId())
                .assetId(history.getAssetId())
                .userId(history.getUserId())
                .performedBy(history.getPerformedBy())
                .action(history.getAction())
                .details(history.getDetails())
                .timestamp(history.getTimestamp())
                .build();
    }
}
