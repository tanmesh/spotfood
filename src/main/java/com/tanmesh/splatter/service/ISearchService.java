package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.entity.search.SearchEntity;

import java.util.Set;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:32
 */
public interface ISearchService {
    Set<UserPost> getNearbySearchResult(String emailId, SearchEntity searchEntity, Boolean near);
}
