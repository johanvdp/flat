// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;

public class DefaultModelTest {

  @Test
  public void testAdd() {
    // given
    final DefaultModel model = new DefaultModel("name", Enforce.LENIENT);
    // when
    model.add(new Path("A", "B"));
    // then
    Assert.assertNotNull(model.getNode(new Path("A", "B")));
  }

  @Test
  public void testConstructor() {
    // when
    final DefaultModel model = new DefaultModel("name", Enforce.STRICT);
    // then
    Assert.assertEquals("name", model.getName());
    Assert.assertNotNull(model.getRoot());
    Assert.assertNull(model.getRoot().getValue());
    Assert.assertEquals(0, model.getRoot().getChildren().size());
  }

  @Test
  public void testConstructor_Copy() {
    // given
    final DefaultModel model = new DefaultModel("name", Enforce.STRICT);
    model.add(new Path("A"));
    model.setValue(new Path("A"), "a");
    // when
    final Model<?> copy = new DefaultModel("copy", Enforce.LENIENT, model);
    // then
    // different instances
    Assert.assertFalse(model.getRoot() == copy.getRoot());
    Assert.assertFalse(model.getRoot().getChild("A") == copy.getRoot().getChild("A"));
    // same contents
    Assert.assertEquals("a", copy.getRoot().getChild("A").getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_Strict_DoesNotCreateOnDemand() {
    // when
    final Model<?> model = new DefaultModel("name", Enforce.STRICT);
    model.add(new Path("A", "B"));
    // then
    Assert.fail("exception expected");
  }

  @Test
  public void testConstructor_Strict_OnlyCreateLast() {
    // when
    final Model<?> model = new DefaultModel("name", Enforce.STRICT);
    model.add(new Path("A"));
    // then
    Assert.assertNotNull(model.getNode(new Path("A")));
  }

  @Test
  public void testGetValue() {
    // given
    final Model<?> model = new DefaultModel("name", Enforce.LENIENT);
    model.add(new Path("A"));
    model.setValue(new Path("A"), "a");
    // when
    final String getValue = model.getValue(new Path("A"));
    // then
    Assert.assertEquals("a", getValue);
  }

  @Test
  public void testRemove() {
    // given
    final Model<?> model = new DefaultModel("name", Enforce.LENIENT);
    model.add(new Path("A", "B"));
    // when
    model.remove(new Path("A", "B"));
    // then
    Assert.assertEquals(0, model.getNode(new Path("A")).getChildren().size());
  }

  @Test
  public void testSetValue() {
    // given
    final Model<?> model = new DefaultModel("name", Enforce.LENIENT);
    model.add(new Path("A"));
    // when
    model.setValue(new Path("A"), "a");
    // then
    Assert.assertEquals("a", model.getNode(new Path("A")).getValue());
  }

  @Test
  public void testToString() {
    // given
    final Model<?> model = new DefaultModel("name", Enforce.LENIENT);
    model.add(new Path("A", "B"));
    model.setValue(Path.EMPTY, "root");
    model.setVersion(Path.EMPTY, new NumberedVersion(1));
    model.setValue(new Path("A"), "a");
    model.setVersion(new Path("A"), new NumberedVersion(2));
    model.setValue(new Path("A", "B"), "b");
    model.setVersion(new Path("A", "B"), new NumberedVersion(3));
    // when
    final String toString = model.toString();
    // then
    Assert.assertEquals("DefaultModel[name=name,enforce=LENIENT,root=" //
        + "DefaultNode[version=1,value=root,children={A=DefaultNode[version=2,value=a,children={B=DefaultNode[version=3,value=b,children={}]}]}]]",
        toString);
  }
}
