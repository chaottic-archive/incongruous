package com.chaottic.incongruous.monster;

import lombok.Getter;

public enum Species {
    GRASS_FIRST(1000),
    GRASS_SECOND(1000),
    GRASS_THIRD(1000)
    ;

    @Getter
    private final int exp;

    Species(int exp) {
        this.exp = exp;
    }
}
