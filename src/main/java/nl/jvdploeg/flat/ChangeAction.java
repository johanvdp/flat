package nl.jvdploeg.flat;

/**
 * The type of change.
 */
public enum ChangeAction {
  /**
   * Follow the path and add (create) the last node in the path.
   */
  ADD,
  /**
   * Follow the path and remove the last node in the path.
   */
  REMOVE,
  /**
   * Follow the path and set the value of the last node in the path.
   */
  SET;
}