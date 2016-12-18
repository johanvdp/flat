package nl.jvdploeg.flat.job;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.rule.Validators;
import nl.jvdploeg.flat.rule.Verifiers;

/**
 * A transaction defines a shielded environment. Only committing all changes to the model after validation that all changes will be successful.<br>
 * Any jobs created during job execution are executed in the same transaction in breadth first order against the same clone of the model. All changes
 * emanating as a result of each job execution are validated. Any validation failing makes job execution stop.<br>
 * Only after all jobs are executed successfully the {@link Verifiers} are run and the {@link Change}s are applied to the original model.
 */
public class Transaction {

    private static final Logger LOG = LoggerFactory.getLogger(Transaction.class);

    /**
     * Changes collected during job execution. To be applied after the transaction is succesful.
     */
    private final List<Change> changes = new ArrayList<>();

    /** Work on the clone while in the transaction. */
    private final Model clone;

    /**
     * The model. Updated with the collected changes after the transaction is successful.
     */
    private final Model model;

    /**
     * The {@link Validators} check if job execution is successful. Any error detected makes the transaction end without success.
     */
    private final Validators validators;

    /**
     * The {@link Verifiers} run after all jobs have run. The {@link Verifiers} modify the {@link Model}.
     */
    private final Verifiers verifiers;

    /**
     * Constructor.
     *
     * @param model
     *            The information source for job execution.
     * @param validators
     *            Validate job generated changes against the model..
     * @param verifiers
     *            Verify the model and maintain a verification state.
     */
    public Transaction(final Model model, final Validators validators, final Verifiers verifiers) {
        this.model = model;
        this.validators = validators;
        this.verifiers = verifiers;
        clone = new Model(model.getName() + "-" + getClass().getSimpleName(), Enforcement.STRICT, model);
    }

    /**
     * Execute {@link Job} in transaction.<br>
     *
     * Any jobs created during job execution are executed in the same transaction in breadth first order against the same clone of the model. All
     * changes emanating as a result of each job execution are validated. Any validation failing makes job execution stop. Only after all jobs are
     * executed successfully the {@link Verifiers} are run and the {@link Change}s are applied to the original model.
     */
    public boolean execute(final Job job) {
        LOG.info("> execute {}", job);
        boolean successful = false;
        // maintain job list for breadth first job execution
        final LinkedList<Job> jobs = new LinkedList<>();
        jobs.add(job);
        successful = executeBreadthFirst(jobs);
        if (successful) {
            verifiers.verify(model, changes);
            ModelUtils.applyChanges(model, changes);
        }
        LOG.info("< execute {}", successful ? "ok" : "failed");
        return successful;
    }

    /**
     * Breadth first {@link Job} execution.<br>
     * Any jobs created during job execution are also executed in breadth first order. Any changes emanating from each job execution are validated. If
     * invalid job execution is ended and marked as not successful.
     */
    private boolean executeBreadthFirst(final LinkedList<Job> jobs) {
        LOG.info("> executeBreadthFirst {}", jobs);

        while (!jobs.isEmpty()) {
            final Job job = jobs.removeFirst();

            LOG.info("  executeBreadthFirst {}", job);
            job.execute(clone);
            final List<Change> jobChanges = job.getChanges();

            final Model status = validators.validate(clone, jobChanges);
            if (status.getRoot().getChildren().size() > 0) {
                LOG.info("< executeBreadthFirst failed {}", status);
                return false;
            }

            changes.addAll(jobChanges);
            ModelUtils.applyChanges(clone, jobChanges);

            final List<Job> chainedJobs = job.getJobs();
            jobs.addAll(chainedJobs);
        }
        // job execution chain successful
        LOG.info("< executeBreadthFirst ok");
        return true;
    }
}
