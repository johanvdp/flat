package nl.jvdploeg.rx;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DefaultSubscriber<T> implements Subscriber<T> {

  private final DefaultPublisher<T> publisher;

  public DefaultSubscriber(final DefaultPublisher<T> publisher) {
    this.publisher = publisher;
  }

  @Override
  public void onComplete() {
    publisher.publishComplete();
  }

  @Override
  public void onError(final Throwable throwable) {
    publisher.publishError(throwable);
  }

  @Override
  public void onNext(final T item) {
    publisher.publishNext(item);
  }

  @Override
  public void onSubscribe(final Subscription subscription) {
    subscription.request(Long.MAX_VALUE);
  }
}