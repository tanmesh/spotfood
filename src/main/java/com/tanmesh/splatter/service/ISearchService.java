package com.tanmesh.splatter.service;

import com.tanmesh.splatter.wsRequestModel.SearchData;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.util.Set;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:32
 */
public interface ISearchService {
    Set<UserPostData> getSearchTagsResults(String emailId, SearchData searchData);

    Set<UserPostData> getSearchLocalityResults(String emailId, SearchData searchData);
}
