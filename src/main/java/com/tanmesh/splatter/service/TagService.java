package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.entity.User;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.TagData;
import com.tanmesh.splatter.wsResponseModel.TagResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagService {
    private TagDAO tagDAO;
    private IUserService userService;

    public TagService(TagDAO TagDAO, IUserService userService) {
        this.tagDAO = TagDAO;
        this.userService = userService;
    }

    public TagDAO getTagDAO() {
        return tagDAO;
    }

    public void setTagDAO(TagDAO TagDAO) {
        this.tagDAO = TagDAO;
    }

    // TODO: create the existence of the tag.

    public void addTag(TagData tagData) throws InvalidInputException {
        String tagName = tagData.getName();
        if (tagName == null || tagName.length() == 0) {
            throw new InvalidInputException("tagName is empty");
        }
        addTagHelper(tagName);
    }

    private Tag addTagHelper(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagDAO.save(tag);
        return tag;
    }

    List<Tag> getPreInitializedTags() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(addTagHelper("sweet"));
        tagList.add(addTagHelper("yummy"));
        tagList.add(addTagHelper("dessert"));
        tagList.add(addTagHelper("delicious"));
        tagList.add(addTagHelper("spicy"));
        tagList.add(addTagHelper("vegan"));
        tagList.add(addTagHelper("vegetarian"));
        tagList.add(addTagHelper("non-vegetarian"));
        tagList.add(addTagHelper("protein-rich"));
        tagList.add(addTagHelper("low-carb"));
        tagList.add(addTagHelper("creamy"));
        tagList.add(addTagHelper("crunchy"));
        tagList.add(addTagHelper("soft"));
        tagList.add(addTagHelper("indian"));
        tagList.add(addTagHelper("thai"));
        tagList.add(addTagHelper("chinese"));
        tagList.add(addTagHelper("american"));
        tagList.add(addTagHelper("mexican"));
        tagList.add(addTagHelper("appetizer"));
        tagList.add(addTagHelper("beverage"));
        tagList.add(addTagHelper("snacks"));
        tagList.add(addTagHelper("healthy"));
        return tagList;
    }

    public Set<Tag> getAllTag() throws InvalidInputException {
        Set<Tag> tagList = new HashSet<>();
        List<Tag> initTags = getPreInitializedTags();
        for (Tag tag : initTags) {
            tagList.add(tag);
        }
        List<String> tagIdList = tagDAO.findIds();
        if ((tagIdList != null) && (tagIdList.size() > 0)) {
            for (String id : tagIdList) {
                tagList.add(tagDAO.get(id));
            }
        }
        return tagList;
    }

    public Set<TagResponse> getAllTag(String userId) throws InvalidInputException {

        Set<Tag> tagList = new HashSet<>();
        List<Tag> initTags = getPreInitializedTags();
        for (Tag tag : initTags) {
            tagList.add(tag);
        }
        List<String> tagIdList = tagDAO.findIds();
        if ((tagIdList != null) && (tagIdList.size() > 0)) {
            for (String id : tagIdList) {
                tagList.add(tagDAO.get(id));
            }
        }

        User user = userService.getUser(userId);
        if (user == null) {
            throw new InvalidInputException("Invalid user Id");
        }

        Set<TagResponse> tagResponses = new HashSet<>();
        for (Tag tag : tagList) {
            boolean follow = user.isFollowingTag(tag);
            TagResponse tagResponse = new TagResponse(tag, follow);
            tagResponses.add(tagResponse);
        }
        return tagResponses;
    }

    public void deleteTag(String name) throws InvalidInputException {
        sanityCheck(name, "name");
        Tag tag = tagDAO.getTag("name", name);
        tagDAO.delete(tag);
    }

    private void sanityCheck(String id, String msg) throws InvalidInputException {
        if (id == null || id.length() == 0) {
            throw new InvalidInputException(msg + " is NULL");
        }
    }
}
