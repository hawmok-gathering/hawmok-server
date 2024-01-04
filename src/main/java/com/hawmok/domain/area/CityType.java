package com.hawmok.domain.area;

import lombok.Getter;

@Getter
public enum CityType {
    SEOUL("서울시"),
    BUSAN("부산시");

    private final String description;

    CityType(String description) {
        this.description = description;
    }
}
