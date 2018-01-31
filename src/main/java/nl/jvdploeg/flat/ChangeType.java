// The author disclaims copyright to this source code.
package nl.jvdploeg.flat;

/**
 * The type of {@link Change}.
 */
public enum ChangeType {
  /**
   * Follow the path and add the last node in the path.
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
