package com.tanmesh.splatter.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.mongodb.morphia.utils.IndexType.DESC;

/*
    TODO:
    1. how to add secondary index on `lastTimestamp`
    https://morphia.dev/morphia/1.6/indexing.html
 */
@Entity(value = "user_posts", noClassnameStored = true)
@Indexes({
        @Index(fields = @Field(value = "latLong", type = IndexType.GEO2DSPHERE)),
        @Index(fields = @Field(value = "creationTimestamp", type = DESC)),
})
public class UserPost {
    @Id
    private ObjectId postId;
    @Embedded
    private Set<Tag> tagList;
    @Property
    private long creationTimestamp;
    private String locationName;
    private String authorEmailId;
    private int upVotes;
    private String imgUrl;
    private LatLong latLong;
    private boolean liked;

    private String AuthorName;

    public UserPost() {
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public LatLong getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLong latLong) {
        this.latLong = latLong;
    }

    public ObjectId getPostId() {
        return postId;
    }

    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(Set<Tag> tagList) {
        this.tagList = tagList;
    }

    public Set<String> getTagsString() {
        Set<String> res = new HashSet<>();
        for (Tag tag : tagList) {
            res.add(tag.getName());
        }
        return res;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAuthorEmailId() {
        return authorEmailId;
    }

    public void setAuthorEmailId(String authorEmailId) {
        this.authorEmailId = authorEmailId;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPost userPost = (UserPost) o;
        return Objects.equals(postId, userPost.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
