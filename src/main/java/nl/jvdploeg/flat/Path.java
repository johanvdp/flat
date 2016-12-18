package nl.jvdploeg.flat;

import java.util.Arrays;

public class Path {

    private final String[] path;

    public Path() {
        this(new String[0]);
    }

    public Path(final String path) {
        this(new String[] { path });
    }

    public Path(final String[] path) {
        final String[] clone = new String[path.length];
        System.arraycopy(path, 0, clone, 0, path.length);
        this.path = clone;
    }

    public Path createChildPath(final String childName) {
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
        if (!Arrays.equals(path, other.path)) {
            return false;
        }
        return true;
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
        return Arrays.toString(path);
    }
}
