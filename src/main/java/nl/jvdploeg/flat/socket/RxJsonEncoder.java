package nl.jvdploeg.flat.socket;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import nl.jvdploeg.rx.DefaultPublisher;
import org.json.simple.JSONObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/** Convert from application objects to web frame text. */
public class RxJsonEncoder implements Observer<Object>, Publisher<String> {

  private final DefaultPublisher<String> publisher = new DefaultPublisher<>();
  private Disposable subscription;

  public RxJsonEncoder() {
  }

  @Override
  public void onComplete() {
    publisher.publishComplete();
    disposeSubscription();
  }

  @Override
  public void onError(final Throwable throwable) {
    publisher.publishError(throwable);
    disposeSubscription();
  }

  @Override
  public void onNext(final Object object) {
    try {
      final JSONObject jsonObject = JsonEncoder.encode(object);
      final String json = JSONObject.toJSONString(jsonObject);
      publisher.publishNext(json);
    } catch (final Exception ex) {
      publisher.publishError(ex);
      disposeSubscription();
    }
  }

  @Override
  public void onSubscribe(final Disposable subscription) {
    this.subscription = subscription;
  }

  @Override
  public void subscribe(final Subscriber<? super String> subscriber) {
    publisher.subscribe(subscriber);
  }

  private void disposeSubscription() {
    if (subscription != null) {
      if (!subscription.isDisposed()) {
        subscription.dispose();
      }
      subscription = null;
    }
  }
}
