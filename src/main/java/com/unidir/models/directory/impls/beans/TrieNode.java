package com.unidir.models.directory.impls.beans;

import com.unidir.models.directory.beans.IndexableRecord;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Data
public class TrieNode<T extends IndexableRecord> {
  private final Map<Character, TrieNode<T>> children = new ConcurrentHashMap<>();
  private T data;

  public boolean isLeafNode() {
    return !isNull(this.getData());
  }
}
