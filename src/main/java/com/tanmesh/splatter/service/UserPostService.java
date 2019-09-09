package com.tanmesh.splatter.service;

        import com.google.common.base.Preconditions;
        import com.google.common.collect.Sets;
        import com.tanmesh.splatter.dao.TagDAO;
        import com.tanmesh.splatter.dao.UserPostDAO;
        import com.tanmesh.splatter.entity.*;
        import com.tanmesh.splatter.exception.PostNotFoundException;
        import com.tanmesh.splatter.wsRequestModel.UserPostData;
        import org.mongodb.morphia.Key;

        import javax.imageio.ImageIO;
        import java.awt.image.BufferedImage;
        import java.io.*;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Set;

public class UserPostService implements IUserPostService {
    private UserPostDAO userPostDAO;
    private TagDAO tagDAO;
    private IImageService imageService;
    private IUserService userService;

    public UserPostService(UserPostDAO userPostDAO, TagDAO tagDAO, IImageService imageService, IUserService userService) {
        this.userPostDAO = userPostDAO;
        this.tagDAO = tagDAO;
        this.imageService = imageService;
        this.userService = userService;
    }

    @Override
    public void addPost(UserPostData userPostData, String emailId) throws IOException {
        UserPost userPost = new UserPost();
        userPost.setLocationName(userPostData.getLocationName());
        userPost.setAuthorEmailId(emailId);
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(userPostData.getLatitude());
        coordinates.add(userPostData.getLongitude());
        userPost.setLatLong(new LatLong(coordinates));
        userPost.setEncodedImgString(userPostData.getEncodedImgString());
        List<String> postTags = userPost.getTags();
        if (postTags == null) {
            postTags = new ArrayList<>();
        }
        postTags.addAll(userPostData.getTags());
        userPost.setTags(postTags);

        for (String tagName : userPostData.getTags()) {
            Preconditions.checkNotNull(tagName, "tag name should not be null");
            Tag tag = new Tag();
            tag.setName(tagName);
            tagDAO.save(tag);
        }

        userPostDAO.save(userPost);

        String data = userPostData.getEncodedImgString();
        String base64Image = data.split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        String filePath = "/Users/tanmesh/Downloads/test_image.jpeg";
        File file = new File(filePath);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage bimg = ImageIO.read(file);

        String fileExtention = ".jpeg";
        Image original = imageService.getOriginalImage(bimg, fileExtention);
        Image thumbnail = imageService.getThumbnail(original, bimg);
        Image lowImage = imageService.getLowImage(original, bimg);
        Image stdImage = imageService.getStdImage(original, bimg);
    }

    @Override
    public boolean editPost(String postId, List<String> tagList, String location, String authorName) {
        return true;
    }

    @Override
    public Set<UserPost> getUserFeed(String emailId) {
        // 1. all posts from followed tags
        User user = userService.getUserProfile(emailId);
        if (user == null) {
            return Sets.newHashSet();
        }
        Set<String> followedTags = user.getFollowTagList();

        Set<UserPost> userPosts = userPostDAO.getAllPostForTags(followedTags);

        // 2. all posts from followed users
        return userPosts;
    }

    @Override
    public void deletePost(String emailId) {
        UserPost userPost = userPostDAO.getPost("postId", emailId); // emailId
        userPostDAO.delete(userPost);
    }

    @Override
    public UserPost likePost(String emailId) throws PostNotFoundException {
        UserPost userPost = userPostDAO.getPost("postId", emailId);  // emailId
        if (userPost == null) {
            throw new PostNotFoundException("user post is NULL");
        }
        int prevCnt = userPost.getUpVotes();
        int updatedCnt = prevCnt + 1;
        userPost.setUpVotes(updatedCnt);
        Key<UserPost> key = userPostDAO.save(userPost);
        if (key == null) {
            System.out.println("couldn't do");
        }
        return userPost;
    }

    @Override
    public UserPost getPost(String emailId) {
        return userPostDAO.getPost("authorEmailId", emailId);
    }

    @Override
    public List<UserPost> getAllPostOfUser(String authorEmailId) {
        return userPostDAO.getAllPostOfUser(authorEmailId);
    }
}
