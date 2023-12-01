package com.tanmesh.spotfood.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.base.Preconditions;
import com.tanmesh.spotfood.configuration.AwsConfig;
import com.tanmesh.spotfood.dao.*;
import com.tanmesh.spotfood.entity.LatLong;
import com.tanmesh.spotfood.entity.LikedPost;
import com.tanmesh.spotfood.entity.Tag;
import com.tanmesh.spotfood.entity.UserPost;
import com.tanmesh.spotfood.exception.PostNotFoundException;
import com.tanmesh.spotfood.scrachpad.dummyData.FillDummyData;
import com.tanmesh.spotfood.scrachpad.dummyData.RestaurantInfo;
import com.tanmesh.spotfood.wsRequestModel.UserData;
import com.tanmesh.spotfood.wsRequestModel.UserPostData;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class UserPostService implements IUserPostService {
    private IImageService imageService;
    private IUserService userService;
    private UserPostDAO userPostDAO;
    private TagDAO tagDAO;
    private UserDAO userDAO;
    private LikedPostDAO likedPostDAO;
    private FeedDAO feedDAO;
    private ExploreDAO exploreDAO;

    private IFeedService feedService;
    private AwsConfig awsConfig;

    public UserPostService(UserPostDAO userPostDAO, TagDAO tagDAO, LikedPostDAO likedPostDAO, IImageService imageService, IUserService userService, UserDAO userDAO, AwsConfig awsConfig, ExploreDAO exploreDAO, FeedDAO feedDAO, IFeedService feedService) {
        this.userPostDAO = userPostDAO;
        this.tagDAO = tagDAO;
        this.likedPostDAO = likedPostDAO;
        this.imageService = imageService;
        this.userService = userService;
        this.awsConfig = awsConfig;
        this.userDAO = userDAO;
        this.exploreDAO = exploreDAO;
        this.feedDAO = feedDAO;
        this.feedService = feedService;
    }

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

        feedService.generateFeed();
        feedService.generateExplore();

        // TODO: thumbnail, lowImage, stdImage, original
//        BufferedImage bimg = ImageIO.read(file);
//        String fileExtention = ".jpeg";
//        Image original = imageService.getOriginalImage(bimg, fileExtention);
//        Image thumbnail = imageService.getThumbnail(original, bimg);
//        Image lowImage = imageService.getLowImage(original, bimg);
//        Image stdImage = imageService.getStdImage(original, bimg);
    }

    // TODO: add edit feed feature
    @Override
    public boolean editPost(String postId, List<String> tagList, String location, String authorName) {
        return true;
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
        List<String> imgPaths = fillDummyData.readImages();
        List<RestaurantInfo> restaurantInfos = fillDummyData.getRestaurantInfo();
        List<UserData> users = fillDummyData.getDummyUser();

        for (UserData user : users) {
            userService.signUpUser(user);
        }

        for (int i = 0; i < 20; ++i) {
            String emailId = users.get(i % 4).getEmailId();
            List<String> imgUrl = new ArrayList<>();
            imgUrl.add(setImgUrl(imgPaths.get(i * 3), restaurantInfos.get(i).getName(), i * 3));
            imgUrl.add(setImgUrl(imgPaths.get(i * 3 + 1), restaurantInfos.get(i).getName(), i * 3 + 1));
            imgUrl.add(setImgUrl(imgPaths.get(i * 3 + 2), restaurantInfos.get(i).getName(), i * 3 + 2));

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

        feedService.generateExplore();
        feedService.generateFeed();
    }

    @Override
    public void deletePost(String postId) throws PostNotFoundException {
        UserPost userPost = userPostDAO.getPostFromIds(new ObjectId(postId));
        if (userPost == null) {
            throw new PostNotFoundException("user post is NULL");
        }
        userPostDAO.delete(userPost);

        feedService.generateFeed();
        feedService.generateExplore();
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

            feedService.generateFeed();
            feedService.generateExplore();

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

            feedService.generateFeed();
            feedService.generateExplore();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    private String setImgUrl(String imagePath, String locationName, int index) {
        String bucketName = "spotfood-images";
        String filePath = "location/" + locationName + "_" + index + ".jpg";
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
}
