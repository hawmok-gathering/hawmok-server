package com.hawmock.domain.store;

import com.hawmock.domain.BaseEntity;
import com.hawmock.domain.area.Area;
import com.hawmock.domain.introduce.Introduce;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    private String name;

    private String storeImageUrl;

    private String phone;

    private String address;

    private int hallCapacity;

    private int roomCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private StoreCategory storeCategory;

    private LocalDateTime weekday_start_time;

    private LocalDateTime weekday_end_time;

    private LocalDateTime weekend_start_time;

    private LocalDateTime weekend_end_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "introduce_id")
    private Introduce introduce;
}
