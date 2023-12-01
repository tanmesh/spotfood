package com.tanmesh.spotfood.entity.feed;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Objects;

import static org.mongodb.morphia.utils.IndexType.DESC;

@Entity(value = "explore", noClassnameStored = true)
@Indexes({@Index(fields = @Field(value = "creationTimestamp", type = DESC))})
public class Explore {
    @Id
    private String id;
    private String emailId;

    private ObjectId userPostId;
    @Property
    private long creationTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public ObjectId getUserPostId() {
        return userPostId;
    }

    public void setUserPostId(ObjectId userPostId) {
        this.userPostId = userPostId;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Explore explore = (Explore) o;
        return Objects.equals(userPostId, explore.userPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPostId);
    }
}
