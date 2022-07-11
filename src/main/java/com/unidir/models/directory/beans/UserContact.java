package com.unidir.models.directory.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserContact implements IndexableRecord {
  private final String id;
  private final Name name;
  private final String email;
  private final String phoneNumber;

  /**
   * @implNote By default, if no indexable property getter is supplied, use this method.
   * @return Indexable property - property to index with.
   */
  @Override
  public String getIndexableProp() {
    return this.getName().getFirstName();
  }
}
