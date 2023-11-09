package com.tanmesh.splatter.service;

import com.tanmesh.splatter.autocomplete.TagTrie;
import com.tanmesh.splatter.dao.TagDAO;
import com.tanmesh.splatter.entity.Tag;
import com.tanmesh.splatter.wsRequestModel.TagData;

import java.util.ArrayList;
import java.util.List;

public class TagService implements ITagService {
    private TagDAO tagDAO;
    private TagTrie tagTrie;

    public TagService(TagDAO tagDAO, TagTrie tagTrie) {
        this.tagDAO = tagDAO;
        this.tagTrie = tagTrie;

        List<TagData> allTagList = getAllTag();
        for (TagData inputPrefix : allTagList) {
            tagTrie.insert(inputPrefix.getName());
        }
    }

    public void addTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagDAO.save(tag);
        tagTrie.insert(tagName);
    }

    public List<TagData> getAllTag() {
        List<String> tagIdList = tagDAO.findIds();
        List<TagData> outTagList = new ArrayList<>();
        if (tagIdList == null) {
            return outTagList;
        }
        for (String id : tagIdList) {
            outTagList.add(new TagData(tagDAO.get(id).getName()));
        }
        return outTagList;
    }

    public void deleteTag(String name) {
        Tag tag = tagDAO.getTag("name", name);
        tagDAO.delete(tag);
        tagTrie.delete(tag);
    }

    public List<String> autocompleteTags(String inputPrefix) {
        return tagTrie.autocomplete(inputPrefix);
    }
}
