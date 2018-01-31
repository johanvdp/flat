// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

/**
 * Enforcement mode.
 */
public enum Enforce {
  /**
   * Lenient enforcement mode.<br>
   * Create nodes when they are missing.
   */
  LENIENT,
  /**
   * Strict enforcement mode.<br>
   * Throw exceptions when nodes are missing.
   */
  STRICT;
}
