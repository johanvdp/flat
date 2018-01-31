// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeType;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.Version;

public class DefaultChangeTest {

  private static final Path PATH = new Path("A", "B");
  private static final Path PATH_EQUAL = new Path("A", "B");
  private static final Path PATH_OTHER = new Path("A", "C");

  private static final Version VERSION_1 = new NumberedVersion(1);
  private static final Version VERSION_2 = new NumberedVersion(2);
  private static final Version VERSION_OLD = new NumberedVersion(0);
  private static final Version VERSION_OTHER = new NumberedVersion(3);

  private static final Change CHANGE_ADD = DefaultChange.add(PATH);
  private static final Change CHANGE_ADD_NEWVALUE = DefaultChange.add(PATH, "new");
  private static final Change CHANGE_REMOVE = DefaultChange.remove(PATH);
  private static final Change CHANGE_SET_INITIAL = DefaultChange.set(PATH, VERSION_1, "new");
  private static final Change CHANGE_SET = DefaultChange.set(PATH, VERSION_OLD, "new");

  @Test
  public void testAdd() {
    Assert.assertEquals(ChangeType.ADD, CHANGE_ADD.getAction());
    Assert.assertEquals(PATH_EQUAL, CHANGE_ADD.getPath());
    Assert.assertEquals(null, CHANGE_ADD.getVersion());
    Assert.assertEquals(null, CHANGE_ADD.getValue());
  }

  @Test
  public void testAddNewValue() {
    Assert.assertEquals(ChangeType.ADD, CHANGE_ADD_NEWVALUE.getAction());
    Assert.assertEquals(PATH_EQUAL, CHANGE_ADD_NEWVALUE.getPath());
    Assert.assertEquals(null, CHANGE_ADD_NEWVALUE.getVersion());
    Assert.assertEquals("new", CHANGE_ADD_NEWVALUE.getValue());
  }

  @Test
  @SuppressWarnings("unlikely-arg-type")
  public void testEquals() {
    // same instance
    Assert.assertTrue(CHANGE_SET.equals(CHANGE_SET));
    // other is null
    Assert.assertFalse(CHANGE_SET.equals(null));
    // different class
    Assert.assertFalse(CHANGE_SET.equals("different class"));
    // different action
    Assert.assertFalse(CHANGE_SET.equals(CHANGE_ADD_NEWVALUE));
    // different path
    Assert.assertFalse(CHANGE_SET.equals(DefaultChange.set(PATH_OTHER, VERSION_2, "new")));

    // version == null, other version == null
    Assert.assertFalse(CHANGE_ADD.equals(DefaultChange.add(PATH, "1")));
    // version == null, other version != null
    Assert.assertFalse(DefaultChange.set(PATH, null, "old").equals(DefaultChange.set(PATH, VERSION_1, "new")));
    // version != null, other version equal
    Assert.assertTrue(CHANGE_SET.equals(DefaultChange.set(PATH, VERSION_OLD, "new")));
    // version != null, other version not equal
    Assert.assertFalse(CHANGE_SET.equals(DefaultChange.set(PATH, VERSION_OTHER, "new")));

    // newValue == null, other newValue != null
    Assert.assertFalse(DefaultChange.set(PATH, VERSION_OLD, null).equals(CHANGE_SET));
    // newValue == null, other newValue == null
    Assert.assertTrue(DefaultChange.set(PATH, VERSION_OLD, null).equals(DefaultChange.set(PATH, VERSION_OLD, null)));
    // newValue != null, other newValue equal
    Assert.assertTrue(CHANGE_SET.equals(DefaultChange.set(PATH, VERSION_OLD, "new")));
    // newValue != null, other newValue not equal
    Assert.assertFalse(CHANGE_SET.equals(DefaultChange.set(PATH, VERSION_OLD, "other")));
  }

  @Test
  public void testHashcode() {
    Assert.assertEquals(CHANGE_SET.hashCode(), DefaultChange.set(PATH, VERSION_OLD, "new").hashCode());
    Assert.assertNotEquals(CHANGE_SET.hashCode(), CHANGE_ADD_NEWVALUE.hashCode());
    Assert.assertNotEquals(CHANGE_SET.hashCode(), DefaultChange.set(PATH_OTHER, VERSION_OLD, "new").hashCode());
    Assert.assertNotEquals(CHANGE_SET.hashCode(), DefaultChange.set(PATH, VERSION_OLD, "other").hashCode());
    // check hashcode differences
    Assert
        .assertEquals(7,
            new HashSet<>(Arrays.asList(CHANGE_ADD, CHANGE_ADD_NEWVALUE, CHANGE_REMOVE, CHANGE_SET_INITIAL, CHANGE_SET,
                DefaultChange.set(PATH, VERSION_OLD, "new"), DefaultChange.set(PATH_OTHER, VERSION_OLD, "new"),
                DefaultChange.set(PATH, VERSION_OLD, "other"))).size());
  }

  @Test
  public void testRemove() {
    Assert.assertEquals(ChangeType.REMOVE, CHANGE_REMOVE.getAction());
    Assert.assertEquals(PATH_EQUAL, CHANGE_REMOVE.getPath());
    Assert.assertEquals(null, CHANGE_REMOVE.getVersion());
    Assert.assertEquals(null, CHANGE_REMOVE.getValue());
  }

  @Test
  public void testSet() {
    Assert.assertEquals(ChangeType.SET, CHANGE_SET.getAction());
    Assert.assertEquals(PATH_EQUAL, CHANGE_SET.getPath());
    Assert.assertEquals(VERSION_OLD, CHANGE_SET.getVersion());
    Assert.assertEquals("new", CHANGE_SET.getValue());
  }

  @Test
  public void testSetInitial() {
    Assert.assertEquals(ChangeType.SET, CHANGE_SET_INITIAL.getAction());
    Assert.assertEquals(PATH_EQUAL, CHANGE_SET_INITIAL.getPath());
    Assert.assertEquals(VERSION_1, CHANGE_SET_INITIAL.getVersion());
    Assert.assertEquals("new", CHANGE_SET_INITIAL.getValue());
  }

  @Test
  public void testToString() {
    Assert.assertEquals("DefaultChange[action=SET,path=Path[path=[A, B]],version=0,newValue=new]",
        DefaultChange.set(PATH, VERSION_OLD, "new").toString());
  }
}
