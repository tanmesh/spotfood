package com.tanmesh.splatter;

import com.tanmesh.splatter.authentication.AccessTokenAuthenticator;
import com.tanmesh.splatter.authentication.AccessTokenSecurityProvider;
import com.tanmesh.splatter.authentication.AuthService;
import com.tanmesh.splatter.authentication.UserSession;
import com.tanmesh.splatter.dao.AuthDAO;
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
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.mongodb.morphia.Datastore;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class App extends Application<SplatterConfiguration> {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.run(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(Bootstrap<SplatterConfiguration> bootstrap) {

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

    public void run(SplatterConfiguration configuration, Environment environment) throws Exception {
        Datastore ds = MongoUtils.createDatastore(configuration.getDbConfig());

        AuthDAO authDAO = new AuthDAO(ds);
        AuthService authService = new AuthService(authDAO);

        TagDAO tagDAO = new TagDAO(ds);
        TagService tagService = new TagService(tagDAO);

        UserPostDAO userPostDAO = new UserPostDAO(ds);
        IUserPostService userPostService = new UserPostService(userPostDAO, tagDAO);
        UserPostResource userPostResource = new UserPostResource(userPostService);

        UserDAO userDAO = new UserDAO(ds);
        IUserService userService = new UserService(userDAO, authService, userPostService);

        AuthResource authResource = new AuthResource(userService, authService);
        UserResource userResource = new UserResource(userService);
        DebugResource debugResource = new DebugResource(userService);
        TagResource tagResource = new TagResource(tagService);
        AdminResource adminResource = new AdminResource(userService, tagService);


        environment.jersey().register(authResource);
        environment.jersey().register(userResource);
        environment.jersey().register(userPostResource);
        environment.jersey().register(debugResource);
        environment.jersey().register(tagResource);
        environment.jersey().register(adminResource);
        environment.jersey().register(new JsonProcessingExceptionMapper());
        environment.jersey().register(new AccessTokenSecurityProvider<UserSession>(new AccessTokenAuthenticator(authService)));

        configureCors(environment);
    }
}
