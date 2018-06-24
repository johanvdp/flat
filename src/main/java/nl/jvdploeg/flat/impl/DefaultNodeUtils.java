// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import nl.jvdploeg.exception.Checks;
import nl.jvdploeg.exception.ThrowableBuilder;
import nl.jvdploeg.flat.Path;

/**
 * Methods to access the {@link DefaultNode} hierarchy.
 */
abstract class DefaultNodeUtils {

  /**
   * Follow the path and add the node to the path. Creating only the last node
   * when {@link Enforce#STRICT} or all nodes when {@link Enforce#LENIENT}.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param enforce
   *          The enforcement mode.
   */
  public static DefaultNode add(final DefaultNode node, final Path path, final Enforce enforce) {
    Checks.ARGUMENT.notNull(node, "node");
    Checks.ARGUMENT.notNull(path, "path");
    Checks.ARGUMENT.notNull(enforce, "enforce");
    return addAt(node, path, 0, enforce);
  }

  /**
   * Follow the path and find the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @return The last node in the path, or <code>null</code> if it does not
   *         exist.
   */
  public static DefaultNode findNode(final DefaultNode node, final Path path) {
    Checks.ARGUMENT.notNull(node, "node");
    Checks.ARGUMENT.notNull(path, "path");
    return findLastAt(node, path, 0);
  }

  public static DefaultNode[] getChildNodeArray(final DefaultNode node) {
    Checks.ARGUMENT.notNull(node, "node");
    final int numberOfNodes = Integer.parseInt(node.getValue());
    final DefaultNode[] nodes = new DefaultNode[numberOfNodes];
    for (int i = 0; i < numberOfNodes; i++) {
      nodes[i] = node.getChild(Integer.toString(i));
    }
    return nodes;
  }

  /**
   * Follow the path and get the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @return The last node in the path.
   */
  public static DefaultNode getNode(final DefaultNode node, final Path path) {
    Checks.ARGUMENT.notNull(node, "node");
    Checks.ARGUMENT.notNull(path, "path");
    return getLastAt(node, path, 0);
  }

  /**
   * Follow the path and remove the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @return The removed node.
   */
  public static DefaultNode removeLast(final DefaultNode node, final Path path) {
    Checks.ARGUMENT.notNull(node, "node");
    Checks.ARGUMENT.notNull(path, "path");
    return removeLastAt(node, path, 0);
  }

  /**
   * Recursively follow the path and add the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param index
   *          The path node index.
   * @param enforce
   *          The enforcement mode.
   */
  private static DefaultNode addAt(final DefaultNode node, final Path path, final int index, final Enforce enforce) {
    final String name = path.getName(index);
    if (index == path.getLength() - 1) {
      // at end of path
      return node.createChild(name);
    }
    DefaultNode child;
    switch (enforce) {
      case STRICT:
        child = node.getChild(name);
        break;
      case LENIENT:
        child = node.findChild(name);
        if (child == null) {
          child = node.createChild(name);
        }
        break;
      default:
        child = null;
        Checks.ARGUMENT.invalid(enforce, "enforce");
    }
    return addAt(child, path, index + 1, enforce);
  }

  /**
   * Recursively follow the path and find the value of the last node in the
   * path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param index
   *          The path node index.
   * @return The value, or <code>null</code> if it does not exist..
   */
  private static DefaultNode findLastAt(final DefaultNode node, final Path path, final int index) {
    if (index == path.getLength()) {
      // past end of path
      return node;
    }
    final String name = path.getName(index);
    final DefaultNode child = node.findChild(name);
    if (child == null) {
      // not found
      return null;
    }
    return findLastAt(child, path, index + 1);
  }

  /**
   * Recursively follow the path and get the value of the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param index
   *          The path node index.
   * @return The value.
   */
  private static DefaultNode getLastAt(final DefaultNode node, final Path path, final int index) {
    if (index == path.getLength()) {
      // past end of path
      return node;
    }
    final String name = path.getName(index);
    final DefaultNode child = node.getChild(name);
    return getLastAt(child, path, index + 1);
  }

  /**
   * Recursively follow the path and remove the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param index
   *          The path node index.
   * @return The removed node.
   * @throws IllegalArgumentException
   *           When path can not be removed because index does not exist..
   */
  private static DefaultNode removeLastAt(final DefaultNode node, final Path path, final int index) {
    final String name = path.getName(index);
    final DefaultNode child = node.getChildren().get(name);
    if (child == null) {
      throw ThrowableBuilder.createRuntimeExceptionBuilder() //
          .method("removeLastAt") //
          .message("path can not be removed because child does not exist") //
          .identity("node", node) //
          .field("path", path.toString()) //
          .field("index", Integer.toString(index)) //
          .field("name", name) //
          .build();
    }
    if (index == path.getLength() - 1) {
      // at end of path
      return node.getChildren().remove(name);
    }
    // traverse path
    return removeLastAt(child, path, index + 1);
  }

  private DefaultNodeUtils() {
  }
}
