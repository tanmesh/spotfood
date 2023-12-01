package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.wsRequestModel.SearchData;
import com.tanmesh.spotfood.wsRequestModel.UserPostData;

import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:32
 */
public interface ISearchService {
    List<UserPostData> getSearchTagsResults(String emailId, SearchData searchData, int offset);

    List<UserPostData> getSearchLocalityResults(String emailId, SearchData searchData, int offset);
}
