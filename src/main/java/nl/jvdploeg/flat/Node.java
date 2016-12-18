package nl.jvdploeg.flat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {

    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private final Map<String, Node> children;
    private String value;

    public Node() {
        children = new HashMap<>();
    }

    public Node(final Node node) {
        value = node.value;
        children = new HashMap<>();
        for (final Entry<String, Node> child : node.children.entrySet()) {
            final String childName = child.getKey();
            final Node childValue = child.getValue();
            children.put(childName, new Node(childValue));
        }
    }

    /**
     * Create child node.
     *
     * @param name
     *            Child node name.
     * @return The node.
     * @throws IllegalArgumentException
     *             When child node already exist.
     */
    public Node createChild(final String name) {
        Node child = children.get(name);
        if (child == null) {
            child = new Node();
            children.put(name, child);
        } else {
            final String error = String.format("child %s already exist", name);
            LOG.error(error);
            throw new IllegalArgumentException(error);
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
        final Node other = (Node) obj;
        if (children == null) {
            if (other.children != null) {
                return false;
            }
        } else if (!children.equals(other.children)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
     * Find child node.
     *
     * @param name
     *            Child node name.
     * @return The node, or <code>null</code> if not found.
     */
    public Node findChild(final String name) {
        final Node child = children.get(name);
        return child;
    }

    /**
     * Get child node.
     *
     * @param name
     *            Child node name.
     * @return The node.
     * @throws IllegalArgumentException
     *             When child node does not exist.
     */
    public Node getChild(final String name) {
        final Node child = children.get(name);
        if (child == null) {
            final String error = String.format("child %s does not exist", name);
            LOG.error(error);
            throw new IllegalArgumentException(error);
        }
        return child;
    }

    /**
     * Get child node name map.
     *
     * @return The child node name map.
     */
    public Map<String, Node> getChildren() {
        return children;
    }

    /**
     * Get or create child node.
     *
     * @param name
     *            Child node name.
     * @return The node.
     */
    public Node getOrCreateChild(final String name) {
        Node child = children.get(name);
        if (child == null) {
            child = new Node();
            children.put(name, child);
        }
        return child;
    }

    /**
     * Get node value.
     *
     * @return The node value.
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (children == null ? 0 : children.hashCode());
        result = prime * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    /**
     * Set node value.
     *
     * @param value
     *            The value.
     * @return The old value.
     */
    public String setValue(final String value) {
        final String oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (value != null) {
            builder.append(value);
        }
        if (value != null && children != null && !children.isEmpty()) {
            builder.append(", ");
        }
        if (children != null && !children.isEmpty()) {
            builder.append(children);
        }
        builder.append("]");
        return builder.toString();
    }
}
