package com.tanmesh.spotfood.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 12:47
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SearchType {
    TAG,
    LOCALITY,
    CITY,
    STATE,
    USER_NAME,
    BUSINESS_ENTITY_NAME;

    @JsonValue
    public String getName() {
        return this.name();
    }
}
