package nl.jvdploeg.flat.application;

import java.util.Collections;
import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.message.Message;

public class DefaultResponse implements Response {

  private final String identifier;
  private final boolean successful;
  private final List<Message> messages;
  private final List<Change> changes;

  /**
   * Constructor.
   * 
   * @param identifier
   *          Unique response identifier.
   * @param successful
   *          True to indicate success.
   * @param messages
   *          Response messages.
   */
  public DefaultResponse(final String identifier, final boolean successful,
      final List<Message> messages, final List<Change> changes) {
    this.identifier = identifier;
    this.successful = successful;
    this.messages = Collections.unmodifiableList(messages);
    this.changes = Collections.unmodifiableList(changes);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DefaultResponse other = (DefaultResponse) obj;
    if (changes == null) {
      if (other.changes != null) {
        return false;
      }
    } else if (!changes.equals(other.changes)) {
      return false;
    }
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
    if (successful != other.successful) {
      return false;
    }
    return true;
  }

  @Override
  public List<Change> getChanges() {
    return changes;
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
    result = prime * result + ((changes == null) ? 0 : changes.hashCode());
    result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
    result = prime * result + ((messages == null) ? 0 : messages.hashCode());
    result = prime * result + (successful ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean isSuccessful() {
    return successful;
  }

  @Override
  public String toString() {
    return "[" + identifier + ", " + successful + ", " + messages + ", " + changes + "]";
  }
}
