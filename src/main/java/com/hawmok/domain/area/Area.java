package com.hawmok.domain.area;

import com.hawmok.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Area extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CityType cityType;

    @Enumerated(EnumType.STRING)
    private LocalType localType;
}
