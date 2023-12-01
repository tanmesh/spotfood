package com.tanmesh.spotfood.autocomplete;

import java.util.List;

/**
 * Created by tanmesh
 * Date: 2019-09-06
 * Time: 18:04
 */
public interface ITagTrie {
    void add(char c);

    void insert(String word);

    String find(String word);

    List<String> autocomplete(String prefix);

    List<String> getAllPrefixesList();
}
