package com.tanmesh.splatter;

import com.tanmesh.splatter.authentication.AccessTokenAuthenticator;
import com.tanmesh.splatter.authentication.AccessTokenSecurityProvider;
import com.tanmesh.splatter.autocomplete.TagTrie;
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
        AccessTokenService accessTokenService = new RedisAccessTokenService();
        IUserService userService = new UserService(userDAO, accessTokenService);
        TagDAO tagDAO = new TagDAO(ds);
        TagTrie tagTrie = new TagTrie();
        UserPostDAO userPostDAO = new com.tanmesh.splatter.dao.UserPostDAO(ds);
        ISearchService searchService = new SearchService(userService, userPostDAO);
        ITagService tagService = new TagService(tagDAO, tagTrie);
        IImageService imageService = new ImageService();
        UserResource userResource = new UserResource(userService, accessTokenService);
        SearchResource searchResource = new SearchResource(searchService);
        DebugResource debugResource = new DebugResource(userService, tagService);
        TagResource tagResource = new TagResource(tagService);
        AdminResource adminResource = new AdminResource(userService, tagService);
        IUserPostService userpostService = new UserPostService(userPostDAO, tagDAO, imageService, userService);
        UserPostResource userPostResource = new UserPostResource(userpostService, accessTokenService);
        AccessTokenAuthenticator accessTokenAuthenticator = new AccessTokenAuthenticator(accessTokenService);

        environment.jersey().register(userResource);
        environment.jersey().register(userPostResource);
        environment.jersey().register(debugResource);
        environment.jersey().register(tagResource);
        environment.jersey().register(searchResource);
        environment.jersey().register(adminResource);
        environment.jersey().register(new JsonProcessingExceptionMapper());
        environment.jersey().register(new AccessTokenSecurityProvider<>(accessTokenAuthenticator));
//        configureCors(environment);
    }
}
