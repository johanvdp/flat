package nl.jvdploeg.flat.application;

import java.util.Collections;
import java.util.Map;

public class DefaultCommand implements Command {

    private final String type;
    private final boolean test;
    private final Map<String, String> parameters;

    public DefaultCommand(final String type, final boolean test, final Map<String, String> parameters) {
        this.type = type;
        this.test = test;
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "[" + type + ", " + test + ", " + parameters + "]";
    }

    @Override
    public boolean isTest() {
        return test;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (parameters == null ? 0 : parameters.hashCode());
        result = prime * result + (test ? 1231 : 1237);
        result = prime * result + (type == null ? 0 : type.hashCode());
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
        final DefaultCommand other = (DefaultCommand) obj;
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (test != other.test) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
