package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.NodeUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.TestApplication;

public class SwerveJob extends DefaultJob {

  private final Path pathToCar;
  private final Path pathToCarLane;

  public SwerveJob(final Path pathToCar) {
    this.pathToCar = pathToCar;
    pathToCarLane = pathToCar.createChildPath(TestApplication.NODE_LANE);
  }

  @Override
  public String toString() {
    return "SwerveJob[" + pathToCar + "]";
  }

  @Override
  protected void executeImpl() {
    final int lanes = Integer
        .parseInt(NodeUtils.getNode(getModel().getRoot(), TestApplication.ROAD_LANES).getValue());
    final int lane = Integer
        .parseInt(NodeUtils.getNode(getModel().getRoot(), pathToCarLane).getValue());
    if (lane < lanes - 1) {
      // swerve left
      addJob(new MoveLeftJob(pathToCar));
      addJob(new MoveLeftJob(pathToCar));
      addJob(new MoveRightJob(pathToCar));
    } else if (lane > 2) {
      addJob(new MoveRightJob(pathToCar));
      addJob(new MoveRightJob(pathToCar));
      addJob(new MoveLeftJob(pathToCar));
    } else {
      // no room to swerve
      // who to tell? add the concept of job feedback?
    }
  }
}
