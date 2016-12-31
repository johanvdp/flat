package nl.jvdploeg.flat.message;

import nl.jvdploeg.flat.Node;
import org.junit.Assert;

public abstract class AssertMessage {

  public static void assertEquals(final Message expectedMessage, final Node actualNode) {
    final Node expectedNode = MessageNode.createNode(expectedMessage);
    Assert.assertEquals(expectedNode, actualNode);
  }

  public static void assertEquals(final Node expectedNode, final Message actualMessage) {
    final Node actualNode = MessageNode.createNode(actualMessage);
    Assert.assertEquals(expectedNode, actualNode);
  }
}
