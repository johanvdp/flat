package nl.jvdploeg.flat.application;

import java.util.Collections;
import java.util.List;

import nl.jvdploeg.flat.message.Message;

public class DefaultResponse implements Response {

    private final String identifier;
    private final List<Message> messages;

    public DefaultResponse(final String identifier, final List<Message> messages) {
        this.identifier = identifier;
        this.messages = Collections.unmodifiableList(messages);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (identifier == null ? 0 : identifier.hashCode());
        result = prime * result + (messages == null ? 0 : messages.hashCode());
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
        final DefaultResponse other = (DefaultResponse) obj;
        if (identifier == null) {
            if (other.identifier != null) {
                return false;
            }
        } else if (!identifier.equals(other.identifier)) {
            return false;
        }
        if (messages == null) {
            if (other.messages != null) {
                return false;
            }
        } else if (!messages.equals(other.messages)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + identifier + ", " + messages + "]";
    }
}
