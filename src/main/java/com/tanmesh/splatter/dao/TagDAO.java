package com.tanmesh.splatter.dao;

import com.tanmesh.splatter.entity.Tag;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class TagDAO extends BasicDAO<Tag, Integer> {
    public TagDAO(Datastore ds) {
        super(ds);
    }
}