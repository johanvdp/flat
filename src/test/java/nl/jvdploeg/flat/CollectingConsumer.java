package nl.jvdploeg.flat;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class CollectingConsumer<T> implements Consumer<T> {

    private final List<T> collection = new ArrayList<>();

    public CollectingConsumer() {
    }

    @Override
    public void accept(final T t) throws Exception {
        collection.add(t);
    }

    public List<T> getCollection() {
        return collection;
    }
}