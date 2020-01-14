/**
 * Created by Lev on 02.02.14.
 */

/**
 * Моделирует точку на плоскости.
 */
public class Point {
    private double x;
    private double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public static double distance(Point o1, Point o2) {
        return Math.sqrt( (o2.x - o1.x) * (o2.x - o1.x) + (o2.y - o1.y) * (o2.y - o1.y) );
    }
    public boolean equal(Point op) {
        return x == op.x && y == op.y;
    }
    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}