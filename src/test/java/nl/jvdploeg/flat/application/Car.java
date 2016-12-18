package nl.jvdploeg.flat.application;

import nl.jvdploeg.flat.Path;

public class Car {

    public static final Path CARS = new Path(new String[] { Car.NODE_CARS });

    public static final String NODE_CARS = "cars";
    public static final String NODE_LANE = "lane";

    public static Path car(final String name) {
        return new Path(new String[] { Car.NODE_CARS, name });
    }

    public static Path carLane(final String name) {
        return new Path(new String[] { Car.NODE_CARS, name, NODE_LANE });
    }
}
