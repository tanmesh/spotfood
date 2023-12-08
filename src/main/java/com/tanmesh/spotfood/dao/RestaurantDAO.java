package com.tanmesh.spotfood.dao;

import com.tanmesh.spotfood.entity.Restaurant;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class RestaurantDAO extends BasicDAO<Restaurant, String> {
    public RestaurantDAO(Datastore ds) {
        super(ds);
    }

    public Restaurant get(String restaurantId) {
        return this.getDatastore()
                .createQuery(Restaurant.class)
                .field("id").equal(restaurantId)
                .get();
    }
}
