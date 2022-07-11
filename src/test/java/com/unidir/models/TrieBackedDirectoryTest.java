package com.unidir.models;

import com.unidir.models.directory.Directory;
import com.unidir.models.directory.beans.DirectoryFactory;
import com.unidir.models.directory.beans.Name;
import com.unidir.models.directory.beans.UserContact;
import com.unidir.models.directory.impls.Trie;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TrieBackedDirectoryTest {
  private static final UserContact SAKSHAM =
      UserContact.builder()
          .name(new Name("Saksham", "", "Sethi"))
          .id(UUID.randomUUID().toString())
          .email("sakshamsethi47@gmail.com")
          .phoneNumber("8283886544")
          .build();
  private static final UserContact JORDAN =
      UserContact.builder()
          .name(new Name("Jordan", "", "Smith"))
          .id(UUID.randomUUID().toString())
          .email("jsmith@gmail.com")
          .phoneNumber("8283886500")
          .build();
  private static final UserContact TOM =
      UserContact.builder()
          .name(new Name("Tom", "", "Holland"))
          .id(UUID.randomUUID().toString())
          .email("t.holland@gmail.com")
          .phoneNumber("2283886500")
          .build();

  @Test
  public void trieBackedDirectoryTest_createsTrieBackedDirectory_usingDirectoryFactoryMethod() {
    Directory<UserContact> userContactDirectory = DirectoryFactory.trieBackedDirectory();
    assert userContactDirectory instanceof Trie;
  }

  @Test
  public void
      trieBackedDirectoryTest_createsTrieBackedDirectoryWithCustomIndexGetter_usingDirectoryFactoryMethodWithIndexPropGetter() {
    Directory<UserContact> userContactDirectory =
        DirectoryFactory.trieBackedDirectory(UserContact::getEmail);
    assert userContactDirectory instanceof Trie;
    assertNotNull(userContactDirectory.getIndexablePropertyGetter());
  }

  @Test
  public void
      trieBackedDirectoryRegisterAndSearchTest_registersAndSearchUserContacts_givenFirstAddingAndThenSearchUserContact() {
    Directory<UserContact> userContactDirectory = DirectoryFactory.trieBackedDirectory();
    userContactDirectory.register(SAKSHAM);
    UserContact searchedContact = userContactDirectory.search("Saksham");
    assertEquals(searchedContact, SAKSHAM);
  }

  @Test
  public void
      trieBackedDirectoryRegisterAndSearchTest_registersAndSearchUserContactsWithIndexGetter_givenFirstAddingAndThenSearchUserContactWithIndexGetter() {
    Directory<UserContact> userContactDirectory =
        DirectoryFactory.trieBackedDirectory(userContact -> userContact.getName().getLastName());
    userContactDirectory.register(TOM);
    UserContact searchedContact = userContactDirectory.search("Holland");
    assertEquals(searchedContact, TOM);
  }

  @Test
  public void
      trieBackedDirectoryRegisterAndSearchTest_registersAndSearchMultipleUserContacts_givenFirstAddingAndThenSearchMultiUserContact() {
    Directory<UserContact> userContactDirectory = DirectoryFactory.trieBackedDirectory();
    userContactDirectory.register(SAKSHAM);
    userContactDirectory.register(TOM);
    userContactDirectory.register(JORDAN);
    UserContact searchedContactSaksham = userContactDirectory.search("Saksham");
    assertEquals(searchedContactSaksham, SAKSHAM);
    UserContact searchedContactJordan = userContactDirectory.search("Jordan");
    assertEquals(searchedContactJordan, JORDAN);
    UserContact searchedContactTom = userContactDirectory.search("Tom");
    assertEquals(searchedContactTom, TOM);
  }

  @Test
  public void
      trieBackedDirectoryDeletionTest_deletesAllNodesWithNoEntries_givenFirstAddingThenDeletingAndFinallySearchingUsers() {
    Directory<UserContact> userContactDirectory =
        DirectoryFactory.trieBackedDirectory(userContact -> userContact.getName().getLastName());
    userContactDirectory.register(SAKSHAM);
    userContactDirectory.register(TOM);
    userContactDirectory.register(JORDAN);
    userContactDirectory.delete(JORDAN.getName().getLastName());
    assertThrows(NoSuchElementException.class, () -> userContactDirectory.search("Smith"));
  }
}
