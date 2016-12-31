package nl.jvdploeg.flat.application;

import nl.jvdploeg.flat.message.Message;

import java.util.List;

public interface Response extends Identifier {

  List<Message> getMessages();

  boolean isSuccessful();
}
