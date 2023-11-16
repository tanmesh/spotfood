package com.tanmesh.splatter.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.tanmesh.splatter.configuration.AwsConfig;
import com.tanmesh.splatter.dao.LikedPostDAO;
import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.dao.UserDAO;
import com.tanmesh.splatter.dao.UserPostDAO;
import com.tanmesh.splatter.entity.*;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.scrachpad.dummyData.FillDummyData;
import com.tanmesh.splatter.scrachpad.dummyData.RestaurantInfo;
import com.tanmesh.splatter.wsRequestModel.UserData;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class UserPostService implements IUserPostService {
    private UserPostDAO userPostDAO;
    private TagDAO tagDAO;
    private LikedPostDAO likedPostDAO;
    private IImageService imageService;
    private IUserService userService;
    private UserDAO userDAO;
    private AwsConfig awsConfig;
    private Map<String, List<UserPostData>> feed = new HashMap<>();
    private Map<String, List<UserPostData>> exploreFeed = new HashMap<>();

    public UserPostService(UserPostDAO userPostDAO, TagDAO tagDAO, LikedPostDAO likedPostDAO, IImageService imageService, IUserService userService, UserDAO userDAO, AwsConfig awsConfig) {
        this.userPostDAO = userPostDAO;
        this.tagDAO = tagDAO;
        this.likedPostDAO = likedPostDAO;
        this.imageService = imageService;
        this.userService = userService;
        this.awsConfig = awsConfig;
        this.userDAO = userDAO;
    }

//    public clearExploreFeed(String emailId) {
//        try {
//            exploreFeed.get(emailId).clear();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

    @Override
    public void addPost(UserPostData userPostData, String emailId) {
        UserPost userPost = new UserPost();

        String locationName = userPostData.getLocationName();
        userPost.setLocationName(locationName);
        userPost.setAuthorEmailId(emailId);
        userPost.setAuthorName(userDAO.getUserName(emailId));
        userPost.setCreationTimestamp(System.currentTimeMillis());

        double[] coordinates = new double[2];
        coordinates[0] = userPostData.getLongitude();
        coordinates[1] = userPostData.getLatitude();
        userPost.setLatLong(new LatLong(coordinates));

        userPost.setImgUrl(userPostData.getImgUrl());

        Set<Tag> postTags = userPost.getTagList();
        if (postTags == null) {
            postTags = new HashSet<>();
        }
        for (String tag : userPostData.getTagList()) {
            postTags.add(new Tag(tag));
        }
        userPost.setTagList(postTags);

        for (String tagName : userPostData.getTagList()) {
            Preconditions.checkNotNull(tagName, "tag name should not be null");
            Tag tag = new Tag();
            tag.setName(tagName);
            tagDAO.save(tag);
        }

        userPostDAO.save(userPost);

        try {
            feed.get(emailId).clear();
        } catch (Exception e) {
            System.out.println(e);
        }

        // TODO: thumbnail, lowImage, stdImage, original
//        BufferedImage bimg = ImageIO.read(file);
//        String fileExtention = ".jpeg";
//        Image original = imageService.getOriginalImage(bimg, fileExtention);
//        Image thumbnail = imageService.getThumbnail(original, bimg);
//        Image lowImage = imageService.getLowImage(original, bimg);
//        Image stdImage = imageService.getStdImage(original, bimg);
    }

    // TODO:
    @Override
    public boolean editPost(String postId, List<String> tagList, String location, String authorName) {
        return true;
    }

    private List<UserPostData> getFeed(String emailId) {
        List<UserPostData> feed = new ArrayList<>();

        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            return null;
        }

        // 1. all posts from followed tags
        Set<Tag> followedTags = user.getTagList();
        if (followedTags != null) {
            for (Tag tag : followedTags) {
                Set<UserPost> postFromTags = userPostDAO.getAllPostForTags(tag);
                if (postFromTags != null) {
                    for (UserPost userPost : postFromTags) {
                        boolean postIdExists = feed.stream()
                                .anyMatch(postData -> Objects.equals(postData.getPostId(), userPost.getPostId().toString()));

                        if (!postIdExists) {
                            UserPostData userPostData = new UserPostData(userPost, 0);
                            userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                            userPostData.setAuthorName(userDAO.getUserName(userPostData.getAuthorEmailId()));
                            feed.add(userPostData);
                        }
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
                        boolean postIdExists = feed.stream().anyMatch(postData -> Objects.equals(postData.getPostId(), userPost.getPostId().toString()));

                        if (!postIdExists) {
                            UserPostData userPostData = new UserPostData(userPost, 0);
                            userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                            userPostData.setAuthorName(userDAO.getUserName(userPostData.getAuthorEmailId()));
                            feed.add(userPostData);
                        }
                    }
                }
            }
        }

        // 3. all post of current user
        List<UserPost> posts = userPostDAO.getAllPostOfUser(emailId);
        for (UserPost userPost : posts) {
            boolean postIdExists = feed.stream().anyMatch(postData -> Objects.equals(postData.getPostId(), userPost.getPostId().toString()));

            if (!postIdExists) {
                UserPostData userPostData = new UserPostData(userPost, 0);
                userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                userPostData.setAuthorName(userDAO.getUserName(userPostData.getAuthorEmailId()));
                feed.add(userPostData);
            }
        }

        Collections.sort(feed, Comparator.comparingLong(UserPostData::getCreationTimestamp).reversed());

        return feed;
    }

    @Override
    public List<UserPostData> getUserFeed(String emailId, int startAfter) {
//        if(feed.get(emailId) == null) {
//            feed.put(emailId, getFeed(emailId));
//        }

        feed.put(emailId, getFeed(emailId)); // Temporary fix
        List<UserPostData> feed_ = feed.get(emailId);
        List<UserPostData> feed = new ArrayList<>();
        int itemsToAdd = Math.min(2, feed_.size() - startAfter);
        if (startAfter >= 0 && startAfter < feed_.size()) {
            feed.addAll(feed_.subList(startAfter, startAfter + itemsToAdd));
        }

        return feed;
    }

    private Set<UserPostData> addPostsFromFollowedTags(User user, String emailId) {
        Set<UserPostData> feed = Sets.newHashSet();

        Set<Tag> followedTags = user.getTagList();
        if (followedTags != null) {
            for (Tag tag : followedTags) {
                Set<UserPost> postFromTags = userPostDAO.getAllPostForTags(tag);
                if (postFromTags != null) {
                    for (UserPost userPost : postFromTags) {
                        UserPostData userPostData = new UserPostData(userPost, 0);
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed.add(userPostData);
                    }
                }
            }
        }

        return feed;
    }

    private Set<UserPostData> addPostFromFollowedUser(User user, String emailId) {
        Set<UserPostData> feed = new HashSet<>();

        Set<String> followers = user.getFollowersList();
        if (followers != null) {
            for (String followerEmailId : followers) {
                List<UserPost> postFromEmailId = userPostDAO.getAllPostOfUser(followerEmailId);
                if (postFromEmailId != null) {
                    for (UserPost userPost : postFromEmailId) {
                        UserPostData userPostData = new UserPostData(userPost, 0);
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed.add(userPostData);
                    }
                }
            }
        }

        return feed;
    }

    /**
     * Pick all post except the current user post.
     * <p>
     * emailId can be null
     */
    private List<UserPostData> getExploreFeed(String emailId) {
        List<UserPost> exploreFeed_ = new ArrayList<>();

        if (Objects.equals(emailId, "")) {
            exploreFeed_ = userPostDAO.getAllPost();
        } else {
            Set<String> following = userDAO.getUserByEmailId(emailId).getFollowingList();
            for(User user: userDAO.getAllUserExcept(emailId)) {
                if(!following.contains(user.getEmailId())) {
                    exploreFeed_.addAll(userPostDAO.getAllPostOfUser(user.getEmailId()));
                }
            }
        }

        List<UserPostData> exploreFeed = new ArrayList<>();
        for(UserPost userPost: exploreFeed_) {
            UserPostData userPostData = new UserPostData(userPost, 0);
            userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
            exploreFeed.add(userPostData);
        }

        exploreFeed.sort(Comparator.comparingLong(UserPostData::getCreationTimestamp).reversed());

        return exploreFeed;
    }

    @Override
    public List<UserPostData> getUserExplore(int startAfter, String emailId) {
//        if(exploreFeed.get(emailId) == null) {
//            exploreFeed.put(emailId, getExploreFeed(emailId));
//        }
        exploreFeed.put(emailId, getExploreFeed(emailId)); // Temporary fix

        List<UserPostData> exploreFeed_ = exploreFeed.get(emailId);
        List<UserPostData> exploreFeed = new ArrayList<>();
        int itemsToAdd = Math.min(2, exploreFeed_.size() - startAfter);
        if (startAfter >= 0 && startAfter < exploreFeed_.size()) {
            exploreFeed.addAll(exploreFeed_.subList(startAfter, startAfter + itemsToAdd));
        }

        return exploreFeed;
    }

    @Override
    public List<UserPostData> getAllPostOfUser(String authorEmailId, int startAfter) {
        List<UserPost> userPosts = userPostDAO.getAllPostOfUser(authorEmailId, startAfter, 2);

        List<UserPostData> userPostDataList = new ArrayList<>();

        for (UserPost userPost : userPosts) {
            userPostDataList.add(new UserPostData(userPost, 0));
        }
        return userPostDataList;
    }

    @Override
    public void addDummyPost() throws IOException {
        FillDummyData fillDummyData = new FillDummyData();
        List<Set<String>> tags = fillDummyData.getTags();
        List<String> imgPath = fillDummyData.readImages();
        List<RestaurantInfo> restaurantInfos = fillDummyData.getRestaurantInfo();
        List<UserData> users = fillDummyData.getDummyUser();

        for (UserData user : users) {
            userService.signUpUser(user);
        }

        for (int i = 0; i < 20; ++i) {
            String emailId = users.get(i % 4).getEmailId();
            String imgUrl = setImgUrl(imgPath.get(i), restaurantInfos.get(i).getName());

            UserPostData userPostData = new UserPostData();
            userPostData.setAuthorEmailId(emailId);
            userPostData.setTagList(tags.get(i));
            userPostData.setLatitude(restaurantInfos.get(i).getLatitude());
            userPostData.setLongitude(restaurantInfos.get(i).getLongitude());
            userPostData.setLocationName(restaurantInfos.get(i).getName());
            userPostData.setImgUrl(imgUrl);

            addPost(userPostData, emailId);

            System.out.println("user post " + i + " added.");
        }
    }

    private String setImgUrl(String imagePath, String locationName) {
        String bucketName = "spotfood-images";
        String filePath = "location/" + locationName + ".jpg";
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                throw new RuntimeException("Image file not found at the specified path: " + imagePath);
            }

            BufferedImage bufferedImage = ImageIO.read(imageFile);
            if (bufferedImage == null) {
                throw new RuntimeException("Failed to read the image file.");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);

            // Encode the byte array to base64
            byte[] imageBytes = outputStream.toByteArray();

            InputStream inputStream = new ByteArrayInputStream(imageBytes);

            AWSCredentials credentials = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(awsConfig.getRegion()).build();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, inputStream, new ObjectMetadata());
            s3Client.putObject(putObjectRequest);

            return s3Client.getUrl(bucketName, filePath).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePost(String postId) throws PostNotFoundException {
        UserPost userPost = userPostDAO.getPostFromIds(new ObjectId(postId));
        if (userPost == null) {
            throw new PostNotFoundException("user post is NULL");
        }
        userPostDAO.delete(userPost);
    }

    /*
        TODO
        1. fix this. don't increase if already liked the post

     */
    @Override
    public UserPost likePost(String emailId, String postId) throws PostNotFoundException {
        try {
            UserPost userPost = userPostDAO.getPostFromIds(new ObjectId(postId));

            // does Post exist?
            if (userPost == null) {
                throw new PostNotFoundException("user post is NULL");
            }

            // is already Liked?
            if (likedPostDAO.exist(emailId, userPost.getPostId())) {
                System.out.println("Post is already liked.");
                return userPost;
            }

            /* not Liked yet
                1. upvote in UserPost
                2. add in LikedPost
             */
            int prevCnt = userPost.getUpVotes();
            int updatedCnt = prevCnt + 1;
            userPost.setUpVotes(updatedCnt);
            Key<UserPost> key = userPostDAO.save(userPost);
            if (key == null) {
                System.out.println("couldn't do");
            }
            LikedPost likedPost = new LikedPost(userPost.getPostId(), emailId);
            likedPostDAO.save(likedPost);

            return userPost;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void unlikePost(String emailId, String postId) throws PostNotFoundException {
        try {
            UserPost userPost = userPostDAO.getPostFromIds(new ObjectId(postId));

            // does Post exist?
            if (userPost == null) {
                throw new PostNotFoundException("user post is NULL");
            }

            likedPostDAO.dislikedPost(emailId, userPost.getPostId());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public UserPost getPost(String postId) throws PostNotFoundException {
        UserPost userPost = userPostDAO.getPostFromIds(new ObjectId(postId));
        if (userPost == null) {
            throw new PostNotFoundException("user post is NULL");
        }
        return userPost;
    }

}
