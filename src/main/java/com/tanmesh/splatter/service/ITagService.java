package com.tanmesh.splatter.service;

import com.tanmesh.splatter.wsRequestModel.TagData;

import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-07
 * Time: 21:33
 */
public interface ITagService {
    void addTag(String tagName);

    List<TagData> getAllTag();

    void deleteTag(String name);

    List<String> autocompleteTags(String inputPrefix);
}
