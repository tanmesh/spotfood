package com.tanmesh.splatter.entity.search;

import com.tanmesh.splatter.enums.SearchEntityType;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 12:45
 */
public class SearchEntity {
    @Id
    private int id;
    private String entityName;
    private SearchEntityType entityType;

    public SearchEntity() {
    }

    public SearchEntity(int id, String entityName, SearchEntityType entityType) {
        this.id = id;
        this.entityName = entityName;
        this.entityType = entityType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public SearchEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(SearchEntityType entityType) {
        this.entityType = entityType;
    }
}
