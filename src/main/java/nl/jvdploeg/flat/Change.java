// The author disclaims copyright to this source code.
package nl.jvdploeg.flat;

/**
 * Represents a {@link Change} to the {@link Model}.
 */
public interface Change {

  /**
   * The type of change.<br>
   * Never <code>null</code>.
   */
  ChangeType getAction();

  /**
   * What changed.<br>
   * Never <code>null</code>.
   */
  Path getPath();

  /**
   * The new value.
   */
  String getValue();

  /**
   * The old version.
   */
  Version getVersion();
}
