package nl.jvdploeg.flat.job;

import nl.jvdploeg.flat.DefaultChange;
import nl.jvdploeg.flat.NodeUtils;
import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.application.Car;

public class MoveLeftJob extends DefaultJob {

    private final Path pathToCar;
    private final Path pathToCarLane;

    public MoveLeftJob(final Path pathToCar) {
        this.pathToCar = pathToCar;
        pathToCarLane = pathToCar.createChildPath(Car.NODE_LANE);
    }

    @Override
    public String toString() {
        return "MoveLeftJob[" + pathToCar + "]";
    }

    @Override
    protected void executeImpl() {
        final int lane = Integer.parseInt(NodeUtils.getNode(getModel().getRoot(), pathToCarLane).getValue());
        addChange(DefaultChange.set(pathToCarLane, Integer.toString(lane), Integer.toString(lane + 1)));
    }
}
