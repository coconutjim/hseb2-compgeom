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
    public Point minus(Point op) {
        return new Point(op.x - x, op.y - y);
    }
    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}