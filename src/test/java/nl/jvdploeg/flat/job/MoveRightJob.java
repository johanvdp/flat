package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.NodeUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Car;

public class MoveRightJob extends DefaultJob {

    private final Path pathToCar;
    private final Path pathToCarLane;

    public MoveRightJob(final Path pathToCar) {
        this.pathToCar = pathToCar;
        pathToCarLane = pathToCar.createChildPath(Car.NODE_LANE);
    }

    @Override
    public String toString() {
        return "MoveRightJob[" + pathToCar + "]";
    }

    @Override
    protected void executeImpl() {
        final int lane = Integer.parseInt(NodeUtils.getNode(getModel().getRoot(), pathToCarLane).getValue());
        if (lane > 1) {
            addChange(DefaultChange.set(pathToCarLane, Integer.toString(lane), Integer.toString(lane - 1)));
        }
    }
}
