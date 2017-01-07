package nl.jvdploeg.flat.application;

import java.util.List;
import nl.jvdploeg.flat.message.Message;

public interface Response extends Identifier {

  List<Message> getMessages();

  boolean isSuccessful();
}
