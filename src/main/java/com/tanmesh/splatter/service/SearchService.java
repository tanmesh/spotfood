package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.ExploreDAO;
import com.tanmesh.splatter.dao.FeedDAO;
import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.wsRequestModel.SearchData;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tanmesh
 * Date: 2019-09-09
 * Time: 11:35
 */
public class SearchService implements ISearchService {
    private UserPostDAO userPostDAO;
    private FeedDAO feedDAO;
    private ExploreDAO exploreDAO;
    private List<UserPostData> outFeed = new ArrayList<>();

    public SearchService(UserPostDAO userPostDAO, FeedDAO feedDAO, ExploreDAO exploreDAO) {
        this.userPostDAO = userPostDAO;
        this.feedDAO = feedDAO;
        this.exploreDAO = exploreDAO;
    }

    /*
        currently used simple logic
        TODO:
         1. Need to use GeoIndex

     */
    @Override
    public List<UserPostData> getSearchTagsResults(String emailId, SearchData searchData, int offset) {
        if (offset == 0 || outFeed.isEmpty()) {
            setFeedSearchTagsResults(emailId, searchData);
        }

        int itemsToAdd = 2;
        List<UserPostData> feed = new ArrayList<>();
        if (offset >= 0 && offset < outFeed.size()) {
            feed.addAll(outFeed.subList(offset, offset + itemsToAdd));
        }

        return feed;
    }

    @Override
    public List<UserPostData> getSearchLocalityResults(String emailId, SearchData searchData, int offset) {
        if (offset == 0 || outFeed.isEmpty()) {
            setFeedSearchLocalityResults(emailId, searchData);
        }

        int itemsToAdd = 2;
        List<UserPostData> feed = new ArrayList<>();
        if (offset >= 0 && offset < outFeed.size()) {
            feed.addAll(outFeed.subList(offset, offset + itemsToAdd));
        }

        return feed;
    }

    private void setFeedSearchLocalityResults(String emailId, SearchData searchData) {
        Set<UserPostData> feed = new HashSet<>();

        Set<UserPost> feeds = new HashSet<>();
        switch (searchData.getSearchOn()) {
            case FEED:
                feeds = feedDAO.getNearBy(emailId, searchData);
                break;
            case EXPLORE:
                feeds = exploreDAO.getNearBy(emailId, searchData);
                break;
        }

        for (UserPost feed_ : feeds) {
            UserPostData userPostData = new UserPostData();
            userPostData.setPostId(feed_.getPostId().toString());
            userPostData.setTagList(feed_.getTagsString());
            userPostData.setUpVotes(feed_.getUpVotes());
            userPostData.setLocationName(feed_.getLocationName());
            userPostData.setAuthorEmailId(feed_.getAuthorEmailId());
            userPostData.setAuthorName(feed_.getAuthorName());
            userPostData.setImgUrl(feed_.getImgUrl());
            userPostData.setDistance(feed_.getDistance());
            feed.add(userPostData);
        }

        outFeed = feed.stream().collect(Collectors.toList());
        Comparator<UserPostData> distanceComparator = Comparator.comparing(UserPostData::getDistance);
        outFeed.sort(distanceComparator);
    }

    private void setFeedSearchTagsResults(String emailId, SearchData searchData) {
        Set<UserPostData> feed = new HashSet<>();

        for (String tag : searchData.getTag()) {
            Set<UserPost> feeds = new HashSet<>();

            switch (searchData.getSearchOn()) {
                case FEED:
                    feeds = feedDAO.getByTag(emailId, tag, searchData);
                    break;
                case EXPLORE:
                    feeds = exploreDAO.getByTag(emailId, tag, searchData);
                    break;
            }

            for (UserPost feed_ : feeds) {
                UserPostData userPostData = new UserPostData();
                userPostData.setPostId(feed_.getPostId().toString());
                userPostData.setTagList(feed_.getTagsString());
                userPostData.setUpVotes(feed_.getUpVotes());
                userPostData.setLocationName(feed_.getLocationName());
                userPostData.setAuthorEmailId(feed_.getAuthorEmailId());
                userPostData.setAuthorName(feed_.getAuthorName());
                userPostData.setImgUrl(feed_.getImgUrl());
                feed.add(userPostData);
            }
        }

        outFeed = feed.stream().collect(Collectors.toList());
        Comparator<UserPostData> distanceComparator = Comparator.comparing(UserPostData::getCreationTimestamp);
        outFeed.sort(distanceComparator);
    }
}
