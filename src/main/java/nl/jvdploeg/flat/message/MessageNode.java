package nl.jvdploeg.flat.message;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeCollector;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.NodeValueUtils;
import nl.jvdploeg.flat.Path;

import java.util.List;

public abstract class MessageNode {

  public static final String ARGUMENTS = "arguments";
  public static final String ID = "id";
  public static final String MESSAGE = "message";
  public static final String SEVERITY = "severity";

  /**
   * Create changes between two messages.
   * 
   * @param path
   *          The location of the created of changes.
   * @param oldMessage
   *          The old message.
   * @param newMessage
   *          The new message.
   * @return The changes.
   */
  public static List<Change> createChanges(final Path path, final Message oldMessage,
      final Message newMessage) {
    final ChangeCollector collector = new ChangeCollector();
    String oldValue = oldMessage.getSeverity().name();
    String newValue = newMessage.getSeverity().name();
    collector.setNodeValue(path.createChildPath(MessageNode.SEVERITY), oldValue, newValue);
    oldValue = oldMessage.getMessage();
    newValue = newMessage.getMessage();
    collector.setNodeValue(path.createChildPath(MessageNode.MESSAGE), oldValue, newValue);
    final String[] oldValues = oldMessage.getArguments();
    final String[] newValues = newMessage.getArguments();
    collector.setNodeValue(path.createChildPath(MessageNode.ARGUMENTS), oldValues, newValues);
    return collector.getChanges();
  }

  /**
   * Create message from node.
   * 
   * @param node
   *          The node.
   * @return The created message.
   */
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

  /**
   * Create node from message.
   * 
   * @param message
   *          The message.
   * @return The created node.
   */
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
