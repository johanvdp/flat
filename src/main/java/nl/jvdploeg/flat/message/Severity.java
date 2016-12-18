package nl.jvdploeg.flat.message;

public enum Severity {
    /** Production can not continue. The condition is recoverable. */
    ERROR,
    /** Production can not continue. The condition is not recoverable. */
    FATAL,
    /** Informational message. Production is not affected. */
    INFO,
    /**
     * Warning message. Production is degraded (e.g., in quantity, speed, or quality). The condition is recoverable.
     */
    WARNING;
}