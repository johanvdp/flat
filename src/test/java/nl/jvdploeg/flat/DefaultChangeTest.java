package nl.jvdploeg.flat;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultChangeTest {

  private DefaultChange changeOne;
  private DefaultChange changeEqual;
  private DefaultChange changeDifferentAction;
  private DefaultChange changeDifferentPath;
  private DefaultChange changeDifferentOldValue;
  private DefaultChange changeDifferentNewValue;

  @Before
  public void before() {
    changeOne = new DefaultChange(ChangeAction.ADD, new Path(new String[] { "A", "B" }), "old",
        "new");
    changeEqual = new DefaultChange(ChangeAction.ADD, new Path(new String[] { "A", "B" }), "old",
        "new");
    changeDifferentAction = new DefaultChange(ChangeAction.SET, new Path(new String[] { "A", "B" }),
        "old", "new");
    changeDifferentPath = new DefaultChange(ChangeAction.ADD,
        new Path(new String[] { "A", "OTHER" }), "old", "new");
    changeDifferentOldValue = new DefaultChange(ChangeAction.ADD,
        new Path(new String[] { "A", "B" }), "other", "new");
    changeDifferentNewValue = new DefaultChange(ChangeAction.ADD,
        new Path(new String[] { "A", "B" }), "old", "other");
  }

  @Test
  public void testConstructor() {
    Assert.assertEquals(ChangeAction.ADD, changeOne.getAction());
    Assert.assertEquals(new Path(new String[] { "A", "B" }), changeOne.getPath());
    Assert.assertEquals("old", changeOne.getOldValue());
    Assert.assertEquals("new", changeOne.getNewValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue(changeOne.equals(changeEqual));
    Assert.assertFalse(changeOne.equals(changeDifferentAction));
    Assert.assertFalse(changeOne.equals(changeDifferentPath));
    Assert.assertFalse(changeOne.equals(changeDifferentOldValue));
    Assert.assertFalse(changeOne.equals(changeDifferentNewValue));
  }

  @Test
  public void testHashcode() {
    Assert.assertEquals(changeOne.hashCode(), changeEqual.hashCode());
    Assert.assertNotEquals(changeOne.hashCode(), changeDifferentAction.hashCode());
    Assert.assertNotEquals(changeOne.hashCode(), changeDifferentPath.hashCode());
    Assert.assertNotEquals(changeOne.hashCode(), changeDifferentOldValue.hashCode());
    Assert.assertNotEquals(changeOne.hashCode(), changeDifferentNewValue.hashCode());
    // different hashcodes
    Assert.assertEquals(5,
        new HashSet<Change>(Arrays.asList(changeOne, changeEqual, changeDifferentAction,
            changeDifferentPath, changeDifferentOldValue, changeDifferentNewValue)).size());
  }

}
