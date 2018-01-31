// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.impl;

import nl.jvdploeg.flat.Version;

/**
 * Simple numbered {@link Version} implementation.
 */
public final class NumberedVersion implements Version {

  private final long number;

  public NumberedVersion(final long number) {
    this.number = number;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final NumberedVersion other = (NumberedVersion) obj;
    if (number != other.number) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (number ^ number >>> 32);
    return result;
  }

  @Override
  public String toString() {
    return Long.toString(number);
  }
}
