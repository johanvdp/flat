package nl.jvdploeg.flat.application;

import nl.jvdploeg.flat.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResponseBuilder {

  private final List<Message> messages = new ArrayList<>();

  public ResponseBuilder() {
  }

  public void add(List<Message> messages) {
    this.messages.addAll(messages);
  }

  public Response build(boolean successful) {
    String identifier = UUID.randomUUID().toString();
    DefaultResponse response = new DefaultResponse(identifier, successful, messages);
    return response;
  }
}