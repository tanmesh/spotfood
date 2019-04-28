package com.tanmesh.splatter.service;

import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.exception.InvalidInputException;
import com.tanmesh.splatter.wsRequestModel.TagData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
