// The author disclaims copyright to this source code.
package nl.jvdploeg.flat;

/**
 * Provides access to a hierarchical structure of {@link Node}s and values.
 */
public interface Model<T extends Node<T>> {

  /**
   * Follow the path and add (create) the last node in the path.
   *
   * @param path
   *          The path.
   */
  void add(Path path);

  /**
   * Create child node.
   *
   * @param path
   *          The path.
   * @return The node.
   */
  T createChild(Path path);

  /**
   * Follow the path and find the last node in the path.
   *
   * @param path
   *          The path.
   * @return The last node in the path, or <code>null</code> if it does not
   *         exist.
   */
  T findNode(Path path);

  String getName();

  /**
   * Follow the path and get the last node in the path.
   *
   * @param path
   *          The path.
   * @return The last node in the path.
   */
  T getNode(Path path);

  T getRoot();

  /**
   * Follow the path and get the value of the last node in the path.
   *
   * @param path
   *          The path.
   * @return The value.
   */
  String getValue(Path path);

  /**
   * Follow the path and get the version of the last node in the path.
   *
   * @param path
   *          The path.
   * @return The version.
   */
  Version getVersion(Path path);

  /**
   * Follow the path and remove the last node in the path.
   *
   * @param path
   *          The path.
   */
  void remove(Path path);

  /**
   * Follow the path and set the value of the last node in the path.
   *
   * @param path
   *          The path.
   * @param value
   *          The new value.
   * @return The old value.
   */
  String setValue(Path path, String value);

  /**
   * Follow the path and set the version of the last node in the path.
   *
   * @param path
   *          The path.
   * @param version
   *          The new version.
   * @return The old version.
   */
  Version setVersion(Path path, Version version);
}
