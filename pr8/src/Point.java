/**
 * Created by Lev on 02.02.14.
 */

/**
 * Моделирует точку на плоскости.
 */
public class Point {
    protected double x;
    protected double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point(Point point) {
        this.x = point.getX();
        this.y = point.getY();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        x = (int)(x * 100) / 100.;
        y = (int)(y * 100) / 100.;
        point.x = (int)(point.x * 100) / 100.;
        point.y = (int)(point.y * 100) / 100.;


        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}