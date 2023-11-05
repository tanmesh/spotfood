package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.wsRequestModel.SearchData;

import java.util.Set;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:32
 */
public interface ISearchService {
    Set<UserPost> getSearchResult(String emailId, SearchData searchEntity);

    Set<UserPost> getSearchTagsResults(String emailId, SearchData searchData);
}
