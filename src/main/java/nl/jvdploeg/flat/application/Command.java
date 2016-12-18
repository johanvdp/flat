package nl.jvdploeg.flat.application;

import java.util.Map;

public interface Command {

    /** The type of command. */
    String getType();

    /** Test only, do not effectuate the command. */
    boolean isTest();

    /** The command parameters. */
    Map<String, String> getParameters();
}
