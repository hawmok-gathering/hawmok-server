package com.hawmok.domain.area;

import lombok.Getter;

@Getter
public enum LocalType {
    GANGNAM_GU("강남구");

    private final String description;

    LocalType(String description) {
        this.description = description;
    }
}
