package nl.jvdploeg.rx;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSubscription<T> implements Subscription {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSubscription.class);

    private long n;
    private final Subscriber<? super T> subscriber;

    public DefaultSubscription(final Subscriber<? super T> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void cancel() {
        n = 0;
    }

    /**
     * Fire complete.
     */
    public void fireComplete() {
        subscriber.onComplete();
    }

    /**
     * Fire error.
     *
     * @param t
     *            The throwable.
     */
    public void fireError(final Throwable t) {
        subscriber.onError(t);
    }

    /**
     * Fire next.
     *
     * @param observation
     *            The observation.
     */
    public void fireNext(final T observation) {
        if (n == Long.MAX_VALUE) {
            subscriber.onNext(observation);
        } else if (n > 0) {
            n--;
            subscriber.onNext(observation);
        } else {
            LOG.debug("fireNext skipped observation (no request)");
        }
    }

    /**
     * Fire subscribe.
     */
    public void fireSubscribe() {
        subscriber.onSubscribe(this);
    }

    @Override
    public void request(final long n) {
        this.n = n;
    }
}