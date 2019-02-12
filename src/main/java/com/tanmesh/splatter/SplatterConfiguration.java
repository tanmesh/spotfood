package com.tanmesh.splatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanmesh.splatter.configuration.MongoDBConfig;
import io.dropwizard.Configuration;

public class SplatterConfiguration extends Configuration {
    @JsonProperty
    private MongoDBConfig dbConfig;

    public MongoDBConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(MongoDBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

}
