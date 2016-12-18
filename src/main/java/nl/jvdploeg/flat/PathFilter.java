package nl.jvdploeg.flat;

import io.reactivex.functions.Predicate;
import nl.jvdploeg.nfa.TokenMatcher;

public class PathFilter implements Predicate<Change> {

    private final TokenMatcher matcher;

    public PathFilter(final TokenMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean test(final Change change) {
        final Path path = change.getPath();
        return matcher.matches(path.getPath());
    }
}
