package nl.jvdploeg.flat.socket;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import nl.jvdploeg.rx.DefaultPublisher;

/** Convert from web frame text and to application objects. */
public class RxJsonDecoder implements Observer<String>, Publisher<Object> {

    private final DefaultPublisher<Object> publisher = new DefaultPublisher<>();
    private Disposable subscription;

    public RxJsonDecoder() {
    }

    @Override
    public void subscribe(final Subscriber<? super Object> subscriber) {
        publisher.subscribe(subscriber);
    }

    @Override
    public void onSubscribe(final Disposable subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onNext(final String text) {
        try {
            final Object object = JsonDecoder.decode(text);
            publisher.publishNext(object);
        } catch (final Exception e) {
            publisher.publishError(e);
            disposeSubscription();
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
