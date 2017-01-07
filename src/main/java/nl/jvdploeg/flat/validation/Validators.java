package nl.jvdploeg.flat.validation;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of {@link Validator}s.
 */
public class Validators {

  private List<Validator> validators;

  public Validators() {
  }

  public void setValidators(final List<Validator> validators) {
    this.validators = validators;
  }

  /**
   * Validate proposed changes to the model and report any objections.
   *
   * @param model
   *          The model as it was before the change.
   * @param changes
   *          The (proposed) changes to the model.
   * @return The objections.
   */
  public List<Message> validate(final Model model, final List<Change> changes) {
    final List<Message> messages = new ArrayList<>();
    for (final Validator validator : validators) {
      List<Message> objections = validator.validate(model, changes);
      messages.addAll(objections);
    }
    return messages;
  }
}
