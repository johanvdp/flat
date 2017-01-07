package nl.jvdploeg.flat;

/**
 * Reflects a change in the {@link Model}.
 */
public interface Change {

  ChangeAction getAction();

  String getNewValue();

  String getOldValue();

  Path getPath();
}
