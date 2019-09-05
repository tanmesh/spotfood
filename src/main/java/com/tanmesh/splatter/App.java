package com.tanmesh.splatter;

import com.tanmesh.splatter.authentication.AccessTokenAuthenticator;
import com.tanmesh.splatter.authentication.AccessTokenSecurityProvider;
import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.resources.*;
import com.tanmesh.splatter.service.*;
import com.tanmesh.splatter.utils.MongoUtils;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.mongodb.morphia.Datastore;

public class App extends Application<SplatterConfiguration> {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.run(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    private void configureCors(Environment environment) {
//        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
//        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
//        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
//        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "*");
//        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "*");
//        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,x-access-token");
//        filter.setInitParameter("allowCredentials", "true");
//    }

    @Override
    public void initialize(Bootstrap<SplatterConfiguration> bootstrap) {

    }

    public void run(SplatterConfiguration configuration, Environment environment) throws Exception {
        Datastore ds = MongoUtils.createDatastore(configuration.getDbConfig());
        UserDAO userDAO = new UserDAO(ds);
        IUserService userService = new UserService(userDAO);
        TagDAO tagDAO = new TagDAO(ds);
        TagService tagService = new TagService(tagDAO);
        AccessTokenService accessTokenService = new RedisAccessTokenService();
        UserResource userResource = new UserResource(userService, accessTokenService);
        DebugResource debugResource = new DebugResource(userService);
        TagResource tagResource = new TagResource(tagService);
        AdminResource adminResource = new AdminResource(userService, tagService);
        UserPostDAO userPostDAO = new UserPostDAO(ds);
        IUserPostService userpostService = new UserPostService(userPostDAO, tagDAO);
        UserPostResource userPostResource = new UserPostResource(userpostService);

        environment.jersey().register(userResource);
        environment.jersey().register(userPostResource);
        environment.jersey().register(debugResource);
        environment.jersey().register(tagResource);
        environment.jersey().register(adminResource);
        environment.jersey().register(new JsonProcessingExceptionMapper());

        AccessTokenAuthenticator accessTokenAuthenticator = new AccessTokenAuthenticator(accessTokenService);

        environment.jersey().register(new AccessTokenSecurityProvider<>(accessTokenAuthenticator));
//        configureCors(environment);
    }
}
