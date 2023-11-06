package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.wsRequestModel.SearchData;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.util.HashSet;
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

    /*
        currently used simple logic
        TODO:
         1. Need to use GeoIndex

     */
    @Override
    public Set<UserPostData> getSearchTagsResults(String emailId, SearchData searchData) {
        Set<UserPostData> feed = new HashSet<>();

        Set<UserPost> feeds = userPostDAO.getByTag(searchData.getTag(), searchData);

        for (UserPost feed_ : feeds) {
            UserPostData userPostData = new UserPostData();
            userPostData.setPostId(feed_.getPostId().toString());
            userPostData.setTagList(feed_.getTagsString());
            userPostData.setUpVotes(feed_.getUpVotes());
            userPostData.setLocationName(feed_.getLocationName());
            userPostData.setAuthorEmailId(feed_.getAuthorEmailId());
            userPostData.setImgUrl(feed_.getImgUrl());
            feed.add(userPostData);
        }

        return feed;
    }

    @Override
    public Set<UserPostData> getSearchLocalityResults(String emailId, SearchData searchData) {
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

        Set<UserPostData> feed = new HashSet<>();
        Set<UserPost> feeds = userPostDAO.getNearBy(searchData);
        for (UserPost feed_ : feeds) {
            UserPostData userPostData = new UserPostData();
            userPostData.setPostId(feed_.getPostId().toString());
            userPostData.setTagList(feed_.getTagsString());
            userPostData.setUpVotes(feed_.getUpVotes());
            userPostData.setLocationName(feed_.getLocationName());
            userPostData.setAuthorEmailId(feed_.getAuthorEmailId());
            userPostData.setImgUrl(feed_.getImgUrl());
            feed.add(userPostData);
        }

        return feed;
    }
}
