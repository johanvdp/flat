package nl.jvdploeg.flat.rule;

import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;

/** Collection of {@link Rule}s. */
public class Validators {

    private List<Rule> validators;

    public Validators() {
    }

    public void setValidators(final List<Rule> validators) {
        this.validators = validators;
    }

    public Model validate(final Model world, final List<Change> changes) {
        final Model status = new Model(Validators.class.getSimpleName(), Enforcement.STRICT);
        for (final Rule validator : validators) {
            if (validator.isApplicable(changes)) {
                final List<Change> validatorChanges = validator.evaluate(world, changes);
                ModelUtils.applyChanges(status, validatorChanges);
            }
        }
        return status;
    }
}
