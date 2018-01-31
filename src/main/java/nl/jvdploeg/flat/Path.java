// The author disclaims copyright to this source code.
package nl.jvdploeg.flat;

import java.util.Arrays;

import nl.jvdploeg.exception.Checks;

/**
 * Path in the {@link Node} hierarchy.
 */
public final class Path implements Comparable<Path> {

  private static final String TYPE_NAME = Path.class.getSimpleName();

  /** Empty path (can not be created otherwise). */
  public static final Path EMPTY = new Path();

  /**
   * Compare {@link Path} to each other, starting at the given index.<br>
   * In order: ["A"] < ["A", "A"]
   */
  private static int compareTo(final Path one, final Path other, final int index) {
    if (one == other) {
      return 0;
    }
    if (index >= one.path.length && index >= other.path.length) {
      return 0;
    }
    if (index >= one.path.length) {
      return -1;
    }
    if (index >= other.path.length) {
      return 1;
    }
    final String oneStep = one.path[index];
    final String otherStep = other.path[index];
    final int result = oneStep.compareTo(otherStep);
    if (result == 0) {
      return compareTo(one, other, index + 1);
    }
    return result;
  }

  private final String[] path;

  public Path(final String path) {
    this(new String[] { path });
  }

  public Path(final String... path) {
    Checks.ARGUMENT.notNull(path, "path");
    final String[] clone = new String[path.length];
    for (int i = 0; i < path.length; i++) {
      final String segment = path[i];
      Checks.ARGUMENT.notNull(segment, String.format("path %1$s segment", Arrays.toString(path)));
      clone[i] = segment;
    }
    this.path = clone;
  }

  /** Re-use the one and only {@link #EMPTY} path. */
  private Path() {
    this(new String[0]);
  }

  /**
   * Compare {@link Path} to each other.<br>
   * In order: null < ["A"] < ["A", "A"]
   */
  @Override
  public int compareTo(final Path other) {
    return compareTo(this, other, 0);
  }

  public Path createChildPath(final Path childPath) {
    Checks.ARGUMENT.notNull(childPath, "childPath");
    final int childPathLength = childPath.path.length;
    final String[] newPath = new String[path.length + childPathLength];
    System.arraycopy(path, 0, newPath, 0, path.length);
    // copy with length 0 works
    System.arraycopy(childPath.path, 0, newPath, path.length, childPath.path.length);
    return new Path(newPath);
  }

  public Path createChildPath(final String childName) {
    Checks.ARGUMENT.notNull(childName, "childName");
    final String[] childPath = new String[path.length + 1];
    System.arraycopy(path, 0, childPath, 0, path.length);
    childPath[path.length] = childName;
    return new Path(childPath);
  }

  public Path createParentPath() {
    final String[] parentPath = new String[path.length - 1];
    System.arraycopy(path, 0, parentPath, 0, path.length - 1);
    return new Path(parentPath);
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
    final Path other = (Path) obj;
    return this.compareTo(other) == 0;
  }

  public String getLastNodeName() {
    return path[path.length - 1];
  }

  public int getLength() {
    return path.length;
  }

  public String getName(final int index) {
    return path[index];
  }

  public String[] getPath() {
    final String[] clone = new String[path.length];
    System.arraycopy(path, 0, clone, 0, path.length);
    return clone;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(path);
  }

  @Override
  public String toString() {
    return TYPE_NAME + "[path=" + Arrays.toString(path) + "]";
  }
}
