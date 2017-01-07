package nl.jvdploeg.flat.application;

import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.message.Message;

public interface Response extends Identifier {

  List<Change> getChanges();

  List<Message> getMessages();

  boolean isSuccessful();
}
