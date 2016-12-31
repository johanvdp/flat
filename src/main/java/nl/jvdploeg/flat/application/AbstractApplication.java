package nl.jvdploeg.flat.application;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.job.Job;
import nl.jvdploeg.flat.job.Transaction;
import nl.jvdploeg.flat.validation.Validators;

public abstract class AbstractApplication implements Application {

  protected Model model;
  protected Validators validators;

  protected AbstractApplication() {
  }

  public Response execute(final Job job) {
    final Transaction transaction = new Transaction(model, validators);
    final Response response = transaction.execute(job);
    return response;
  }

  @Override
  public Model getModel() {
    return model;
  }

  public void initialize() {
    model = createModel();
    validators = createValidators();
  }

  protected abstract Model createModel();

  protected abstract Validators createValidators();
}
