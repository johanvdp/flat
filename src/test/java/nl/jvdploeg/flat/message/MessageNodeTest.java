package nl.jvdploeg.flat.message;

import org.junit.Test;

import nl.jvdploeg.flat.Node;

public class MessageNodeTest {

    @Test
    public void testCreateMessage() {
        final Node expectedNode = createNode();
        final Message actualMessage = MessageNode.createMessage(expectedNode);
        AssertMessage.assertEquals(expectedNode, actualMessage);
    }

    @Test
    public void testCreateNode() {
        final Message expectedMessage = createMessage();
        final Node actualNode = MessageNode.createNode(expectedMessage);
        AssertMessage.assertEquals(expectedMessage, actualNode);
    }

    private Message createMessage() {
        final Message message = new DefaultMessage(MessageNode.ID, Severity.INFO, "message {} {}", new String[] { "a", "b" });
        return message;
    }

    private Node createNode() {
        final Node node = new Node();
        final Node id = node.createChild(MessageNode.ID);
        id.setValue(MessageNode.ID);
        final Node severity = node.createChild(MessageNode.SEVERITY);
        severity.setValue(Severity.INFO.name());
        final Node message = node.createChild(MessageNode.MESSAGE);
        message.setValue("message {} {}");
        final Node arguments = node.createChild(MessageNode.ARGUMENTS);
        arguments.setValue("2");
        final Node arguments0 = arguments.createChild("0");
        arguments0.setValue("a");
        final Node arguments1 = arguments.createChild("1");
        arguments1.setValue("b");
        return node;
    }
}
