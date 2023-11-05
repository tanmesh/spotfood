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
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.exception.PostNotFoundException;
import com.tanmesh.splatter.scrachpad.dummyData.FillDummyData;
import com.tanmesh.splatter.scrachpad.dummyData.RestaurantInfo;
import com.tanmesh.splatter.wsRequestModel.UserPostData;
import org.mongodb.morphia.Key;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserPostService implements IUserPostService {
    private UserPostDAO userPostDAO;
    private TagDAO tagDAO;
    private LikedPostDAO likedPostDAO;
    private IImageService imageService;
    private IUserService userService;
    private UserDAO userDAO;
    private AwsConfig awsConfig;

    public UserPostService(UserPostDAO userPostDAO, TagDAO tagDAO, LikedPostDAO likedPostDAO, IImageService imageService, IUserService userService, UserDAO userDAO, AwsConfig awsConfig) {
        this.userPostDAO = userPostDAO;
        this.tagDAO = tagDAO;
        this.likedPostDAO = likedPostDAO;
        this.imageService = imageService;
        this.userService = userService;
        this.awsConfig = awsConfig;
        this.userDAO = userDAO;
    }

    @Override
    public void addPost(UserPostData userPostData, String emailId) throws IOException {
        UserPost userPost = new UserPost();

        String locationName = userPostData.getLocationName();
        userPost.setLocationName(locationName);
        userPost.setAuthorEmailId(emailId);
        userPost.setCreationTimestamp(System.currentTimeMillis());

        double[] coordinates = new double[2];
        coordinates[0] = userPostData.getLongitude();
        coordinates[1] = userPostData.getLatitude();
        userPost.setLatLong(new LatLong(coordinates));

        userPost.setImageS3Path(userPostData.getImgUrl());

        Set<Tag> postTags = userPost.getTags();
        if (postTags == null) {
            postTags = new HashSet<>();
        }
        for (String tag : userPostData.getTagList()) {
            postTags.add(new Tag(tag));
        }
        userPost.setTags(postTags);

        for (String tagName : userPostData.getTagList()) {
            Preconditions.checkNotNull(tagName, "tag name should not be null");
            Tag tag = new Tag();
            tag.setName(tagName);
            tagDAO.save(tag);
        }

        userPostDAO.save(userPost);

        // TODO: thumbnail, lowImage, stdImage, original
//        BufferedImage bimg = ImageIO.read(file);
//        String fileExtention = ".jpeg";
//        Image original = imageService.getOriginalImage(bimg, fileExtention);
//        Image thumbnail = imageService.getThumbnail(original, bimg);
//        Image lowImage = imageService.getLowImage(original, bimg);
//        Image stdImage = imageService.getStdImage(original, bimg);
    }

    @Override
    public boolean editPost(String postId, List<String> tagList, String location, String authorName) {
        return true;
    }

    public Set<UserPostData> getUserFeed(String emailId) {
        Set<UserPostData> feed = Sets.newHashSet();

        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            return feed;
        }

        // 1. all posts from followed tags
        Set<Tag> followedTags = user.getTagList();
        if (followedTags != null) {
            for (Tag tag : followedTags) {
                Set<UserPost> postFromTags = userPostDAO.getAllPostForTags(tag);
                if (postFromTags != null) {
                    for (UserPost userPost : postFromTags) {
                        UserPostData userPostData = new UserPostData(
                                userPost.getPostId().toString(),
                                userPost.getTagsString(),
                                userPost.getLocationName(),
                                userPost.getAuthorEmailId(),
                                userPost.getUpVotes(),
                                userPost.getImageS3Path(),
                                userPost.getLatLong().getCoordinates()[0],
                                userPost.getLatLong().getCoordinates()[1],
                                userPost.getCreationTimestamp()
                        );
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed.add(userPostData);
                    }
                }
            }
        }

        // 2. all posts from followed user;
        Set<String> followers = user.getFollowersList();
        if (followers != null) {
            for (String followerEmailId : followers) {
                List<UserPost> postFromEmailId = userPostDAO.getAllPostOfUser(followerEmailId);
                if (postFromEmailId != null) {
                    for (UserPost userPost : postFromEmailId) {
                        UserPostData userPostData = new UserPostData(
                                userPost.getPostId().toString(),
                                userPost.getTagsString(),
                                userPost.getLocationName(),
                                userPost.getAuthorEmailId(),
                                userPost.getUpVotes(),
                                userPost.getImageS3Path(),
                                userPost.getLatLong().getCoordinates()[0],
                                userPost.getLatLong().getCoordinates()[1],
                                userPost.getCreationTimestamp()
                        );
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed.add(userPostData);
                    }
                }
            }
        }

        return feed;
    }

    /*
    TODO: modify Feed logic
     */
    @Override
    public Set<UserPostData> getUserFeed(String emailId, int startAfter) {
        if (startAfter == -1) {
            return getUserFeed(emailId);
        }

        Set<UserPostData> feed_ = Sets.newHashSet();

        User user = userDAO.getUserByEmailId(emailId);
        if (user == null) {
            return feed_;
        }

        // 1. all posts from followed tags
        Set<Tag> followedTags = user.getTagList();
        if (followedTags != null) {
            for (Tag tag : followedTags) {
                Set<UserPost> postFromTags = userPostDAO.getAllPostForTags(tag);
                if (postFromTags != null) {
                    for (UserPost userPost : postFromTags) {
                        UserPostData userPostData = new UserPostData(
                                userPost.getPostId().toString(),
                                userPost.getTagsString(),
                                userPost.getLocationName(),
                                userPost.getAuthorEmailId(),
                                userPost.getUpVotes(),
                                userPost.getImageS3Path(),
                                userPost.getLatLong().getCoordinates()[0],
                                userPost.getLatLong().getCoordinates()[1],
                                userPost.getCreationTimestamp()
                        );
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed_.add(userPostData);
                    }
                }
            }
        }

        // 2. all posts from followed user;
        Set<String> followers = user.getFollowersList();
        if (followers != null) {
            for (String followerEmailId : followers) {
                List<UserPost> postFromEmailId = userPostDAO.getAllPostOfUser(followerEmailId);
                if (postFromEmailId != null) {
                    for (UserPost userPost : postFromEmailId) {
                        UserPostData userPostData = new UserPostData(
                                userPost.getPostId().toString(),
                                userPost.getTagsString(),
                                userPost.getLocationName(),
                                userPost.getAuthorEmailId(),
                                userPost.getUpVotes(),
                                userPost.getImageS3Path(),
                                userPost.getLatLong().getCoordinates()[0],
                                userPost.getLatLong().getCoordinates()[1],
                                userPost.getCreationTimestamp()
                        );
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed_.add(userPostData);
                    }
                }
            }
        }

        List<UserPostData> list = new ArrayList<>(feed_);
        Set<UserPostData> feed = new HashSet<>();
        int i = 0;

        while (startAfter + i < list.size() && i < 2) {
            feed.add(list.get(startAfter + i));
            i++;
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
                        UserPostData userPostData = new UserPostData(
                                userPost.getPostId().toString(),
                                userPost.getTagsString(),
                                userPost.getLocationName(),
                                userPost.getAuthorEmailId(),
                                userPost.getUpVotes(),
                                userPost.getImageS3Path(),
                                userPost.getLatLong().getCoordinates()[0],
                                userPost.getLatLong().getCoordinates()[1],
                                userPost.getCreationTimestamp()
                        );
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
                        UserPostData userPostData = new UserPostData(
                                userPost.getPostId().toString(),
                                userPost.getTagsString(),
                                userPost.getLocationName(),
                                userPost.getAuthorEmailId(),
                                userPost.getUpVotes(),
                                userPost.getImageS3Path(),
                                userPost.getLatLong().getCoordinates()[0],
                                userPost.getLatLong().getCoordinates()[1],
                                userPost.getCreationTimestamp()
                        );
                        userPostData.setLiked(likedPostDAO.exist(emailId, userPost.getPostId()));
                        feed.add(userPostData);
                    }
                }
            }
        }

        return feed;
    }

    public Set<UserPostData> getUserExplore() {
        Set<UserPostData> feed = Sets.newHashSet();

        List<UserPost> userPosts = userPostDAO.getAllPost();

        for (UserPost userPost: userPosts) {
            UserPostData userPostData = new UserPostData();
            userPostData.setImgUrl(userPost.getImageS3Path());
            userPostData.setLocationName(userPost.getLocationName());
            userPostData.setAuthorEmailId(userPost.getAuthorEmailId());
            userPostData.setUpvotes(userPost.getUpVotes());
            feed.add(userPostData);
        }

        return feed;
    }

    @Override
    public Set<UserPostData> getUserExplore(int startAfter) {
        if (startAfter == -1) {
            return getUserExplore();
        }

        Set<UserPostData> feed = Sets.newHashSet();

        List<User> users = userDAO.getAllUser();

        for (User user : users) {
            Set<UserPostData> feed_;

            // 1. all posts from followed tags
            feed_ = addPostsFromFollowedTags(user, user.getEmailId());
            feed.addAll(feed_);

            // 2. all posts from followed user;
            feed_ = addPostFromFollowedUser(user, user.getEmailId());
            feed.addAll(feed_);
        }

        List<UserPostData> list = new ArrayList<>(feed);
        Set<UserPostData> feed_ = new HashSet<>();
        int i = 0;
        while (startAfter + i < list.size() && i < 2) {
            feed.add(list.get(startAfter + i));
            i++;
        }

        return feed_;
    }

    @Override
    public void addDummyPost() throws InvalidInputException, IOException {
        FillDummyData fillDummyData = new FillDummyData();
        List<Set<String>> tags = FillDummyData.getTags();
        List<String> imgPath = FillDummyData.readImages();
        List<RestaurantInfo> restaurantInfos = FillDummyData.getRestaurantInfo();
        List<String> userEmailId = FillDummyData.getEmailId();

        for (int i = 0; i < 20; ++i) {
            String emailId = userEmailId.get(i % 4);
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
        String filePath = "location/" + locationName + ".jpeg";
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
            ImageIO.write(bufferedImage, "jpeg", outputStream);

            // Encode the byte array to base64
            byte[] imageBytes = outputStream.toByteArray();

            InputStream inputStream = new ByteArrayInputStream(imageBytes);

            AWSCredentials credentials = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(awsConfig.getRegion())
                    .build();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, inputStream, new ObjectMetadata());
            s3Client.putObject(putObjectRequest);

            return s3Client.getUrl(bucketName, filePath).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePost(String postId) throws PostNotFoundException {
        UserPost userPost = userPostDAO.getPostFromIds(postId);
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
            UserPost userPost = userPostDAO.getPostFromIds(postId);

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
            UserPost userPost = userPostDAO.getPostFromIds(postId);

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
        UserPost userPost = userPostDAO.getPostFromIds(postId);
        if (userPost == null) {
            throw new PostNotFoundException("user post is NULL");
        }
        return userPost;
    }

    @Override
    public List<UserPost> getAllPostOfUser(String authorEmailId) {
        return userPostDAO.getAllPostOfUser(authorEmailId);
    }
}
