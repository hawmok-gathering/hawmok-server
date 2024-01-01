package com.hawmock.api.response;

import com.hawmock.domain.store.Store;
import com.hawmock.domain.store.StoreCategory;

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
