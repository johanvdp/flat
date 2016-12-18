package nl.jvdploeg.flat.application;

import nl.jvdploeg.flat.Path;

public class Road {

    public static final String NODE_LANES = "lanes";
    public static final String NODE_ROAD = "road";

    public static final Path ROAD = new Path(new String[] { NODE_ROAD });
    public static final Path ROAD_LANES = new Path(new String[] { Road.NODE_ROAD, NODE_LANES });

}
