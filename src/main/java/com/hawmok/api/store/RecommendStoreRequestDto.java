package com.hawmok.api.store;

import com.hawmok.domain.area.CityType;
import com.hawmok.domain.area.LocalType;
import lombok.Data;

import java.util.List;

@Data
public class RecommendStoreRequestDto {
    private List<CityType> cityTypes;
    private List<LocalType> localTypes;
}
