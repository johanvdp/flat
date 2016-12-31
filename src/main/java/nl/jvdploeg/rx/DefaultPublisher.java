package nl.jvdploeg.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultPublisher<T> implements Publisher<T> {

  private final List<DefaultSubscription<T>> subscriptions = new ArrayList<>();

  public DefaultPublisher() {
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

  public void publishError(final Throwable throwable) {
    final Iterator<DefaultSubscription<T>> iterator = subscriptions.iterator();
    while (iterator.hasNext()) {
      final DefaultSubscription<T> subscription = iterator.next();
      try {
        subscription.fireError(throwable);
      } catch (final Exception ignored) {
        // ignore
      }
      iterator.remove();
    }
  }

  public void publishNext(final T object) {
    final Iterator<DefaultSubscription<T>> iterator = subscriptions.iterator();
    while (iterator.hasNext()) {
      final DefaultSubscription<T> subscription = iterator.next();
      try {
        subscription.fireNext(object);
      } catch (final Exception ex) {
        // end misbehaving client
        try {
          subscription.fireError(ex);
        } catch (final Exception ignored) {
          // ignore

        }
        iterator.remove();
      }
    }
  }

  @Override
  public void subscribe(final Subscriber<? super T> subscriber) {
    final DefaultSubscription<T> subscription = new DefaultSubscription<>(subscriber);
    subscriptions.add(subscription);
    subscription.fireSubscribe();
  }
}
