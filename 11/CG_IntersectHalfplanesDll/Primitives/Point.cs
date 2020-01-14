using System;

namespace CG_IntersectHalfplanesDll.Primitives {
    public class Point: IComparable<Point> {

    public Misc.Line line;
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

    public static bool isLeftTurn(Point p0, Point p1, Point p2) {
        return (crossProduct(p0, p1, p2) > 0);
    }

    public static bool isRightTurn(Point p0, Point p1, Point p2) {
        return (crossProduct(p0, p1, p2) < 0);
    }

    public override bool Equals(Object o2) {

        if (! (o2 is Point)) {
            return false;
        }

        Point p2 = (Point) o2;

        return (this.getX() == p2.getX() && this.getY() == p2.getY());
    }

        public int CompareTo(Point other) {
             return ((Double) this.x).CompareTo(other.x);
        }

        public override String ToString() {
        return ("(" + this.getX() + ", " + this.getY() + ")");
    }

    public Point subtract(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }

    public double distance(Point p) {
        return Math.Sqrt(Math.Pow(x - p.x, 2) + Math.Pow(y - p.y, 2));
    }

}

}
