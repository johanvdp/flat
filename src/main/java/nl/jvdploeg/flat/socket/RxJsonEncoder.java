package nl.jvdploeg.flat.socket;

import org.json.simple.JSONObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import nl.jvdploeg.rx.DefaultPublisher;

/** Convert from application objects to web frame text. */
public class RxJsonEncoder implements Observer<Object>, Publisher<String> {

    private final DefaultPublisher<String> publisher = new DefaultPublisher<>();
    private Disposable subscription;

    public RxJsonEncoder() {
    }

    @Override
    public void subscribe(final Subscriber<? super String> subscriber) {
        publisher.subscribe(subscriber);
    }

    @Override
    public void onSubscribe(final Disposable subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onNext(final Object object) {
        try {
            final JSONObject jsonObject = JsonEncoder.encode(object);
            final String json = JSONObject.toJSONString(jsonObject);
            publisher.publishNext(json);
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
