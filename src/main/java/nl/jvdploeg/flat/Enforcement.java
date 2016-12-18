package nl.jvdploeg.flat;

public enum Enforcement {
    /**
     * Lenient enforcement.<br>
     * Create nodes and values when they are missing.
     */
    LENIENT,
    /**
     * Strict enforcement.<br>
     * Throw exceptions when nodes or values are missing.
     */
    STRICT;
}