package ComputationalGeometry.misc;


/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 1/14/13
 * Time: 12:49 PM
 */
public class Point {

    private double x;
    private double y;
    public Line line;
    public int type = 0;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point subtract(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }

    public double getX() {
        return x;
    }

    public int getIntX() {
        return (int)x;
    }

    public double getY() {
        return y;
    }

    public int getIntY() {
        return (int)y;
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    public boolean equals(Point p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    public String print() {
        return "(" + this.getX() + ", " + this.getY() + ")";
    }
}
