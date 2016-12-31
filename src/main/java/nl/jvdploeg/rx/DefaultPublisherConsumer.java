package nl.jvdploeg.rx;

import io.reactivex.functions.Consumer;

public class DefaultPublisherConsumer<T> implements Consumer<T> {

  private final DefaultSubscriber<T> subscriber;

  public DefaultPublisherConsumer(final DefaultPublisher<T> publisher) {
    subscriber = new DefaultSubscriber<>(publisher);
  }

  @Override
  public void accept(final T item) throws Exception {
    subscriber.onNext(item);
  }
}
