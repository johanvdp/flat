package nl.jvdploeg.flat;

import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class CollectingSubscriber<T> implements Subscriber<T> {

  private final List<T> collection = new ArrayList<>();
  private Throwable error;
  private boolean complete;
  private Subscription subscription;

  public CollectingSubscriber() {
  }

  public List<T> getCollection() {
    return collection;
  }

  public Throwable getError() {
    return error;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public boolean isComplete() {
    return complete;
  }

  @Override
  public void onComplete() {
    if (complete) {
      throw new IllegalStateException("should not call onComplete more than once");
    }
    if (this.error != null) {
      throw new IllegalStateException("should not call onComplete after onError");
    }
    complete = true;
  }

  @Override
  public void onError(final Throwable error) {
    if (complete) {
      throw new IllegalStateException("should not call onError after onComplete");
    }
    if (this.error != null) {
      throw new IllegalStateException("should not call onError more than once");
    }
    this.error = error;
  }

  @Override
  public void onNext(final T item) {
    if (complete) {
      throw new IllegalStateException("should not call onNext after onComplete");
    }
    if (this.error != null) {
      throw new IllegalStateException("should not call onNext after onError");
    }
    collection.add(item);
  }

  @Override
  public void onSubscribe(final Subscription subscription) {
    this.subscription = subscription;
    subscription.request(Long.MAX_VALUE);
  }
}