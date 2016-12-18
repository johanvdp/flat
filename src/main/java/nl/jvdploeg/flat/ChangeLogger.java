package nl.jvdploeg.flat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChangeLogger implements Observer<Change> {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeLogger.class);

    private final String source;

    public ChangeLogger(final String source) {
        this.source = source;
    }

    @Override
    public void onComplete() {
        LOG.debug("{} onComplete", source);
    }

    @Override
    public void onError(final Throwable e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s onError", source), e);
        }
    }

    @Override
    public void onNext(final Change change) {

        final ChangeAction action = change.getAction();
        final Path path = change.getPath();
        final String oldValue = change.getOldValue();
        final String value = change.getNewValue();

        final String debugPath = LOG.isDebugEnabled() ? path.toString() : "";
        switch (action) {
        case SET:
            if (oldValue == null) {
                LOG.debug("{} onNext set {} {}", source, debugPath, value);
            } else {
                LOG.debug("{} onNext set {} {} {}", source, debugPath, oldValue, value);
            }
            break;
        case ADD:
            LOG.debug("{} onNext add {}", source, debugPath);
            break;
        case REMOVE:
            LOG.debug("{} onNext remove {}", source, debugPath);
            break;
        default:
            throw new IllegalArgumentException(String.format("%s onNext unknown change action: %s", source, action));
        }
    }

    @Override
    public void onSubscribe(final Disposable disposable) {
        LOG.debug("{} onSubscribe", source);
        // logger never cancels subscription
    }
}
