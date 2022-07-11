package com.unidir.models.directory.beans;

import com.unidir.models.directory.Directory;
import com.unidir.models.directory.beans.IndexableRecord;
import com.unidir.models.directory.impls.Trie;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
@UtilityClass
public class DirectoryFactory {
  public static <T extends IndexableRecord> Directory<T> trieBackedDirectory() {
    log.info("Initializing tire backed directory");
    return new Trie<>();
  }

  public static <T extends IndexableRecord> Directory<T> trieBackedDirectory(
      Function<T, String> indexGetter) {
    return new Trie<>(indexGetter);
  }
}
