package com.tanmesh.splatter;

import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.resources.*;
import com.tanmesh.splatter.service.*;
import com.tanmesh.splatter.utils.MongoUtils;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Environment;
import org.mongodb.morphia.Datastore;

import java.util.HashMap;

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
        HashMap<String, String> userToken = new HashMap<>();
        IUserService userService = new UserService(userDAO, userToken);
        TagDAO tagDAO = new TagDAO(ds);
        TagService tagService = new TagService(tagDAO);
        UserResource userResource = new UserResource(userService);
        DebugResource debugResource = new DebugResource(userService);
        TagResource tagResource = new TagResource(tagService);
        AdminResource adminResource = new AdminResource(userService);
        UserPostDAO userPostDAO = new UserPostDAO(ds);
        IUserPostService userpostService = new UserPostService(userPostDAO);
        UserPostResource userPostResource = new UserPostResource(userpostService);

        environment.jersey().register(userResource);
        environment.jersey().register(userPostResource);
        environment.jersey().register(debugResource);
        environment.jersey().register(tagResource);
        environment.jersey().register(adminResource);
        environment.jersey().register(new JsonProcessingExceptionMapper(true));
    }
}
