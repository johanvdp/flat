// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.jvdploeg.exception.Checks;
import nl.jvdploeg.exception.ErrorBuilder;
import nl.jvdploeg.flat.Node;
import nl.jvdploeg.flat.Version;

/**
 * Part of the hierarchical structure of {@link DefaultNode}s and values.
 */
public final class DefaultNode implements Node<DefaultNode> {

  private static final Version INITIAL = new NumberedVersion(0);

  private static final String TYPE_NAME = DefaultNode.class.getSimpleName();
  private final Map<String, DefaultNode> children = new HashMap<>();
  private String value;
  private Version version = INITIAL;

  /** Default constructor. */
  public DefaultNode() {
  }

  /** Copy constructor. */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public DefaultNode(final Node<?> node) {
    Checks.ARGUMENT.notNull(node, "node");
    version = node.getVersion();
    value = node.getValue();
    final Map<String, Node<?>> otherChildren = (Map<String, Node<?>>) node.getChildren();
    for (final Entry<String, Node<?>> child : otherChildren.entrySet()) {
      final String childName = child.getKey();
      final Node childValue = child.getValue();
      children.put(childName, new DefaultNode(childValue));
    }
  }

  /**
   * Create child node.
   *
   * @param name
   *          Child node name.
   * @return The node.
   * @throws IllegalArgumentException
   *           When child node already exist.
   */
  // @Override
  public DefaultNode createChild(final String name) {
    Checks.ARGUMENT.notNull(name, "name");
    DefaultNode child = children.get(name);
    if (child == null) {
      child = new DefaultNode();
      children.put(name, child);
    } else {
      throw new ErrorBuilder() //
          .method("createChild") //
          .message("child already exist") //
          .identity("instance", this) //
          .field("childName", name) //
          .build();
    }
    return child;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DefaultNode other = (DefaultNode) obj;
    if (version == null) {
      if (other.getVersion() != null) {
        return false;
      }
    } else if (!version.equals(other.getVersion())) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    if (!children.equals(other.children)) {
      return false;
    }
    return true;
  }

  /**
   * Find child node.
   *
   * @param name
   *          Child node name.
   * @return The node, or <code>null</code> if not found.
   */
  @Override
  public DefaultNode findChild(final String name) {
    Checks.ARGUMENT.notNull(name, "name");
    final DefaultNode child = children.get(name);
    return child;
  }

  /**
   * Get child node.
   *
   * @param name
   *          Child node name.
   * @return The node.
   * @throws IllegalArgumentException
   *           When child node does not exist.
   */
  @Override
  public DefaultNode getChild(final String name) {
    Checks.ARGUMENT.notNull(name, "name");
    final DefaultNode child = children.get(name);
    Checks.ARGUMENT.notNull(child, String.format("child %s", name));
    return child;
  }

  /**
   * Get child node name map.
   *
   * @return The child node name map.
   */
  @Override
  public Map<String, DefaultNode> getChildren() {
    return children;
  }

  /**
   * Get node value.
   *
   * @return The node value.
   */
  @Override
  public String getValue() {
    return value;
  }

  /**
   * Get node version.
   *
   * @return The node version.
   */
  @Override
  public Version getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + children.hashCode();
    result = prime * result + (value == null ? 0 : value.hashCode());
    result = prime * result + (version == null ? 0 : version.hashCode());
    return result;
  }

  /**
   * Set node value.
   *
   * @param newValue
   *          The value.
   * @return The old value.
   */
  public String setValue(final String newValue) {
    final String oldValue = value;
    value = newValue;
    return oldValue;
  }

  /**
   * Set node version.
   *
   * @param newVersion
   *          The version.
   * @return The old version.
   */
  public Version setVersion(final Version newVersion) {
    final Version oldVersion = version;
    version = newVersion;
    return oldVersion;
  }

  @Override
  public String toString() {
    return TYPE_NAME + "[version=" + version + ",value=" + value + ",children=" + children + "]";
  }
}
