package com.tanmesh.spotfood.service;

import com.tanmesh.spotfood.wsRequestModel.UserPostData;

import java.util.List;

public interface IFeedService {
    List<UserPostData> getUserFeed(String emailId, int startAfter);

    void generateFeed();

    List<UserPostData> getUserExplore(String emailId, int startAfter);

    void generateExplore();
}
