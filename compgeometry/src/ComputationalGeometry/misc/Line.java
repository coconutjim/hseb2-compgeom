package ComputationalGeometry.misc;

import ComputationalGeometry.Primitives.Point;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 2/28/13
 * Time: 2:15 PM
 */
public
class Line {
    public Point left;
    public Point right;
    public boolean reversed;

    public Line(Point l, Point r) {
        left = l;
        right = r;
    }
}
