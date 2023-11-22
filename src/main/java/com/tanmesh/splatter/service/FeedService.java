package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.*;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.entity.UserPost;
import com.tanmesh.splatter.entity.feed.Explore;
import com.tanmesh.splatter.entity.feed.Feed;
import com.tanmesh.splatter.wsRequestModel.UserPostData;

import java.util.*;

public class FeedService implements IFeedService {
    private UserDAO userDAO;
    private FeedDAO feedDAO;
    private UserPostDAO userPostDAO;
    private LikedPostDAO likedPostDAO;
    private ExploreDAO exploreDAO;

    public FeedService(UserDAO userDAO, FeedDAO feedDAO, UserPostDAO userPostDAO, LikedPostDAO likedPostDAO, ExploreDAO exploreDAO) {
        this.userDAO = userDAO;
        this.feedDAO = feedDAO;
        this.userPostDAO = userPostDAO;
        this.likedPostDAO = likedPostDAO;
        this.exploreDAO = exploreDAO;
    }

    // Generate Feed for each User
    @Override
    public void generateFeed() {
        feedDAO.deleteAllFeeds();

        List<User> users = userDAO.getAllUser();
        for (User user : users) {
            updateFeed(user.getEmailId());
        }
    }

    @Override
    public void generateExplore() {
        exploreDAO.deleteAllExplores();

        List<User> users = userDAO.getAllUser();
        for (User user : users) {
            updateExplore(user.getEmailId());
        }
    }

    @Override
    public List<UserPostData> getUserFeed(String emailId, int startAfter) {
        List<UserPostData> feed_ = new ArrayList<>();

        List<Feed> feedItems = feedDAO.getFeed(emailId, startAfter, 2);
        for (Feed feedItem : feedItems) {
            UserPost userPost = userPostDAO.getPostFromIds(feedItem.getUserPostId());
            UserPostData userPostData = new UserPostData(userPost, 0);
            userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
            userPostData.setAuthorName(userDAO.getUserName(userPostData.getAuthorEmailId()));
            feed_.add(userPostData);
        }

        return feed_;
    }

    @Override
    public List<UserPostData> getUserExplore(String emailId, int startAfter) {
        List<UserPostData> explore_ = new ArrayList<>();

        List<Explore> exploreItems = exploreDAO.getExplore(emailId, startAfter, 2);

        for (Explore exploreItem : exploreItems) {
            UserPost userPost = userPostDAO.getPostFromIds(exploreItem.getUserPostId());

            UserPostData userPostData = new UserPostData(userPost, 0);
            userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
            userPostData.setAuthorName(userDAO.getUserName(userPostData.getAuthorEmailId()));
            explore_.add(userPostData);
        }

        return explore_;
    }

    private void updateExplore(String emailId) {
        Set<Explore> exploreSet = new HashSet<>();

        if (Objects.equals(emailId, "")) {
            List<UserPost> userPosts = userPostDAO.getAllPost();
            for (UserPost userPost : userPosts) {
                Explore exploreItem = new Explore();
                exploreItem.setUserPostId(userPost.getPostId());
                exploreItem.setCreationTimestamp(userPost.getCreationTimestamp());
                exploreItem.setEmailId(emailId);
                exploreSet.add(exploreItem);
            }
        } else {
            Set<String> following = userDAO.getUserByEmailId(emailId).getFollowingList();
            List<User> allUser = userDAO.getAllUser();
            for (User user : allUser) {
                if (!emailId.equals(user.getEmailId()) && !following.contains(user.getEmailId())) {
                    List<UserPost> userPosts = userPostDAO.getAllPostOfUser(user.getEmailId());
                    for (UserPost userPost : userPosts) {
                        Explore exploreItem = new Explore();
                        exploreItem.setUserPostId(userPost.getPostId());
                        exploreItem.setCreationTimestamp(userPost.getCreationTimestamp());
                        exploreItem.setEmailId(emailId);
                        exploreSet.add(exploreItem);
                    }
                }
            }
        }

//        List<UserPostData> exploreFeed = new ArrayList<>();
//        for (UserPost userPost : exploreFeed_) {
//            UserPostData userPostData = new UserPostData(userPost, 0);
//            userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
//            exploreFeed.add(userPostData);
//        }
//
//        exploreFeed.sort(Comparator.comparingLong(UserPostData::getCreationTimestamp).reversed());

        List<Explore> explores = new ArrayList<>(exploreSet);
        for (Explore explore : explores) {
            exploreDAO.save(explore);
        }
    }

    private void updateFeed(String emailId) {
        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            return;
        }

        Set<Feed> feedSet = new HashSet<>();

        // 1. all posts from followed tags
        Set<Tag> followedTags = user.getTagList();
        if (followedTags != null) {
            for (Tag tag : followedTags) {
                Set<UserPost> postFromTags = userPostDAO.getAllPostForTags(tag);
                if (postFromTags != null) {
                    for (UserPost userPost : postFromTags) {
                        Feed feedItem = new Feed();
                        feedItem.setUserPostId(userPost.getPostId());
                        feedItem.setCreationTimestamp(userPost.getCreationTimestamp());
                        feedItem.setEmailId(emailId);
                        feedSet.add(feedItem);
                    }
                }
            }
        }

        // 2. all posts from followed user;
        Set<String> followings = user.getFollowingList();
        if (followings != null) {
            for (String followingEmailId : followings) {
                List<UserPost> postFromEmailId = userPostDAO.getAllPostOfUser(followingEmailId);
                if (postFromEmailId != null) {
                    for (UserPost userPost : postFromEmailId) {
                        Feed feedItem = new Feed();
                        feedItem.setUserPostId(userPost.getPostId());
                        feedItem.setCreationTimestamp(userPost.getCreationTimestamp());
                        feedItem.setEmailId(emailId);
                        feedSet.add(feedItem);
                    }
                }
            }
        }

        // 3. all post of current user
        List<UserPost> posts = userPostDAO.getAllPostOfUser(emailId);
        for (UserPost userPost : posts) {
            Feed feedItem = new Feed();
            feedItem.setUserPostId(userPost.getPostId());
            feedItem.setCreationTimestamp(userPost.getCreationTimestamp());
            feedItem.setEmailId(emailId);
            feedSet.add(feedItem);
        }

//        Collections.sort(feed, Comparator.comparingLong(UserPostData::getCreationTimestamp).reversed());

        List<Feed> feeds = new ArrayList<>(feedSet);

        for (Feed feed : feeds) {
            feedDAO.save(feed);
        }
    }
}
