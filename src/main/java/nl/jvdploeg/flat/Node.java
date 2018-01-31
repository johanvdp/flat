// The author disclaims copyright to this source code.
package nl.jvdploeg.flat;

import java.util.Map;

/**
 * {@link Node}s in a hierarchical structure.
 */
public interface Node<T extends Node<T>> {

  /**
   * Find child node.
   *
   * @param name
   *          Child node name.
   * @return The node, or <code>null</code> if not found.
   */
  T findChild(String name);

  /**
   * Get child node.
   *
   * @param name
   *          Child node name.
   * @return The node.
   * @throws IllegalArgumentException
   *           When child node does not exist.
   */
  T getChild(String name);

  /**
   * Get child node name map.
   *
   * @return The child node name map.
   */
  Map<String, T> getChildren();

  /**
   * Get node value.
   *
   * @return The node value.
   */
  String getValue();

  /**
   * Get node version.
   *
   * @return The node version.
   */
  Version getVersion();
}
