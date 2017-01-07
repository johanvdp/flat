package nl.jvdploeg.flat;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

  @Before
  public void before() {
  }

  @Test
  public void testConstructor() {
    final Node node = new Node();
    Assert.assertNull(node.getValue());
    Assert.assertEquals(0, node.getChildren().size());
  }

  @Test
  public void testCopyConstructor() {
    final Node node = new Node();
    node.setValue("value");
    final Node child = node.createChild("child");
    child.setValue("childValue");

    final Node copy = new Node(node);

    // different instances
    Assert.assertFalse(node == copy);
    Assert.assertFalse(node.getChildren() == copy.getChildren());
    Assert.assertFalse(node.getChild("child") == copy.getChild("child"));
    // same contents
    Assert.assertEquals("value", copy.getValue());
    Assert.assertEquals(1, copy.getChildren().size());
    Assert.assertEquals("childValue", copy.getChild("child").getValue());
  }

  @Test
  public void testCreateChild() {
    final Node node = new Node();
    final Node child = node.createChild("child");
    Assert.assertNotNull(child);
    Assert.assertEquals(1, node.getChildren().size());
    Assert.assertEquals(1, node.getChildren().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateChildExists() {
    final Node node = new Node();
    node.createChild("child");
    node.createChild("child");
  }

  @Test
  public void testEquals() {
    final Node node = createNode("value", "childValue");
    final Node nodeEquals = createNode("value", "childValue");
    final Node nodeDifferentValue = createNode("different", "childValue");
    final Node nodeDifferentChildValue = createNode("value", "different");
    Assert.assertTrue(node.equals(nodeEquals));
    Assert.assertFalse(node.equals(nodeDifferentValue));
    Assert.assertFalse(node.equals(nodeDifferentChildValue));
  }

  @Test
  public void testFindChild() {
    final Node node = new Node();
    final Node child = node.createChild("child");
    Assert.assertEquals(child, node.findChild("child"));
  }

  @Test
  public void testFindChildNotFound() {
    final Node node = new Node();
    Assert.assertNull(node.findChild("other"));
  }

  @Test
  public void testGetChild() {
    final Node node = new Node();
    final Node child = node.createChild("child");
    Assert.assertEquals(child, node.getChild("child"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetChildDoesNotExist() {
    final Node node = new Node();
    Assert.assertNull(node.getChild("other"));
  }

  @Test
  public void testGetChildren() {
    final Node node = new Node();
    final Node child = node.createChild("child");
    Assert.assertEquals(1, node.getChildren().size());
    Assert.assertTrue(child == node.getChildren().get("child"));
  }

  @Test
  public void testGetOrCreateChild() {
    final Node node = new Node();
    final Node child = node.getOrCreateChild("child");
    Assert.assertNotNull(child);
    final Node sameChild = node.getOrCreateChild("child");
    Assert.assertTrue(child == sameChild);
  }

  @Test
  public void testGetSetValue() {
    final Node node = new Node();
    node.setValue("value");
    Assert.assertEquals("value", node.getValue());
  }

  @Test
  public void testHashcode() {
    final Node node = createNode("value", "childValue");
    final Node nodeEquals = createNode("value", "childValue");
    final Node nodeDifferentValue = createNode("different", "childValue");
    final Node nodeDifferentChildValue = createNode("value", "different");
    Assert.assertEquals(nodeEquals.hashCode(), node.hashCode());
    Assert.assertNotEquals(nodeDifferentValue.hashCode(), node.hashCode());
    Assert.assertNotEquals(nodeDifferentChildValue.hashCode(), node.hashCode());
    // different hashcodes
    Assert
        .assertEquals(3,
            new HashSet<Node>(
                Arrays.asList(node, nodeEquals, nodeDifferentValue, nodeDifferentChildValue))
                    .size());

  }

  private Node createNode(final String value, final String childValue) {
    final Node node = new Node();
    node.setValue(value);
    final Node child = node.createChild("child");
    child.setValue(childValue);
    return node;
  }

}
