package com.hawmock.api.store;

import com.hawmock.domain.area.CityType;
import com.hawmock.domain.area.LocalType;
import lombok.Data;

import java.util.List;

@Data
public class RecommendStoreRequestDto {
    private List<CityType> cityTypes;
    private List<LocalType> localTypes;
}
