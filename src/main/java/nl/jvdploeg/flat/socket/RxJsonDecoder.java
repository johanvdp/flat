package nl.jvdploeg.flat.socket;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import nl.jvdploeg.rx.DefaultPublisher;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/** Convert from web frame text to application objects. */
public class RxJsonDecoder implements Observer<String>, Publisher<Object> {

  private final DefaultPublisher<Object> publisher = new DefaultPublisher<>();
  private Disposable subscription;

  public RxJsonDecoder() {
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
  public void onNext(final String text) {
    try {
      final Object object = JsonDecoder.decode(text);
      publisher.publishNext(object);
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
  public void subscribe(final Subscriber<? super Object> subscriber) {
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
