package nl.jvdploeg.flat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ModelChanger implements Observer<Change> {

    private static final Logger LOG = LoggerFactory.getLogger(ModelChanger.class);

    private Disposable disposable;
    private final Model model;

    public ModelChanger(final Model model) {
        this.model = model;
    }

    @Override
    public void onComplete() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onComplete", model.getName());
        }
        dispose();
    }

    @Override
    public void onError(final Throwable t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s onError %s", model.getName(), t.getMessage()), t);
        }
        dispose();
    }

    @Override
    public void onNext(final Change change) {
        ModelUtils.applyChange(model, change);
    }

    @Override
    public void onSubscribe(final Disposable disposable) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} onSubscribe", model.getName());
        }
        this.disposable = disposable;
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
