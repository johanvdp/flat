package nl.jvdploeg.flat;

public interface Change {

    ChangeAction getAction();

    String getNewValue();

    String getOldValue();

    Path getPath();
}
