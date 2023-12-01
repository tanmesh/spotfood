package com.tanmesh.spotfood;

import com.tanmesh.spotfood.authentication.AccessTokenAuthenticator;
import com.tanmesh.spotfood.authentication.AccessTokenSecurityProvider;
import com.tanmesh.spotfood.autocomplete.TagTrie;
import com.tanmesh.spotfood.dao.*;
import com.tanmesh.spotfood.resources.*;
import com.tanmesh.spotfood.service.*;
import com.tanmesh.spotfood.utils.MongoUtils;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.mongodb.morphia.Datastore;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class App extends Application<SpotfoodConfiguration> {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.run(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,x-access-token");
        filter.setInitParameter("allowCredentials", "true");
    }

    @Override
    public void initialize(Bootstrap<SpotfoodConfiguration> bootstrap) {

    }

    public void run(SpotfoodConfiguration configuration, Environment environment) throws Exception {
        Datastore ds = MongoUtils.createDatastore(configuration.getDbConfig());

        UserDAO userDAO = new UserDAO(ds);
        TagDAO tagDAO = new TagDAO(ds);
        LikedPostDAO likedPostDAO = new LikedPostDAO(ds);
        UserPostDAO userPostDAO = new UserPostDAO(ds);

        FeedDAO feedDAO = new FeedDAO(ds);
        ExploreDAO exploreDAO = new ExploreDAO(ds);

        IFeedService feedService = new FeedService(userDAO, feedDAO, userPostDAO, likedPostDAO, exploreDAO);

        AccessTokenService accessTokenService = new RedisAccessTokenService();
        IUserService userService = new UserService(userDAO, accessTokenService, feedService);

        TagTrie tagTrie = new TagTrie();

        ISearchService searchService = new SearchService(userPostDAO, feedDAO, exploreDAO);
        ITagService tagService = new TagService(tagDAO, tagTrie);
        IImageService imageService = new ImageService();

//        UserResource userResource = new UserResource(userService, accessTokenService, configuration.getKafkaProducerConfig());
        UserResource userResource = new UserResource(userService, accessTokenService);
        SearchResource searchResource = new SearchResource(searchService);
        DebugResource debugResource = new DebugResource(userService, tagService);
        TagResource tagResource = new TagResource(tagService);
        AdminResource adminResource = new AdminResource(userService, tagService);

        IUserPostService userPostService = new UserPostService(userPostDAO, tagDAO, likedPostDAO, imageService, userService, userDAO, configuration.getAwsConfig(), exploreDAO, feedDAO, feedService);
        UserPostResource userPostResource = new UserPostResource(userPostService, accessTokenService, feedService);
        AccessTokenAuthenticator accessTokenAuthenticator = new AccessTokenAuthenticator(accessTokenService);

        environment.jersey().register(userResource);
        environment.jersey().register(userPostResource);
        environment.jersey().register(debugResource);
        environment.jersey().register(tagResource);
        environment.jersey().register(searchResource);
        environment.jersey().register(adminResource);
        environment.jersey().register(new JsonProcessingExceptionMapper());
        environment.jersey().register(new AccessTokenSecurityProvider<>(accessTokenAuthenticator));
        configureCors(environment);
    }
}
