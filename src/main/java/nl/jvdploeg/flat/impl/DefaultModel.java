// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import nl.jvdploeg.exception.Checks;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.Version;

public final class DefaultModel implements Model<DefaultNode> {

  private static final String TYPE_NAME = DefaultModel.class.getSimpleName();
  private final Enforce enforce;
  private final String name;
  private final DefaultNode root;

  /**
   * Create a model.
   *
   * @param name
   *          The name of the model.
   * @param enforce
   *          The node creation strategy.
   */
  public DefaultModel(final String name, final Enforce enforce) {
    Checks.ARGUMENT.notNull(name, "name");
    Checks.ARGUMENT.notNull(enforce, "enforce");
    this.name = name;
    this.enforce = enforce;
    root = new DefaultNode();
  }

  /**
   * Create a model by cloning the nodes in another.
   *
   * @param name
   *          The name of the model.
   * @param enforce
   *          The node creation strategy.
   * @param model
   *          The model containing the nodes to clone.
   */
  public DefaultModel(final String name, final Enforce enforce, final Model<?> model) {
    Checks.ARGUMENT.notNull(name, "name");
    Checks.ARGUMENT.notNull(enforce, "enforce");
    Checks.ARGUMENT.notNull(model, "model");
    this.name = name;
    this.enforce = enforce;
    root = new DefaultNode(model.getRoot());
  }

  /**
   * Follow the path and add (create) the last node in the path.
   *
   * @param path
   *          The path.
   */
  @Override
  public void add(final Path path) {
    DefaultNodeUtils.add(root, path, enforce);
  }

  @Override
  public DefaultNode createChild(final Path path) {
    final Path parent = path.createParentPath();
    final String childName = path.getLastNodeName();
    final DefaultNode node = getNode(parent);
    final DefaultNode childNode = node.createChild(childName);
    return childNode;
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
  @Override
  public DefaultNode findNode(final Path path) {
    final DefaultNode node = DefaultNodeUtils.findNode(root, path);
    return node;
  }

  @Override
  public String getName() {
    return name;
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
  @Override
  public DefaultNode getNode(final Path path) {
    final DefaultNode node = DefaultNodeUtils.getNode(root, path);
    return node;
  }

  @Override
  public DefaultNode getRoot() {
    return root;
  }

  /**
   * Follow the path and get the value of the last node in the path.
   *
   * @param path
   *          The path.
   * @return The value.
   */
  @Override
  public String getValue(final Path path) {
    final DefaultNode node = DefaultNodeUtils.getNode(root, path);
    final String value = node.getValue();
    return value;
  }

  /**
   * Follow the path and get the version of the last node in the path.
   *
   * @param path
   *          The path.
   * @return The value.
   */
  @Override
  public Version getVersion(final Path path) {
    final DefaultNode node = DefaultNodeUtils.getNode(root, path);
    final Version value = node.getVersion();
    return value;
  }

  /**
   * Follow the path and remove the last node in the path.
   *
   * @param path
   *          The path.
   */
  @Override
  public void remove(final Path path) {
    DefaultNodeUtils.removeLast(root, path);
  }

  /**
   * Follow the path and set the value of the last node in the path.
   *
   * @param path
   *          The path.
   * @param value
   *          The new value.
   * @return The old value.
   */
  @Override
  public String setValue(final Path path, final String newValue) {
    final DefaultNode node = DefaultNodeUtils.getNode(root, path);
    final String oldValue = node.setValue(newValue);
    return oldValue;
  }

  /**
   * Follow the path and set the version of the last node in the path.
   *
   * @param path
   *          The path.
   * @param version
   *          The new version.
   * @return The old version.
   */
  @Override
  public Version setVersion(final Path path, final Version newVersion) {
    final DefaultNode node = DefaultNodeUtils.getNode(root, path);
    final Version oldVersion = node.setVersion(newVersion);
    return oldVersion;
  }

  @Override
  public String toString() {
    return TYPE_NAME + "[name=" + name + ",enforce=" + enforce + ",root=" + root + "]";
  }
}
