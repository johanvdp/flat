// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class DefaultNodeTest {

  @Test
  public void testConstructor_Copy() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    final DefaultNode child = node.createChild("child");
    child.setValue("childValue");
    // when
    final DefaultNode copy = new DefaultNode(node);
    // then
    // different instances
    Assert.assertFalse(node.getChildren() == copy.getChildren());
    Assert.assertFalse(node.getChild("child") == copy.getChild("child"));
    // same contents
    Assert.assertEquals("value", copy.getValue());
    Assert.assertEquals(1, copy.getChildren().size());
    Assert.assertEquals("childValue", copy.getChild("child").getValue());
  }

  @Test
  public void testConstructor_Empty() {
    // when
    final DefaultNode node = new DefaultNode();
    // then
    Assert.assertNotNull(node.getVersion());
    Assert.assertNull(node.getValue());
    Assert.assertEquals(0, node.getChildren().size());
  }

  @Test
  public void testCreateChild() {
    // given
    final DefaultNode node = new DefaultNode();
    // when
    final DefaultNode child = node.createChild("child");
    // then
    Assert.assertNotNull(child);
    Assert.assertEquals(1, node.getChildren().size());
    Assert.assertTrue(child == node.getChildren().get("child"));
  }

  @Test(expected = RuntimeException.class)
  public void testCreateChild_Exists() {
    // given
    final DefaultNode node = new DefaultNode();
    node.createChild("child");
    // when
    node.createChild("child");
    // then
    Assert.fail("exception expected");
  }

  @Test
  public void testEquals() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    final DefaultNode child = node.createChild("child");
    child.setValue("childValue");
    final DefaultNode other = new DefaultNode();
    other.setValue("value");
    final DefaultNode otherChild = other.createChild("child");
    otherChild.setValue("childValue");
    // when/then
    Assert.assertTrue(node.equals(other));
  }

  @Test
  public void testEquals_ChildrenDifferent() {
    // given
    final DefaultNode node = new DefaultNode();
    final DefaultNode child = node.createChild("child");
    child.setValue("childValue");
    final DefaultNode other = new DefaultNode();
    // when/then
    Assert.assertFalse(node.equals(other));
  }

  @Test
  @SuppressWarnings("unlikely-arg-type")
  public void testEquals_OtherClass() {
    // given
    final DefaultNode node = new DefaultNode();
    // when/then
    Assert.assertFalse(node.equals("OtherClass"));
  }

  @Test
  public void testEquals_OtherNull() {
    // given
    final DefaultNode node = new DefaultNode();
    // when/then
    Assert.assertFalse(node.equals(null));
  }

  @Test
  public void testEquals_SameObject() {
    // given
    final DefaultNode node = new DefaultNode();
    // when/then
    Assert.assertTrue(node.equals(node));
  }

  @Test
  public void testEquals_ValueDifferent() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    final DefaultNode other = new DefaultNode();
    other.setValue("different");
    // when/then
    Assert.assertFalse(node.equals(other));
  }

  @Test
  public void testEquals_ValueNull_OtherValueNotNull() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue(null);
    final DefaultNode other = new DefaultNode();
    other.setValue("value");
    // when/then
    Assert.assertFalse(node.equals(other));
  }

  @Test
  public void testEquals_VersionDifferent() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    node.setVersion(new NumberedVersion(1));
    final DefaultNode child = node.createChild("child");
    child.setValue("childValue");
    final DefaultNode other = new DefaultNode();
    node.setValue("value");
    node.setVersion(new NumberedVersion(2));
    final DefaultNode otherChild = other.createChild("child");
    otherChild.setValue("childValue");
    // when/then
    Assert.assertFalse(node.equals(other));
  }

  @Test
  public void testFindChild() {
    // given
    final DefaultNode node = new DefaultNode();
    final DefaultNode child = node.createChild("child");
    // when
    final DefaultNode findChild = node.findChild("child");
    // then
    Assert.assertEquals(child, findChild);
  }

  @Test
  public void testFindChild_NotFound() {
    // given
    final DefaultNode node = new DefaultNode();
    // when
    final DefaultNode findChild = node.findChild("other");
    // then
    Assert.assertNull(findChild);
  }

  @Test
  public void testGetChild() {
    // given
    final DefaultNode node = new DefaultNode();
    final DefaultNode child = node.createChild("child");
    // when
    final DefaultNode getChild = node.getChild("child");
    // then
    Assert.assertEquals(child, getChild);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetChild_NotFound() {
    // given
    final DefaultNode node = new DefaultNode();
    // when
    node.getChild("other");
    // then
    Assert.fail("exception expected");
  }

  @Test
  public void testGetChildren() {
    // given
    final DefaultNode node = new DefaultNode();
    final DefaultNode child = node.createChild("child");
    // when
    final Map<String, DefaultNode> children = node.getChildren();
    // then
    Assert.assertNotNull(children);
    Assert.assertEquals(1, children.size());
    Assert.assertTrue(child == children.get("child"));
  }

  @Test
  public void testGetValue() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    // when
    final String value = node.getValue();
    // then
    Assert.assertEquals("value", value);
  }

  @Test
  public void testHashCode() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    final DefaultNode child = node.createChild("child");
    child.setValue("childValue");

    final DefaultNode equal = new DefaultNode();
    equal.setValue("value");
    final DefaultNode equalChild = equal.createChild("child");
    equalChild.setValue("childValue");

    final DefaultNode different = new DefaultNode();
    different.setValue("value");
    final DefaultNode differentChild1 = different.createChild("child1");
    different.createChild("child2");
    differentChild1.setValue("child1Value");

    // when/then
    Assert.assertTrue(node.hashCode() == equal.hashCode());
    Assert.assertFalse(node.hashCode() == different.hashCode());
  }

  @Test
  public void testToString() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("value");
    final DefaultNode child = node.createChild("child");
    child.setValue("childValue");
    // when
    final String toString = node.toString();
    // then
    Assert.assertEquals("DefaultNode[version=0,value=value,children={child=DefaultNode[version=0,value=childValue,children={}]}]", toString);
  }
}
