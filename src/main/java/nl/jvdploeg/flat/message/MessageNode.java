package nl.jvdploeg.flat.message;

import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeCollector;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.NodeValueUtils;
import nl.jvdploeg.flat.Path;

public abstract class MessageNode {

    public static final String ARGUMENTS = "arguments";
    public static final String ID = "id";
    public static final String MESSAGE = "message";
    public static final String SEVERITY = "severity";

    public static List<Change> createChanges(final Path path, final Message existingMessage, final Message newMessage) {
        final ChangeCollector collector = new ChangeCollector();
        String oldValue = existingMessage.getSeverity().name();
        String newValue = newMessage.getSeverity().name();
        collector.setNodeValue(path.createChildPath(MessageNode.SEVERITY), oldValue, newValue);
        oldValue = existingMessage.getMessage();
        newValue = newMessage.getMessage();
        collector.setNodeValue(path.createChildPath(MessageNode.MESSAGE), oldValue, newValue);
        final String[] oldValues = existingMessage.getArguments();
        final String[] newValues = newMessage.getArguments();
        collector.setNodeValue(path.createChildPath(MessageNode.ARGUMENTS), oldValues, newValues);
        return collector.getChanges();
    }

    public static Message createMessage(final Node node) {
        final Node idNode = node.getChild(MessageNode.ID);
        final Node severityNode = node.getChild(MessageNode.SEVERITY);
        final Node messageNode = node.getChild(MessageNode.MESSAGE);
        final Node argumentsNode = node.getChild(MessageNode.ARGUMENTS);
        final String id = idNode.getValue();
        final Severity severity = Enum.valueOf(Severity.class, severityNode.getValue());
        final String message = messageNode.getValue();
        final String[] arguments = NodeValueUtils.getValueArray(argumentsNode);
        final Message createdMessage = new DefaultMessage(id, severity, message, arguments);
        return createdMessage;
    }

    public static Node createNode(final Message message) {
        final Node node = new Node();
        node.createChild(MessageNode.ID).setValue(message.getIdentifier());
        node.createChild(MessageNode.SEVERITY).setValue(message.getSeverity().name());
        node.createChild(MessageNode.MESSAGE).setValue(message.getMessage());
        final Node arguments = node.createChild(MessageNode.ARGUMENTS);
        NodeValueUtils.createValueArray(arguments, message.getArguments());
        return node;
    }
}