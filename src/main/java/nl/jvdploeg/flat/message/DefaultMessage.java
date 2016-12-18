package nl.jvdploeg.flat.message;

import java.util.Arrays;
import java.util.List;

public class DefaultMessage implements Message {

    public static Message findById(final List<Message> list, final Message one) {
        for (final Message candidate : list) {
            if (candidate.getIdentifier().equals(one.getIdentifier())) {
                return candidate;
            }
        }
        return null;
    }

    private final String[] arguments;
    private final String id;
    private final String message;
    private final Severity severity;

    public DefaultMessage(final String id, final Severity severity, final String message, final String... arguments) {
        this.id = id;
        this.severity = severity;
        this.message = message;
        this.arguments = arguments;
    }

    @Override
    public String[] getArguments() {
        final String[] clone = new String[arguments.length];
        System.arraycopy(arguments, 0, clone, 0, arguments.length);
        return clone;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(arguments);
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (message == null ? 0 : message.hashCode());
        result = prime * result + (severity == null ? 0 : severity.hashCode());
        return result;
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
        final DefaultMessage other = (DefaultMessage) obj;
        if (!Arrays.equals(arguments, other.arguments)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        if (severity != other.severity) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + id + ", " + severity + ", " + message + ", " + Arrays.toString(arguments) + "]";
    }
}
