// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import org.junit.Assert;
import org.junit.Test;

import nl.jvdploeg.flat.util.TestModel;

public class DefaultNodeValueUtilsTest {

  @Test
  public void testCreateValueArray() {
    // given
    final DefaultNode node = new DefaultNode();
    // when
    DefaultNodeValueUtils.createValueArray(node, new String[] { "a", "b" });
    // then
    Assert.assertEquals("a", node.getChildren().get("0").getValue());
    Assert.assertEquals("b", node.getChildren().get("1").getValue());
  }

  @Test
  public void testGetBestValue() {
    // given
    final DefaultNode node = TestModel.create().getRoot();
    // when/then
    Assert.assertEquals(null, DefaultNodeValueUtils.getBestValue(node, TestModel.PATH_EMPTY.createChildPath("B")));
    Assert.assertEquals("b", DefaultNodeValueUtils.getBestValue(node, TestModel.PATH_A.createChildPath("B")));
    Assert.assertEquals("b", DefaultNodeValueUtils.getBestValue(node, TestModel.PATH_A_A.createChildPath("B")));
    Assert.assertEquals("b", DefaultNodeValueUtils.getBestValue(node, TestModel.PATH_A_B.createChildPath("B")));
    Assert.assertEquals("bb", DefaultNodeValueUtils.getBestValue(node, TestModel.PATH_A_C_E.createChildPath("B")));
    Assert.assertEquals("bb", DefaultNodeValueUtils.getBestValue(node, TestModel.PATH_A_C_E_B.createChildPath("B")));
  }

  @Test
  public void testGetValueArray() {
    // given
    final DefaultNode node = new DefaultNode();
    DefaultNodeValueUtils.createValueArray(node, new String[] { "a", "b" });
    // when
    final String[] array = DefaultNodeValueUtils.getValueArray(node);
    // then
    Assert.assertEquals("a", array[0]);
    Assert.assertEquals("b", array[1]);
  }
}
