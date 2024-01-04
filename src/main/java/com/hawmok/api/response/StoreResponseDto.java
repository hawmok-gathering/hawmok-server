package com.hawmok.api.response;

import com.hawmok.domain.store.Store;
import com.hawmok.domain.store.StoreCategory;

public record StoreResponseDto(
        StoreCategory category,
        String storeName,
        int totalPeopleCapacity,
        String location
) {
    public static StoreResponseDto toDto(Store store) {
        return new StoreResponseDto(
                store.getStoreCategory(),
                store.getName(),
                store.totalPeopleCapacity(),
                store.getIntroduce().getStoreLocation()
        );
    }
}
