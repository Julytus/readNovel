package com.project.NovelWeb.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum Status {
    UNKNOWN(0),
    ONGOING(1),
    COMPLETE(2),
    SINGLE(3),
    OVA(4),
    ONA(5),
    LICENSED(6),
    EMPTY(7),
    ANNOUNCEMENT(8),
    NOT_RELEASED(9),
    CANCELED(10),
    ON_HOLD(11),
    ANTHOLOGY(12),
    MAGAZINE(13);

    private final int id;

}