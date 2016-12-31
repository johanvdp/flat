package nl.jvdploeg.flat.message;

import nl.jvdploeg.flat.application.Identifier;

public interface Message extends Identifier {

  String[] getArguments();

  String getMessage();

  Severity getSeverity();
}
