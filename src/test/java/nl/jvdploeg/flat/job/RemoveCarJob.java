package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.TestApplication;

public class RemoveCarJob extends DefaultJob {

  private final String carName;

  public RemoveCarJob(final String carName) {
    this.carName = carName;
  }

  @Override
  public String toString() {
    return "RemoveCarJob[" + carName + "]";
  }

  @Override
  protected void executeImpl() {
    final Path pathToCar = TestApplication.CARS.createChildPath(carName);
    addChange(DefaultChange.remove(pathToCar));
  }
}
