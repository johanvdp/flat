package nl.jvdploeg.flat.message;

import org.junit.Assert;

import nl.jvdploeg.flat.Node;

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
