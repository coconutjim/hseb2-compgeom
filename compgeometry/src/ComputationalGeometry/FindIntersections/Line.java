package ComputationalGeometry.FindIntersections;


import ComputationalGeometry.Primitives.Point;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/1/13
 * Time: 3:03 PM
 */
public class Line {
    public Point upper;
    public Point lower;
    public boolean isReversed;

    public Line(Point upper, Point lower) {
        this.upper = upper;
        this.lower = lower;
        this.isReversed = false;
    }
}
