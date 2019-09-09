package com.tanmesh.splatter.service;

import com.tanmesh.splatter.autocomplete.TagTrie;
import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagService implements ITagService {
    private TagDAO tagDAO;
    private TagTrie tagTrie;

    public TagService(TagDAO tagDAO, TagTrie tagTrie) {
        this.tagDAO = tagDAO;
        this.tagTrie = tagTrie;
    }

    public void addTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagDAO.save(tag);
    }

    public List<Tag> getAllTag() {
        List<String> tagIdList = tagDAO.findIds();
        List<Tag> tagList = new ArrayList<>();
        if (tagIdList == null) {
            return tagList;
        }
        for (String id : tagIdList) {
            tagList.add(tagDAO.get(id));
        }
        return tagList;
    }

    public void deleteTag(String name) {
        Tag tag = tagDAO.getTag("name", name);
        tagDAO.delete(tag);
    }

    public List<String> autocompleteTags(String inputPrefix) {
        return tagTrie.autocomplete(inputPrefix);
    }

    public void insertAllTagForAutocomplete() {
        List<Tag> allTagList = getAllTag();
        for (Tag inputPrefix : allTagList) {
            tagTrie.insert(inputPrefix.getName());
        }
    }

    // TODO: create the existence of the tag.
}
