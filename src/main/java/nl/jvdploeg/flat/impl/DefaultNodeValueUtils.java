// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import nl.jvdploeg.exception.Checks;
import nl.jvdploeg.flat.Path;

/**
 * Methods to access values in the {@link DefaultNode} hierarchy.
 */
abstract class DefaultNodeValueUtils {

  public static void createValueArray(final DefaultNode node, final String[] arguments) {
    Checks.ARGUMENT.notNull(node, "node");
    Checks.ARGUMENT.notNull(arguments, "arguments");
    node.setValue(Integer.toString(arguments.length));
    for (int i = 0; i < arguments.length; i++) {
      final DefaultNode indexChild = node.createChild(Integer.toString(i));
      indexChild.setValue(arguments[i]);
    }
  }

  /**
   * Follow the path and get the value of the last matching value along the
   * path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param valueName
   *          The value name.
   * @return The value of the last node in the path.
   */
  public static String getBestValue(final DefaultNode node, final Path path) {
    Checks.ARGUMENT.notNull(node, "node");
    Checks.ARGUMENT.notNull(path, "path");
    final Path parentPath = path.createParentPath();
    final String childName = path.getLastNodeName();
    return getBestValueIndex(node, parentPath, 0, childName, null);
  }

  public static String[] getValueArray(final DefaultNode node) {
    Checks.ARGUMENT.notNull(node, "node");
    final int numberOfNodes = Integer.parseInt(node.getValue());
    final String[] nodes = new String[numberOfNodes];
    for (int i = 0; i < numberOfNodes; i++) {
      nodes[i] = node.getChild(Integer.toString(i)).getValue();
    }
    return nodes;
  }

  /**
   * Recursively follow the path and get the value of the last matching value
   * along the path.
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
  private static String getBestValueIndex(final DefaultNode node, final Path path, final int index, final String valueName, final String bestValue) {
    // check this node for better value
    final DefaultNode valueNode = node.findChild(valueName);
    String betterValue = bestValue;
    if (valueNode != null && valueNode.getValue() != null) {
      betterValue = valueNode.getValue();
    }
    if (index == path.getLength()) {
      // past end of path
      return betterValue;
    }
    final String name = path.getName(index);
    final DefaultNode child = node.getChild(name);
    return getBestValueIndex(child, path, index + 1, valueName, betterValue);
  }

  private DefaultNodeValueUtils() {
  }
}
