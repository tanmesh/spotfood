package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.TagData;

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

    public void addTag(TagData tagData) throws InvalidInputException {
        String tagName = tagData.getName();
        if (tagName == null || tagName.length() == 0) {
            throw new InvalidInputException("tagName is empty");
        }
        addTagHelper(tagName);
    }

    private void addTagHelper(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagDAO.save(tag);
    }

    public List<Tag> getAllTag() throws InvalidInputException {
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
