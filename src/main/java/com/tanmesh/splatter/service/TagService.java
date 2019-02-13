package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.TagData;
import org.mongodb.morphia.Key;

import java.util.ArrayList;
import java.util.List;

public class TagService {
    private TagDAO tagDAO;

    public TagService(TagDAO TagDAO) {
        this.tagDAO = TagDAO;
    }

    public TagDAO getTagDAO() {
        return tagDAO;
    }

    public void setTagDAO(TagDAO TagDAO) {
        this.tagDAO = TagDAO;
    }

    // TODO: create the existence of the tag.

    public boolean addTag(TagData tagData) throws InvalidInputException {
        String tagName = tagData.getTagName();
        if (tagName == null || tagName.length() == 0) {
            throw new InvalidInputException("tagName is empty");
        }
        return addTagHelper(tagName);
    }

    private boolean addTagHelper(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        Key<Tag> tagKey = tagDAO.save(tag);
        return tagKey != null;
    }

    public List<Tag> getAllTag(TagData tagData) throws InvalidInputException {
        List<String> tagIdList = tagDAO.findIds();
        List<Tag> tagList = new ArrayList<>();
        if (tagIdList == null || tagIdList.size() == 0) {
            throw new InvalidInputException("tagIdList is NULL");
        }
        for (String id : tagIdList) {
            tagList.add(tagDAO.get(id));
        }
        return tagList;
    }
}
