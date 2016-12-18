package nl.jvdploeg.rx;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DefaultSubscriber<T> implements Subscriber<T> {

    private final DefaultPublisher<T> publisher;

    public DefaultSubscriber(final DefaultPublisher<T> publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(final T t) {
        publisher.publishNext(t);
    }

    @Override
    public void onError(final Throwable t) {
        publisher.publishError(t);
    }

    @Override
    public void onComplete() {
        publisher.publishComplete();
    }
}