package com.tanmesh.splatter.entity.feed;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Objects;

import static org.mongodb.morphia.utils.IndexType.DESC;

@Entity(value = "feed", noClassnameStored = true)
@Indexes({@Index(fields = @Field(value = "creationTimestamp", type = DESC))})
public class Feed {
    @Id
    private String id;
    private String emailId;
    private ObjectId userPostId;
    @Property
    private long creationTimestamp;

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
        Feed feed = (Feed) o;
        return Objects.equals(userPostId, feed.userPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPostId);
    }
}

