package com.unidir.models.directory.impls;

import com.unidir.models.directory.Directory;
import com.unidir.models.directory.beans.IndexableRecord;
import com.unidir.models.directory.impls.beans.TrieNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trie<T extends IndexableRecord> extends Directory<T> {
  private final TrieNode<T> root = new TrieNode<>();

  public Trie(Function<T, String> indexablePropertyGetter) {
    super(indexablePropertyGetter);
  }

  @Override
  public void register(T record) {
    TrieNode<T> currentNode = this.getRoot();
    for (char c : getIndexableProperty(record).toCharArray()) {
      currentNode = currentNode.getChildren().computeIfAbsent(c, key -> new TrieNode<>());
    }
    currentNode.setData(record);
  }

  @Override
  public void update(T record) {
    final String key = getIndexableProperty(record);
    TrieNode<T> currentNode = findTrieNodeHelper(key);
    currentNode.setData(record);
  }

  @Override
  public T search(String indexableProperty) {
    return findTrieNodeHelper(indexableProperty).getData();
  }

  @Override
  public void delete(String indexableProperty) {
    deleteNodeHelper(this.getRoot(), indexableProperty, 0);
  }

  private boolean deleteNodeHelper(TrieNode<T> node, String indexableProperty, int index) {
    if (index >= indexableProperty.length()) {
      return node.isLeafNode();
    }
    TrieNode<T> child = node.getChildren().get(indexableProperty.charAt(index));
    if (isNull(child)) {
      return false;
    }
    boolean shouldDeleteChild = deleteNodeHelper(child, indexableProperty, index + 1);
    if (shouldDeleteChild) {
      node.getChildren().remove(indexableProperty.charAt(index));
      return node.getChildren().isEmpty();
    }
    return false;
  }

  private String getIndexableProperty(T data) {
    return isNull(super.getIndexablePropertyGetter())
        ? data.getIndexableProp()
        : super.getIndexablePropertyGetter().apply(data);
  }

  private TrieNode<T> findTrieNodeHelper(String key) {
    TrieNode<T> currentNode = this.getRoot();
    for (char c : key.toCharArray()) {
      if (currentNode.getChildren().containsKey(c)) {
        currentNode = currentNode.getChildren().get(c);
      } else {
        throw new NoSuchElementException("No such element found with provided key: " + key);
      }
    }
    if (!currentNode.isLeafNode()) {
      throw new NoSuchElementException("No such element found with provided key: " + key);
    }
    return currentNode;
  }
}
