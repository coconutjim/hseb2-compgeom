package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/8/13
 * Time: 9:22 AM
 */
public class Line {
    public double a;
    public double b;
    public double c;
    public double angle;
    public double x1;
    public double x2;
    public double y1;
    public double y2;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Line(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.angle = getAngle();
    }

    public Line(Point p1, Point p2) {
        x1 = p1.getX();
        x2 = p2.getX();
        y1 = p1.getY();
        y2 = p2.getY();
        this.a = p1.getY() - p2.getY();
        this.b = p2.getX() - p1.getX();
        this.c = p1.getX() * p2.getY() - p2.getX() * p1.getY();
        this.angle = getAngle();
    }

    public boolean pointOnLine(Point p) {
        return (a * p.getX() + b * p.getY() + c == 0);
    }

    private double getAngle() {
        if ((b == 0 && a != 0)) return Math.PI/2;

        double atan = Math.atan(-a/b);
        if (atan < 0) atan += Math.PI;

        return atan;
    }

    public Line getPerpendicularLine(Point p) {
        return new Line(this.b, -this.a, -this.b*p.getX() + this.a*p.getY());
    }

    public static Point intersection(Line l1, Line l2) {
        if (l1.b != 0) {
            double f = l2.a - l2.b*l1.a/l1.b;
            if (f == 0) {
                return null;
            } else {
                double x = (-l2.c + l2.b*l1.c/l1.b) / f;
                double y = (-l1.c - l1.a*x) / l1.b;

                return new Point(x, y);
            }
        } else {
            if (l1.a == 0) {
                return null;
            } else {
                double f = l2.b - l2.a*l1.b/l1.a;
                if (f == 0) {
                    return null;
                } else {
                    double y = (-l2.c + l2.a*l1.c/l1.a) / f;
                    double x = (-l1.c - l1.b*y) / l1.a;

                    return new Point(x, y);
                }
            }
        }
    }

    public float getLength()
    {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    @Override
    public String toString() {
        return a + "*x + " + b + "*y + " + c + " = 0.0";
    }
}
