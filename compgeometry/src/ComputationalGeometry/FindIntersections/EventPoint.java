package ComputationalGeometry.FindIntersections;

import ComputationalGeometry.Primitives.Point;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/1/13
 * Time: 3:16 PM
 */
public class EventPoint implements Comparable {
    public Point point;
    public boolean isIntersection;

    public EventPoint(Point point, boolean isIntersection) {
        this.point = point;
        this.isIntersection = isIntersection;
    }

    @Override
    public int compareTo(Object o) {
        Point thisPoint = this.point;
        Point otherPoint = ((EventPoint) o).point;
        if (thisPoint.getY() < otherPoint.getY()) {
            return -1;
        } else if (thisPoint.getY() == otherPoint.getY()) {
            if (thisPoint.getX() < otherPoint.getX()) {
                return -1;
            } else if (thisPoint.getX() == otherPoint.getX()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
