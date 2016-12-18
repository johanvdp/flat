package nl.jvdploeg.flat.job;

import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;

public interface Job {

    /** Execute the {@link Job} given the provided {@link Model}. */
    void execute(Model model);

    /** Get {@link Job} execution resulting {@link Change}s. */
    List<Change> getChanges();

    /** Get {@link Job} execution resulting {@link Job}s. */
    List<Job> getJobs();
}
