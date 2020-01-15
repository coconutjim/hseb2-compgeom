package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/8/13
 * Time: 9:19 AM
 */
public class Point implements Comparable<Point> {

    public ComputationalGeometry.misc.Line line;
    public int type = 0;
    public double x;
    public double y;
    private Segment segment;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private static double crossProduct(Point p0, Point p1, Point p2) {
        return (p1.getX() - p0.getX()) * (p2.getY() - p0.getY()) -
                (p2.getX() - p0.getX()) * (p1.getY() - p0.getY());
    }

    public static boolean isLeftTurn(Point p0, Point p1, Point p2) {
        return (crossProduct(p0, p1, p2) > 0);
    }

    public static boolean isRightTurn(Point p0, Point p1, Point p2) {
        return (crossProduct(p0, p1, p2) < 0);
    }

    @Override
    public boolean equals(Object o2) {

        if (! (o2 instanceof Point)) {
            return false;
        }

        Point p2 = (Point) o2;

        return (this.getX() == p2.getX() && this.getY() == p2.getY());
    }

    @Override
    public String toString() {
        return ("(" + this.getX() + ", " + this.getY() + ")");
    }

    public Point subtract(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    @Override
    public int compareTo(Point o) {
        return ((Double) this.x).compareTo(o.x);
    }
}
