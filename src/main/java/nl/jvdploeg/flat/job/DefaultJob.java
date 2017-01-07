package nl.jvdploeg.flat.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultJob implements Job {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultJob.class);

  private final List<Message> messages = new ArrayList<>();
  private final List<Change> changes = new ArrayList<>();
  private final List<Job> jobs = new ArrayList<>();
  private Model model;

  protected DefaultJob() {
  }

  @Override
  public final void execute(final Model model) {
    if (this.model != null) {
      throw new IllegalStateException("do not re-use a job");
    }
    this.model = model;
    executeImpl();
  }

  @Override
  public List<Change> getChanges() {
    return changes;
  }

  @Override
  public List<Job> getJobs() {
    return jobs;
  }

  @Override
  public List<Message> getMessages() {
    return Collections.unmodifiableList(messages);
  }

  public Model getModel() {
    return model;
  }

  protected void addChange(final Change change) {
    LOG.info(">< addChange {}", change);
    changes.add(change);
  }

  protected void addJob(final Job job) {
    LOG.info(">< addJob {}", job.getClass().getSimpleName());
    jobs.add(job);
  }

  protected void addMessage(final Message message) {
    messages.add(message);
  }

  protected abstract void executeImpl();
}
