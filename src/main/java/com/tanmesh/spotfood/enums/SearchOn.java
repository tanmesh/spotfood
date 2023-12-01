package com.tanmesh.spotfood.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SearchOn {
    FEED,
    EXPLORE;

    @JsonValue
    public String getName() {
        return this.name();
    }
}