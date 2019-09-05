package com.tanmesh.splatter.utils;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.tanmesh.splatter.configuration.MongoDBConfig;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukesh.bang on 07/04/15.
 */
public class MongoUtils {

    private static final Base64Variant base64Variant = Base64Variants.MODIFIED_FOR_URL;
    private final static ObjectIdSerializer objectIdSerializer = new ObjectIdSerializer();
    private final static ObjectIdDeserializer objectIdDeserializer = new ObjectIdDeserializer();

    public static ObjectMapper configureObjectMapper(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, objectIdSerializer);
        module.addDeserializer(ObjectId.class, objectIdDeserializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static Datastore createDatastore(MongoDBConfig dbConfig) throws UnknownHostException {
        Morphia morphia = new Morphia();
        ServerAddress addr = new ServerAddress(dbConfig.getHost(), dbConfig.getPort());
        List<MongoCredential> credentialsList = new ArrayList<>();
//        MongoCredential credentia = MongoCredential.createCredential(
////                dbConfig.getUserByEmailId(), dbConfig.getDbName(), dbConfig.getPassword().toCharArray());
////        credentialsList.add(credentia);

        MongoClient client = new MongoClient(addr, credentialsList);
        Datastore datastore = morphia.createDatastore(client, dbConfig.getDbName());
        return datastore;

    }

    public static Datastore createDatastore(Mongo mongo, String dbName) {
        Morphia morphia = new Morphia();

//        new ValidationExtension(morphia);
        return morphia.createDatastore((MongoClient) mongo, dbName);
    }

//    public static Datastore createDatastore(MongoConfiguration mongoConfiguration) throws UnknownHostException {
//
//        List<MongoDBConfig> dbConfig = mongoConfiguration.getReplicaSets();
//        String dbName = mongoConfiguration.getDbName();
//
//        Morphia morphia = new Morphia();
//
////        new ValidationExtension(morphia);
//        final Mongo mongo;
//        List<ServerAddress> addresses = new LinkedList<>();
//        for (MongoDBConfig mongoDBConfig : dbConfig) {
//            addresses.add(new ServerAddress(mongoDBConfig.getHost(), mongoDBConfig.getPort()));
//        }
//
//        List<MongoCredential> credList = new LinkedList<>();
//        for (MongoDBConfig mongoDBConfig : dbConfig) {
//            if(mongoDBConfig.getUserByEmailId() != null) {
//                credList.add(MongoCredential.createMongoCRCredential(mongoDBConfig
//                        .getUserByEmailId(), mongoDBConfig.getDbName(), mongoDBConfig.getPassword().toCharArray()));
//            }
//        }
//
//        if (!credList.isEmpty()) {
//            // create authenticated connection
//            mongo = new MongoClient(addresses, credList);
//        } else {
//            // create unauthenticated connection
//            mongo = new MongoClient(addresses);
//        }
//        mongo.setWriteConcern(WriteConcern.REPLICA_ACKNOWLEDGED);
//        mongo.setReadPreference(ReadPreference.primary());
//
//        return morphia.createDatastore((MongoClient) mongo, dbName);
//    }

    public static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            byte[] byteArray = value.toByteArray();
            jgen.writeBinary(base64Variant, byteArray, 0, byteArray.length);
        }
    }

    public static class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
        @Override
        public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new ObjectId(jp.getBinaryValue(base64Variant));
        }
    }
}
