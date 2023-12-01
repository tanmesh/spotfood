package com.tanmesh.spotfood.dao;

import com.tanmesh.spotfood.entity.Tag;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class TagDAO extends BasicDAO<Tag, String> {
    public TagDAO(Datastore ds) {
        super(ds);
    }

    public Tag getTag(String name, String tagName) {
        return this.getDatastore().createQuery(Tag.class).filter(name, tagName).get();
    }
}