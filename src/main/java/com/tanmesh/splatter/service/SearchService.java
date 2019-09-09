package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.entity.search.SearchEntity;

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

    @Override
    public Set<UserPost> getNearbySearchResult(String emailId, SearchEntity searchEntity, Boolean near) {
//         only for the tags
//        Set<UserPost> userPosts = null;
//        if (near && searchEntity.getEntityType().equals(SearchEntityType.TAG)) {
//            String tagName = searchEntity.getEntityName();
//            User user = userService.getUserProfile(emailId);
//            if(user != null) {
//                LatLong latlong = user.getLastUpdatedLocation();
//                if (latlong != null) {
//                    Double latitude = latlong.getCoordinates().get(0);
//                    Double longitude = latlong.getCoordinates().get(1);
//                    userPosts = userPostDAO.getNearBy(tagName, latitude, longitude);
//                }
//            }
//        }
//        return userPosts;
        return userPostDAO.getNearBy("chilli", 26.803087, 80.891247);
    }
}
