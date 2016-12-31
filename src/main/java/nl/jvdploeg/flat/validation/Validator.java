package nl.jvdploeg.flat.validation;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.message.Message;

import java.util.List;

public interface Validator {

  /**
   * Get resulting {@link Message}s.
   */
  List<Message> getMessages();

  /**
   * Evaluate the proposed changes to the model and report any objection messages.
   *
   * @param model
   *          The model as it was before the change.
   * @param changes
   *          The (proposed) changes to the model.
   */
  void validate(final Model model, final List<Change> changes);
}
