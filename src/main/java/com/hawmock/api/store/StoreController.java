package com.hawmock.api.store;

import com.hawmock.api.ApiResponse;
import com.hawmock.api.response.StoreResponseDto;
import com.hawmock.application.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ApiResponse<List<StoreResponseDto>> getStores(
            @ModelAttribute RecommendStoreRequestDto request
    ) {
        List<StoreResponseDto> result = storeService.getStores(request);
        return ApiResponse.success(result);
    }

}
