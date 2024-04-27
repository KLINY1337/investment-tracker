package com.fintracker.backend.fintrackermonolith.core.module.asset.model.service;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.repository.AssetRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.CreateAssetResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.DeleteAssetsByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.GetAssetsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.UpdateAssetByIdResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetIdsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public CreateAssetResponse createAsset(String symbol, String assetTypeName) {
        if (assetRepository.existsBySymbolAndAssetTypeName(symbol, assetTypeName)) {
            throw new AssetAlreadyExistsException("Specified asset already exists");
        }

        Asset asset = Asset.builder()
                .symbol(symbol)
                .assetTypeName(assetTypeName)
                .build();

        return new CreateAssetResponse(
                true,
                "Asset successfully created",
                assetRepository.save(asset)
        );
    }

    public GetAssetsResponse getAssetsByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(assetRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new AssetIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        return new GetAssetsResponse(
                true,
                "Specified assets retrieved from database",
                assetRepository.findAllById(processableAndUnprocessableIds.get(true))
        );
    }

    public UpdateAssetByIdResponse updateAssetById(Long id, String symbol, String assetTypeName) {
        if (assetRepository.existsBySymbolAndAssetTypeName(symbol, assetTypeName)) {
            throw new AssetAlreadyExistsException("Specified asset already exists");
        }
        Asset asset = assetRepository
                .findById(id)
                .orElseThrow(() -> new AssetIdsNotFoundException("Specified asset does not exist in database", List.of(id)));

        asset.setSymbol(symbol);
        asset.setAssetTypeName(assetTypeName);

        return new UpdateAssetByIdResponse(
                true,
                "Asset successfully updated",
                asset
        );
    }

    public DeleteAssetsByIdsResponse deleteAssetsByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(assetRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new AssetIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        assetRepository.deleteAllById(processableAndUnprocessableIds.get(true));
        return new DeleteAssetsByIdsResponse(
                true,
                "Specified assets deleted from database",
                processableAndUnprocessableIds.get(true).size()
        );
    }
}
