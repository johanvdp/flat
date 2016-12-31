package nl.jvdploeg.flat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NodeUtils {

  private static final Logger LOG = LoggerFactory.getLogger(NodeUtils.class);

  /**
   * Follow the path and add (create) the last node in the path. Creating only the last node when
   * {@link Enforcement#STRICT} or all nodes when {@link Enforcement#LENIENT}.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   * @param enforcement
   *          The enforcement mode.
   */
  public static Node addLast(final Node node, final Path path, final Enforcement enforcement) {
    return addLastAt(node, path, 0, enforcement);
  }

  public static Node[] getChildNodeArray(final Node node) {
    final int numberOfNodes = Integer.valueOf(node.getValue());
    final Node[] nodes = new Node[numberOfNodes];
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
  public static Node getNode(final Node node, final Path path) {
    return getLastAt(node, path, 0);
  }

  /**
   * Follow the path and remove the last node in the path.
   *
   * @param node
   *          The node.
   * @param path
   *          The path.
   */
  public static void removeLast(final Node node, final Path path) {
    removeLastAt(node, path, 0);
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
   * @param enforcement
   *          The enforcement mode.
   */
  private static Node addLastAt(final Node node, final Path path, final int index,
      final Enforcement enforcement) {
    final String name = path.getName(index);
    if (index == path.getLength() - 1) {
      // at end of path
      return node.createChild(name);
    }
    final Node child;
    if (enforcement == Enforcement.STRICT) {
      child = node.getChild(name);
    } else {
      child = node.getOrCreateChild(name);
    }
    return addLastAt(child, path, index + 1, enforcement);
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
  private static Node getLastAt(final Node node, final Path path, final int index) {
    if (index == path.getLength()) {
      // past end of path
      return node;
    }
    final String name = path.getName(index);
    final Node child = node.getChild(name);
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
   * @throws IllegalArgumentException
   *           When path can not be removed because index does not exist..
   */
  private static void removeLastAt(final Node node, final Path path, final int index) {
    final String name = path.getName(index);
    if (index == path.getLength() - 1) {
      // at end of path
      final Node child = node.getChildren().remove(name);
      if (child == null) {
        final String error = String.format("%s can not be removed because %s (%d) does not exist",
            path, name, index);
        LOG.error(error);
        throw new IllegalArgumentException(error);
      }
      return;
    }
    final Node child = node.getChildren().get(name);
    if (child == null) {
      final String error = String.format("%s can not be removed because %s (%d) does not exist",
          path, name, index);
      LOG.error(error);
      throw new IllegalArgumentException(error);
    }
    removeLastAt(child, path, index + 1);
  }

}
