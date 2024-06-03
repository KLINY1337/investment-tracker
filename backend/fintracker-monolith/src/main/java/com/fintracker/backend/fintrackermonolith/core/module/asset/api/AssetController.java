package com.fintracker.backend.fintrackermonolith.core.module.asset.api;

import com.fintracker.backend.fintrackermonolith.core.module.asset.api.request.CreateAssetRequest;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.request.UpdateAssetByIdRequest;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.CreateAssetResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.DeleteAssetsByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.GetAssetsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.api.response.UpdateAssetByIdResponse;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.service.AssetService;
import com.fintracker.backend.fintrackermonolith.core.util.RequestParamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://5.23.48.222:8080")
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<CreateAssetResponse> createAsset(@RequestBody CreateAssetRequest request) {
        return ResponseEntity.ok(assetService.createAsset(
                request.symbol(),
                request.assetTypeName())
        );
    }

    @GetMapping
    public ResponseEntity<GetAssetsResponse> getAssetsByIds(@RequestParam String idList) {
        return ResponseEntity.ok(assetService.getAssetsByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

    @PutMapping
    public ResponseEntity<UpdateAssetByIdResponse> updateAssetById(@RequestParam Long id, @RequestBody UpdateAssetByIdRequest request) {
        return ResponseEntity.ok(assetService.updateAssetById(
                id,
                request.symbol(),
                request.assetTypeName())
        );
    }

    @DeleteMapping
    ResponseEntity<DeleteAssetsByIdsResponse> deleteAssetsByIds(@RequestParam String idList) {
        return ResponseEntity.ok(assetService.deleteAssetsByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }
}
