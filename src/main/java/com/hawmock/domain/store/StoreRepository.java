package com.hawmock.domain.store;

import com.hawmock.domain.area.CityType;
import com.hawmock.domain.area.LocalType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByAreaCityTypeInOrAreaLocalTypeIn(List<CityType> cityTypes, List<LocalType> localTypes);

}
