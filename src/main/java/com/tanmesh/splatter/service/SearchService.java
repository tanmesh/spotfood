package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.wsRequestModel.SearchData;

import java.util.Set;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:35
 */
public class SearchService implements ISearchService {
    private IUserService userService;
    private UserPostDAO userPostDAO;

    public SearchService(IUserService userService, UserPostDAO userPostDAO) {
        this.userService = userService;
        this.userPostDAO = userPostDAO;
    }

    // TODO: incomplete
    @Override
    public Set<UserPost> getSearchResult(String emailId, SearchData searchEntity) {

        Set<UserPost> searchFeed;

        return userPostDAO.getNearBy("sweet", 26.803087, 80.891247);
    }

    @Override
    public Set<UserPost> getSearchTagsResults(String emailId, SearchData searchData) {
//        Set<UserPost> userPosts = null;
//        String tagName = searchData.getName();
//        User user = userService.getUserProfile(emailId);
//        if(user != null) {
//            LatLong latlong = user.getLastUpdatedLocation();
//            if (latlong != null) {
//                double latitude = latlong.getCoordinates()[0];
//                double longitude = latlong.getCoordinates()[1];
//                userPosts = userPostDAO.getNearBy(tagName, latitude, longitude);
//            }
//        }
//        return userPosts;
        return null;
    }
}
