package nl.jvdploeg.flat;

import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;

public class CollectingConsumer<T> implements Consumer<T> {

  private final List<T> collection = new ArrayList<>();

  public CollectingConsumer() {
  }

  @Override
  public void accept(final T item) throws Exception {
    collection.add(item);
  }

  public List<T> getCollection() {
    return collection;
  }
}