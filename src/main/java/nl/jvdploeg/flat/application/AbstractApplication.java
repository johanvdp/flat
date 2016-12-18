package nl.jvdploeg.flat.application;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.job.Job;
import nl.jvdploeg.flat.job.Transaction;
import nl.jvdploeg.flat.rule.Validators;
import nl.jvdploeg.flat.rule.Verifiers;

public abstract class AbstractApplication implements Application {

    private Model model;
    private Validators validators;
    private Verifiers verifiers;

    protected AbstractApplication() {
    }

    public boolean execute(final Job job) {
        final Transaction transaction = new Transaction(model, validators, verifiers);
        final boolean successful = transaction.execute(job);
        return successful;
    }

    @Override
    public Model getModel() {
        return model;
    }

    public Model getVerifiersModel() {
        return verifiers.getStatus();
    }

    public void initialize() {
        model = createModel();
        verifiers = createVerifiers();
        validators = createValidators();
    }

    protected abstract Model createModel();

    protected abstract Validators createValidators();

    protected abstract Verifiers createVerifiers();
}
