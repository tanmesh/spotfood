package com.tanmesh.splatter.wsResponseModel;

import com.tanmesh.splatter.entity.Tag;

public class TagResponse {
    private Tag tag;
    private boolean follow;

    public TagResponse() {
    }

    public TagResponse(Tag tag, boolean follow) {
        this.tag = tag;
        this.follow = follow;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
}
