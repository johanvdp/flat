package nl.jvdploeg.flat.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.message.Message;

public class ResponseBuilder {

  private final List<Message> messages = new ArrayList<>();
  private final List<Change> changes = new ArrayList<>();

  public ResponseBuilder() {
  }

  public void addChanges(List<Change> changes) {
    this.changes.addAll(changes);
  }

  public void addMessages(List<Message> messages) {
    this.messages.addAll(messages);
  }

  /**
   * Build response.
   * 
   * @param successful
   *          True to indicate success.
   * @return The built reponse.
   */
  public Response build(boolean successful) {
    String identifier = UUID.randomUUID().toString();
    DefaultResponse response = new DefaultResponse(identifier, successful, messages, changes);
    return response;
  }
}
