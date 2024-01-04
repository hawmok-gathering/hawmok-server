package com.hawmok.application;

import com.hawmok.api.response.StoreResponseDto;
import com.hawmok.api.store.RecommendStoreRequestDto;
import com.hawmok.domain.store.Store;
import com.hawmok.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreResponseDto> getStores(RecommendStoreRequestDto request) {
        List<Store> stores = storeRepository.findByAreaCityTypeInOrAreaLocalTypeIn(request.getCityTypes(), request.getLocalTypes());

        return stores.stream()
                .map(StoreResponseDto::toDto)
                .toList();
    }
}
