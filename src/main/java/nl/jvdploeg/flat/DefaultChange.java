package nl.jvdploeg.flat;

public class DefaultChange implements Change {

  public static Change add(final Path path) {
    return new DefaultChange(ChangeAction.ADD, path, null, null);
  }

  public static Change remove(final Path path) {
    return new DefaultChange(ChangeAction.REMOVE, path, null, null);
  }

  public static Change set(final Path path, final String value) {
    return new DefaultChange(ChangeAction.SET, path, null, value);
  }

  public static Change set(final Path path, final String oldValue, final String value) {
    return new DefaultChange(ChangeAction.SET, path, oldValue, value);
  }

  private final ChangeAction action;
  private final String newValue;
  private final String oldValue;
  private final Path path;

  /**
   * Constructor.
   * 
   * @param action
   *          The type of change.
   * @param path
   *          The location.
   * @param oldValue
   *          The old value.
   * @param newValue
   *          The new value.
   */
  public DefaultChange(final ChangeAction action, final Path path, final String oldValue,
      final String newValue) {
    this.action = action;
    this.path = path;
    this.oldValue = oldValue;
    this.newValue = newValue;
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
    final Change other = (Change) obj;
    if (action != other.getAction()) {
      return false;
    }
    if (path == null) {
      if (other.getPath() != null) {
        return false;
      }
    } else if (!path.equals(other.getPath())) {
      return false;
    }
    if (oldValue == null) {
      if (other.getOldValue() != null) {
        return false;
      }
    } else if (!oldValue.equals(other.getOldValue())) {
      return false;
    }
    if (newValue == null) {
      if (other.getNewValue() != null) {
        return false;
      }
    } else if (!newValue.equals(other.getNewValue())) {
      return false;
    }
    return true;
  }

  @Override
  public ChangeAction getAction() {
    return action;
  }

  @Override
  public String getNewValue() {
    return newValue;
  }

  @Override
  public String getOldValue() {
    return oldValue;
  }

  @Override
  public Path getPath() {
    return path;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (action == null ? 0 : action.hashCode());
    result = prime * result + (path == null ? 0 : path.hashCode());
    result = prime * result + (oldValue == null ? 0 : oldValue.hashCode());
    result = prime * result + (newValue == null ? 0 : newValue.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "[" + action + ", " + path + ", " + oldValue + ", " + newValue + "]";
  }
}
