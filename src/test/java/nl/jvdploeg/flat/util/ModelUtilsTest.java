// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.util;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.impl.DefaultChange;
import nl.jvdploeg.flat.impl.NumberedVersion;

public class ModelUtilsTest {

  private Model<?> model;

  @Before
  public void before() {
    model = TestModel.create();
  }

  @Test
  public void testApplyChange_Add() {
    // given
    final Change change = DefaultChange.add(new Path("A", "NEW"));
    // when
    ModelUtils.applyChange(model, change);
    // then
    Assert.assertNotNull(model.getNode(new Path("A", "NEW")));
  }

  @Test
  public void testApplyChange_Remove() {
    // given
    final int childCount = model.getNode(new Path("A")).getChildren().size();
    final Change change = DefaultChange.remove(new Path("A", "B"));
    // when
    ModelUtils.applyChange(model, change);
    // then
    Assert.assertEquals(childCount - 1, model.getNode(new Path("A")).getChildren().size());
  }

  @Test
  public void testApplyChange_Set_VersionMatch() {
    // given
    final Change change = DefaultChange.set(TestModel.PATH_A, new NumberedVersion(0), "newA");
    // when
    ModelUtils.applyChange(model, change);
    // then
    Assert.assertEquals("newA", model.getValue(TestModel.PATH_A));
  }

  @Test(expected = RuntimeException.class)

  public void testApplyChange_Set_VersionMismatch() {
    // given
    // currentValue=a, version=wrong
    final Change change = DefaultChange.set(TestModel.PATH_A, new NumberedVersion(-1), "dontcare");
    // when
    ModelUtils.applyChange(model, change);
    // then
    Assert.fail("exception expected");
  }

  @Test
  public void testApplyChange_Set_WithoutVersion() {
    // given
    final Change change = DefaultChange.set(TestModel.PATH_A, null, "newA");
    // when
    ModelUtils.applyChange(model, change);
    // then
    Assert.assertEquals("newA", model.getValue(TestModel.PATH_A));
  }

  @Test(expected = RuntimeException.class)
  public void testApplyChange_SetCurrentValueNull() {
    // given
    // currentValue=null, version=wrong
    final Change change = DefaultChange.set(TestModel.PATH_A_C_E, new NumberedVersion(-1), "dontcare");
    // when
    ModelUtils.applyChange(model, change);
    // then
    Assert.fail("exception expected");
  }

  @Test
  public void testApplyChanges() {
    // given
    final Change change1 = DefaultChange.set(TestModel.PATH_A, new NumberedVersion(0), "newA");
    final Change change2 = DefaultChange.add(new Path("A", "NEW"));
    // when
    ModelUtils.applyChanges(model, Arrays.asList(change1, change2));
    // then
    Assert.assertEquals("newA", model.getValue(TestModel.PATH_A));
    Assert.assertNotNull(model.getNode(new Path("A", "NEW")));
  }
}
