package com.unidir.models.directory;

import com.unidir.models.directory.beans.IndexableRecord;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@NoArgsConstructor
public abstract class Directory<T extends IndexableRecord> {

  private Function<T, String> indexablePropertyGetter;

  public Directory(Function<T, String> indexablePropertyGetter) {
    this.indexablePropertyGetter = indexablePropertyGetter;
  }

  public abstract void register(T record);

  public abstract void update(T record);

  public abstract T search(String indexableProperty);

  public abstract void delete(String indexableProperty);
}
