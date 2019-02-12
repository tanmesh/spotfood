package com.tanmesh.splatter;

import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.resources.DebugResource;
import com.tanmesh.splatter.resources.UserResource;
import com.tanmesh.splatter.service.UserService;
import com.tanmesh.splatter.utils.MongoUtils;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Environment;
import org.mongodb.morphia.Datastore;

public class App extends Application<SplatterConfiguration> {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(SplatterConfiguration configuration, Environment environment) throws Exception {
        Datastore ds = MongoUtils.createDatastore(configuration.getDbConfig());
        UserDAO userDAO = new UserDAO(ds);
        UserService userService = new UserService(userDAO);
        UserResource userResource = new UserResource(userService);
        DebugResource debugResource = new DebugResource(userService);


        environment.jersey().register(userResource);
        environment.jersey().register(debugResource);
        environment.jersey().register(new JsonProcessingExceptionMapper(true));

    }
}
