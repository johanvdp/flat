package nl.jvdploeg.flat.rule;

import java.util.List;

import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.ModelUtils;

/** Collection of {@link Rule}s. */
public class Verifiers {

    private final Model status = new Model(Verifiers.class.getSimpleName(), Enforcement.STRICT);
    private List<Rule> verifiers;

    public Verifiers() {
    }

    public Model getStatus() {
        return status;
    }

    public void setVerifiers(final List<Rule> verifiers) {
        this.verifiers = verifiers;
    }

    public void verify(final Model world, final List<Change> changes) {
        for (final Rule verifier : verifiers) {
            if (verifier.isApplicable(changes)) {
                final List<Change> verifierChanges = verifier.evaluate(world, changes);
                ModelUtils.applyChanges(status, verifierChanges);
            }
        }
    }
}
