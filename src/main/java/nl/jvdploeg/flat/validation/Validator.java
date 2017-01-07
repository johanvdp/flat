package nl.jvdploeg.flat.validation;

import java.util.List;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.message.Message;

/**
 * Validates proposed changes to the model.
 */
public interface Validator {

  /**
   * Validate proposed changes to the model and report any objections.
   *
   * @param model
   *          The model as it was before the change.
   * @param changes
   *          The (proposed) changes to the model.
   * @return The objections.
   */
  List<Message> validate(final Model model, final List<Change> changes);
}
