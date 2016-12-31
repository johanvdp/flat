package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.TestApplication;

public class AddCarJob extends DefaultJob {

  private final String carName;

  public AddCarJob(final String carName) {
    this.carName = carName;
  }

  @Override
  public String toString() {
    return "AddCarJob[" + carName + "]";
  }

  @Override
  protected void executeImpl() {
    final Path pathToCar = TestApplication.CARS.createChildPath(carName);
    final Path pathToCarLane = pathToCar.createChildPath(TestApplication.NODE_LANE);
    addChange(DefaultChange.add(pathToCar));
    addChange(DefaultChange.add(pathToCarLane));
    addChange(DefaultChange.set(pathToCarLane, "1"));
  }
}
