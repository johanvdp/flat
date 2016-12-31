package nl.jvdploeg.flat;

public abstract class NodeValueUtils {

  public static void createValueArray(final Node node, final String[] arguments) {
    node.setValue(Integer.toString(arguments.length));
    for (int i = 0; i < arguments.length; i++) {
      final Node indexChild = node.createChild(Integer.toString(i));
      indexChild.setValue(arguments[i]);
    }
  }

  /**
   * Follow the path and get the value of the last matching value along the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @return The value of the last node in the path.
   */
  public static String getBestValue(final Node node, final Path path) {
    final Path parentPath = path.createParentPath();
    final String childName = path.getLastNodeName();
    return getBestValueIndex(node, parentPath, 0, childName, null);
  }

  public static String[] getValueArray(final Node node) {
    final int numberOfNodes = Integer.valueOf(node.getValue());
    final String[] nodes = new String[numberOfNodes];
    for (int i = 0; i < numberOfNodes; i++) {
      nodes[i] = node.getChild(Integer.toString(i)).getValue();
    }
    return nodes;
  }

  /**
   * Recursively follow the path and get the value of the last matching value along the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param index
   *          The path node index.
   * @param valueName
   *          The value name.
   * @param bestValue
   *          The best value.
   * @return The value.
   */
  private static String getBestValueIndex(final Node node, final Path path, final int index,
      final String valueName, final String bestValue) {
    // check this node for better value
    final Node valueNode = node.findChild(valueName);
    String betterValue = bestValue;
    if (valueNode != null && valueNode.getValue() != null) {
      betterValue = valueNode.getValue();
    }
    if (index == path.getLength()) {
      // past end of path
      return betterValue;
    }
    final String name = path.getName(index);
    final Node child = node.getChild(name);
    return getBestValueIndex(child, path, index + 1, valueName, betterValue);
  }
}
