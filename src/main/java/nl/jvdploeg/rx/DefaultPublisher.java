package nl.jvdploeg.rx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class DefaultPublisher<T> implements Publisher<T> {

    private final List<DefaultSubscription<T>> subscriptions = new ArrayList<>();

    public DefaultPublisher() {
    }

    @Override
    public void subscribe(final Subscriber<? super T> subscriber) {
        final DefaultSubscription<T> subscription = new DefaultSubscription<>(subscriber);
        subscriptions.add(subscription);
        subscription.fireSubscribe();
    }

    public void publishNext(final T object) {
        final Iterator<DefaultSubscription<T>> iterator = subscriptions.iterator();
        while (iterator.hasNext()) {
            final DefaultSubscription<T> subscription = iterator.next();
            try {
                subscription.fireNext(object);
            } catch (final Exception e) {
                // end misbehaving client
                try {
                    subscription.fireError(e);
                } catch (final Exception ignored) {
                    // ignore

                }
                iterator.remove();
            }
        }
    }

    public void publishError(final Throwable t) {
        final Iterator<DefaultSubscription<T>> iterator = subscriptions.iterator();
        while (iterator.hasNext()) {
            final DefaultSubscription<T> subscription = iterator.next();
            try {
                subscription.fireError(t);
            } catch (final Exception ignored) {
                // ignore
            }
            iterator.remove();
        }
    }

    public void publishComplete() {
        final Iterator<DefaultSubscription<T>> iterator = subscriptions.iterator();
        while (iterator.hasNext()) {
            final DefaultSubscription<T> subscription = iterator.next();
            try {
                subscription.fireComplete();
            } catch (final Exception ignored) {
                // ignore
            }
            iterator.remove();
        }
    }
}
