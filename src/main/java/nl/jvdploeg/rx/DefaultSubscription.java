package nl.jvdploeg.rx;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSubscription<T> implements Subscription {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultSubscription.class);

  private long numberRequested;
  private final Subscriber<? super T> subscriber;

  public DefaultSubscription(final Subscriber<? super T> subscriber) {
    this.subscriber = subscriber;
  }

  @Override
  public void cancel() {
    numberRequested = 0;
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
   * @param throwable
   *          The throwable.
   */
  public void fireError(final Throwable throwable) {
    subscriber.onError(throwable);
  }

  /**
   * Fire next.
   *
   * @param observation
   *          The observation.
   */
  public void fireNext(final T observation) {
    if (numberRequested == Long.MAX_VALUE) {
      subscriber.onNext(observation);
    } else if (numberRequested > 0) {
      numberRequested--;
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
  public void request(final long numberRequested) {
    this.numberRequested = numberRequested;
  }
}