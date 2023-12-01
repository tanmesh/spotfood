package com.tanmesh.spotfood.autocomplete;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tanmesh.spotfood.entity.Tag;

import java.util.List;
import java.util.Map;

/**
 * Created by tanmesh
 * Date: 2019-09-06
 * Time: 17:50
 */
public class TagTrie implements ITagTrie {
    private Map<Character, TagTrie> children;
    private String prefix;
    private boolean terminal;

    public TagTrie() {
        this(null);
    }

    private TagTrie(String prefixValue) {
        this.children = Maps.newHashMap();
        this.prefix = prefixValue;
        this.terminal = false;
    }

    @Override
    public void add(char c) {
        String localPrefix;
        if (this.prefix == null) {
            localPrefix = Character.toString(c);
        } else {
            localPrefix = this.prefix + c;
        }
        children.put(c, new TagTrie(localPrefix));
    }

    @Override
    public void insert(String word) {
        Preconditions.checkNotNull(word, "word cannot be null");
        TagTrie node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.add(c);
            }
            node = node.children.get(c);
        }
        node.terminal = true;
    }

    @Override
    public String find(String word) {
        TagTrie node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return "";
            }
            node = node.children.get(c);
        }
        return node.prefix;
    }

    @Override
    public List<String> autocomplete(String prefix) {
        TagTrie node = this;
        for (char ch : prefix.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return Lists.newArrayList();
            }
            node = node.children.get(ch);
        }
        return node.getAllPrefixesList();
    }

    @Override
    public List<String> getAllPrefixesList() {
        List<String> results = Lists.newArrayList();
        if (this.terminal) {
            results.add(this.prefix);
        }
        for (Map.Entry<Character, TagTrie> entry : children.entrySet()) {
            TagTrie child = entry.getValue();
            List<String> childPrefixes = child.getAllPrefixesList();
            results.addAll(childPrefixes);
        }
        return results;
    }

    // TODO: complete delete(tag)
    public void delete(Tag tag) {
    }
}
