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

  public List<Message> validate(final Model world, final List<Change> changes) {
    final List<Message> messages = new ArrayList<>();
    for (final Validator validator : validators) {
      validator.validate(world, changes);
      messages.addAll(validator.getMessages());
    }
    return messages;
  }
}
