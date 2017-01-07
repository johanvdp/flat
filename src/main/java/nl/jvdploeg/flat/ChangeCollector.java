package nl.jvdploeg.flat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class ChangeCollector {

  private final List<Change> changes = new ArrayList<>();

  public ChangeCollector() {
  }

  public void addChange(final Change other) {
    changes.add(other);
  }

  public void addChanges(final List<Change> other) {
    changes.addAll(other);
  }

  /**
   * Add {@link Node} to model.
   * 
   * @param path
   *          The location.
   * @param node
   *          The node.
   */
  public void addNode(final Path path, final Node node) {
    final String value = node.getValue();
    if (path.getLength() > 0) {
      changes.add(DefaultChange.add(path));
      if (value != null) {
        changes.add(DefaultChange.set(path, value));
      }
    }
    final Set<Entry<String, Node>> children = node.getChildren().entrySet();
    for (final Entry<String, Node> child : children) {
      final String childName = child.getKey();
      final Node childNode = child.getValue();
      final Path childPath = path.createChildPath(childName);
      addNode(childPath, childNode);
    }
  }

  public List<Change> getChanges() {
    return changes;
  }

  public void removeNode(final Path path) {
    changes.add(DefaultChange.remove(path));
  }

  /**
   * Set node value.
   * 
   * @param path
   *          The location.
   * @param oldValue
   *          The old value.F
   * @param newValue
   *          The new value.
   */
  public void setNodeValue(final Path path, final String oldValue, final String newValue) {
    if (!newValue.equals(oldValue)) {
      changes.add(DefaultChange.set(path, oldValue, newValue));
    }
  }

  /**
   * Set node array value.
   * 
   * @param path
   *          The location.
   * @param oldValue
   *          The old value.
   * @param newValue
   *          The new value.
   */
  public void setNodeValue(final Path path, final String[] oldValue, final String[] newValue) {
    final int oldLength = oldValue.length;
    final int newLength = newValue.length;
    setNodeValue(path, Integer.toString(oldLength), Integer.toString(newLength));
    final int length = Math.max(newLength, oldLength);
    for (int i = 0; i < length; i++) {
      final Path childPath = path.createChildPath(Integer.toString(i));
      if (i < oldLength && i < newLength) {
        // both exist
        setNodeValue(childPath, oldValue[i], newValue[i]);
      } else if (i < oldLength) {
        // removed
        changes.add(DefaultChange.remove(childPath));
      } else {
        // added
        changes.add(DefaultChange.add(childPath));
        changes.add(DefaultChange.set(childPath, newValue[i]));
      }
    }
  }
}
