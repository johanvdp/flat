package nl.jvdploeg.flat.rule;

import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Model;

public interface Rule {

    /**
     * Evaluate the proposed changes to the model and report any changes in response.
     *
     * @param model
     *            The model as it was before the change.
     * @param changes
     *            The (proposed) changes to the model.
     * @return The changes that would occur to (another) model.
     */
    List<Change> evaluate(final Model model, final List<Change> changes);

    /**
     * Check if at least one change matches this {@link Rule}.
     *
     * @return True if at least one change matches this {@link Rule}.
     */
    boolean isApplicable(List<Change> changes);
}
