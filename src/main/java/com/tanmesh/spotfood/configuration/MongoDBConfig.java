package com.tanmesh.spotfood.configuration;

public class MongoDBConfig {
    private String dbName;
    private int port;
    private String host;
    private String user;
    private String password;

    public MongoDBConfig(String dbName, int port, String host) {
        this.dbName = dbName;
        this.port = port;
        this.host = host;
    }

    public MongoDBConfig() {
    }

    public String getDbName() {
        return dbName;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
