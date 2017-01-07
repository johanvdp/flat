package nl.jvdploeg.flat.job;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.application.ResponseBuilder;
import nl.jvdploeg.flat.message.Message;
import nl.jvdploeg.flat.validation.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A transaction defines a shielded environment. Only committing all changes to the model after
 * validation that all changes will be successful.<br>
 * Any jobs created during job execution are executed in the same transaction in breadth first order
 * against the same clone of the model. All changes emanating as a result of each job execution are
 * validated. Any validation failing makes job execution stop.<br>
 * Only after all jobs are executed successfully the {@link Change}s are applied to the original
 * model.
 */
public class Transaction {

  private static final Logger LOG = LoggerFactory.getLogger(Transaction.class);

  /**
   * Changes collected during job execution. To be applied after the transaction is succesful.
   */
  private final List<Change> changes = new ArrayList<>();

  /**
   * Build response during job execution.
   */
  private final ResponseBuilder responseBuilder = new ResponseBuilder();

  /** Work on the clone while in the transaction. */
  private final Model clone;

  /**
   * The model. Updated with the collected changes after the transaction is successful.
   */
  private final Model model;

  /**
   * The {@link Validators} check if job execution is successful. Any error detected makes the
   * transaction end without success.
   */
  private final Validators validators;

  /**
   * Constructor.
   *
   * @param model
   *          The information source for job execution.
   * @param validators
   *          Validate job generated changes against the model..
   */
  public Transaction(final Model model, final Validators validators) {
    this.model = model;
    this.validators = validators;
    clone = new Model(model.getName() + "-" + getClass().getSimpleName(), Enforcement.STRICT,
        model);
  }

  /**
   * Execute {@link Job} in transaction.<br>
   *
   * <p>
   * Any jobs created during job execution are executed in the same transaction in breadth first
   * order against the same clone of the model. All changes emanating as a result of each job
   * execution are validated. Any validation failing makes job execution stop. Only after all jobs
   * are executed successfully the {@link Change}s are applied to the original model.
   * </p>
   * 
   * @return The {@link Response}.
   */
  public Response execute(final Job job) {
    LOG.info("> execute {}", job);
    // maintain job list for breadth first job execution
    final LinkedList<Job> jobs = new LinkedList<>();
    jobs.add(job);
    final Response response = executeBreadthFirst(jobs);
    if (response.isSuccessful()) {
      ModelUtils.applyChanges(model, changes);
    }
    LOG.info("< execute {}", response);
    return response;
  }

  /**
   * Breadth first {@link Job} execution.<br>
   * Any jobs created during job execution are also executed in breadth first order. Any changes
   * emanating from each job execution are validated. If invalid job execution is ended and marked
   * as not successful.
   * 
   * @return The {@link Response}.
   */
  private Response executeBreadthFirst(final LinkedList<Job> jobs) {
    LOG.info("> executeBreadthFirst {}", jobs);

    while (!jobs.isEmpty()) {
      final Job job = jobs.removeFirst();

      LOG.info("  executeBreadthFirst {}", job);
      job.execute(clone);

      final List<Message> jobMessages = job.getMessages();
      responseBuilder.addMessages(jobMessages);

      final List<Change> jobChanges = job.getChanges();
      responseBuilder.addChanges(jobChanges);

      List<Message> validatorMessages = validators.validate(clone, jobChanges);
      responseBuilder.addMessages(validatorMessages);
      if (!isValid(validatorMessages)) {
        LOG.info("< executeBreadthFirst validation failed {}", validatorMessages);
        return responseBuilder.build(false);
      }

      changes.addAll(jobChanges);
      ModelUtils.applyChanges(clone, jobChanges);

      final List<Job> chainedJobs = job.getJobs();
      jobs.addAll(chainedJobs);
    }
    // job execution chain successful
    LOG.info("< executeBreadthFirst ok");
    return responseBuilder.build(true);
  }

  private boolean isValid(List<Message> messages) {
    for (Message message : messages) {
      if (message.getSeverity().isErrorOrWorse()) {
        return false;
      }
    }
    return true;
  }
}
