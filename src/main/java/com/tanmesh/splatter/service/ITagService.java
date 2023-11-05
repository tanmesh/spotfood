package com.tanmesh.splatter.service;

import com.tanmesh.splatter.entity.Tag;

import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-07
 * Time: 21:33
 */
public interface ITagService {
    void addTag(String tagName);

    List<Tag> getAllTag();

    void deleteTag(String name);

    List<String> autocompleteTags(String inputPrefix);

    void insertAllTagForAutocomplete();
}
