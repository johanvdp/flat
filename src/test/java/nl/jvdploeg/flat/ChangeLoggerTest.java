package nl.jvdploeg.flat;

import mockit.Capturing;
import mockit.Expectations;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class ChangeLoggerTest {

  private static final Change ADD = DefaultChange.add(new Path(new String[] { "A", "B" }));
  private static final Change REMOVE = DefaultChange.remove(new Path(new String[] { "A", "B" }));
  private static final Change SET = DefaultChange.set(new Path(new String[] { "A", "B" }), "new");
  private static final Change SET_CHECK = DefaultChange.set(new Path(new String[] { "A", "B" }),
      "old", "new");

  @Capturing
  private Logger log;
  private ChangeLogger changeLogger;

  @Before
  public void before() {
    changeLogger = new ChangeLogger("source");
    new Expectations() {
      {
        log.isDebugEnabled();
        result = true;
      }
    };
  }

  @Test
  public void testOnNextAdd() throws Exception {
    changeLogger.onNext(ADD);
    new Verifications() {
      {
        log.debug("{} onNext add {}", "source", "[A, B]");
      }
    };
  }

  @Test
  public void testOnNextRemove() throws Exception {
    changeLogger.onNext(REMOVE);
    new Verifications() {
      {
        log.debug("{} onNext remove {}", "source", "[A, B]");
      }
    };
  }

  @Test
  public void testOnNextSet() throws Exception {
    changeLogger.onNext(SET);
    new Verifications() {
      {
        log.debug("{} onNext set {} {}", "source", "[A, B]", "new");
      }
    };
  }

  @Test
  public void testOnNextSetCheck() throws Exception {
    changeLogger.onNext(SET_CHECK);
    new Verifications() {
      {
        log.debug("{} onNext set {} {} {}", "source", "[A, B]", "old", "new");
      }
    };
  }
}
