// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.util.TestModel;

public class DefaultNodeUtilsTest {

  @Test
  public void testAddLast_Lenient_DoesCreatePath() {
    // given
    final DefaultNode node = new DefaultNode();
    // when
    final DefaultNode addLast = DefaultNodeUtils.add(node, new Path("A", "B"), Enforce.LENIENT);
    // then
    Assert.assertTrue(addLast == node.getChild("A").getChild("B"));
  }

  @Test
  public void testAddLast_Strict_IsLast() {
    // given
    final DefaultNode node = new DefaultNode();
    node.createChild("A");
    // when
    final DefaultNode addLast = DefaultNodeUtils.add(node, new Path("A", "B"), Enforce.STRICT);
    // then
    Assert.assertTrue(addLast == node.getChild("A").getChild("B"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddLast_Strict_IsNotLast() {
    // given
    final DefaultNode node = new DefaultNode();
    // when
    DefaultNodeUtils.add(node, new Path("A", "B"), Enforce.STRICT);
    // then
    Assert.fail("exception expected");
  }

  @Test
  public void testFindNode() {
    // given
    final DefaultNode node = TestModel.create().getRoot();
    // when/then
    Assert.assertEquals("a", DefaultNodeUtils.findNode(node, TestModel.PATH_A).getValue());
    Assert.assertNull(DefaultNodeUtils.findNode(node, TestModel.PATH_D_B));
  }

  @Test
  public void testGetChildNodeArray() {
    // given
    final DefaultNode node = new DefaultNode();
    node.setValue("2");
    final DefaultNode child0 = node.createChild("0");
    final DefaultNode child1 = node.createChild("1");
    // when
    final DefaultNode[] array = DefaultNodeUtils.getChildNodeArray(node);
    // then
    Assert.assertEquals(2, array.length);
    Assert.assertTrue(child0 == array[0]);
    Assert.assertTrue(child1 == array[1]);
  }

  @Test
  public void testGetNode() {
    // given
    final DefaultNode node = TestModel.create().getRoot();
    // when/then
    Assert.assertEquals("a", DefaultNodeUtils.getNode(node, TestModel.PATH_A).getValue());
    Assert.assertEquals("b", DefaultNodeUtils.getNode(node, TestModel.PATH_A_B).getValue());
    Assert.assertEquals("c", DefaultNodeUtils.getNode(node, TestModel.PATH_A_C).getValue());
  }

  @Test
  public void testRemove() {
    // given
    final DefaultNode node = new DefaultNode();
    final DefaultNode childA = node.createChild("A");
    // when
    final DefaultNode removeLast = DefaultNodeUtils.removeLast(node, new Path("A"));
    // then
    Assert.assertTrue(childA == removeLast);
    Assert.assertEquals(0, node.getChildren().size());
  }

  @Test(expected = RuntimeException.class)
  public void testRemove_PathNodeDoesNotExists() {
    // given
    final DefaultNode node = new DefaultNode();
    node.createChild("A");
    // when
    DefaultNodeUtils.removeLast(node, new Path("B"));
    // then
    Assert.fail("exception expected");
  }
}
