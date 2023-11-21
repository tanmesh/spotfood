package com.tanmesh.splatter.service;

import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.util.List;

public interface IFeedService {
    List<UserPostData> getUserFeed(String emailId, int startAfter);

    void generateFeed();

    List<UserPostData> getUserExplore(String emailId, int startAfter);

    void generateExplore();
}
