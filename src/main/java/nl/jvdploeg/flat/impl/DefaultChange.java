// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import nl.jvdploeg.exception.Checks;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ChangeType;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.Version;

public final class DefaultChange implements Change {

  private static final String TYPE_NAME = DefaultChange.class.getSimpleName();

  public static Change add(final Path path) {
    Checks.ARGUMENT.notNull(path, "path");
    return new DefaultChange(ChangeType.ADD, path, null, null);
  }

  public static Change add(final Path path, final String newValue) {
    Checks.ARGUMENT.notNull(path, "path");
    return new DefaultChange(ChangeType.ADD, path, null, newValue);
  }

  public static Change remove(final Path path) {
    Checks.ARGUMENT.notNull(path, "path");
    return new DefaultChange(ChangeType.REMOVE, path, null, null);
  }

  public static Change set(final Path path, final Version oldVersion, final String newValue) {
    Checks.ARGUMENT.notNull(path, "path");
    return new DefaultChange(ChangeType.SET, path, oldVersion, newValue);
  }

  private final ChangeType action;
  private final Version version;
  private final Path path;
  private final String value;

  private DefaultChange(final ChangeType action, final Path path, final Version version, final String value) {
    this.action = action;
    this.path = path;
    this.version = version;
    this.value = value;
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
    // path (can not be null)
    if (!path.equals(other.getPath())) {
      return false;
    }
    // version
    if (version == null) {
      if (other.getVersion() != null) {
        return false;
      }
    } else if (!version.equals(other.getVersion())) {
      return false;
    }
    // value
    if (value == null) {
      if (other.getValue() != null) {
        return false;
      }
    } else if (!value.equals(other.getValue())) {
      return false;
    }
    return true;
  }

  @Override
  public ChangeType getAction() {
    return action;
  }

  @Override
  public Path getPath() {
    return path;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public Version getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + action.hashCode();
    result = prime * result + path.hashCode();
    result = prime * result + (version == null ? 0 : version.hashCode());
    result = prime * result + (value == null ? 0 : value.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return TYPE_NAME + "[action=" + action + ",path=" + path + ",version=" + version + ",newValue=" + value + "]";
  }
}
