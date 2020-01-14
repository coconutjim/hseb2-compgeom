/**
 * Created by Lev on 13.02.14.
 */
public class Pair {
    private Point p1;
    private Point p2;
    private double distance;

    public Pair(Point p1, Point p2, double distance) {
        this.p1 = p1;
        this.p2 = p2;
        this.distance = distance;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public double getDistance() {
        return distance;
    }
}
