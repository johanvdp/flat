package nl.jvdploeg.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TypeFilter<I, O extends I> implements Observer<I>, Publisher<O> {

    private final DefaultPublisher<O> publisher = new DefaultPublisher<>();
    private final Class<O> type;
    private Disposable subscription;

    public TypeFilter(final Class<O> type) {
        this.type = type;
    }

    @Override
    public void subscribe(final Subscriber<? super O> subscriber) {
        publisher.subscribe(subscriber);
    }

    @Override
    public void onSubscribe(final Disposable subscription) {
        this.subscription = subscription;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onNext(final I object) {
        if (type.isInstance(object)) {
            try {
                publisher.publishNext((O) object);
            } catch (final Exception e) {
                publisher.publishError(e);
                disposeSubscription();
            }
        }
    }

    private void disposeSubscription() {
        if (subscription != null) {
            if (!subscription.isDisposed()) {
                subscription.dispose();
            }
            subscription = null;
        }
    }

    @Override
    public void onError(final Throwable t) {
        publisher.publishError(t);
        disposeSubscription();
    }

    @Override
    public void onComplete() {
        publisher.publishComplete();
        disposeSubscription();
    }
}
