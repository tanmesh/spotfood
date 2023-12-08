package com.tanmesh.spotfood.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.mongodb.morphia.utils.IndexType.DESC;

@Getter
@Setter
@Entity(value = "user_posts", noClassnameStored = true)
@Indexes({@Index(fields = @Field(value = "latLong", type = IndexType.GEO2DSPHERE)), @Index(fields = @Field(value = "creationTimestamp", type = DESC)),})
public class UserPost {
    @Id
    private ObjectId postId;
    @Embedded
    private Set<Tag> tagList;
    @Embedded
    private String restaurantId;
    @Property
    private long creationTimestamp;
    private String restaurantName;
    private String authorEmailId;
    private int upVotes;
    private List<String> imgUrl;
    private LatLong latLong;
    private boolean liked;
    private String AuthorName;
    private int distance;

    public UserPost() {
    }

    public Set<String> getTagsString() {
        Set<String> res = new HashSet<>();
        for (Tag tag : tagList) {
            res.add(tag.getName());
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPost userPost = (UserPost) o;
        return Objects.equals(postId, userPost.postId);
    }
}
