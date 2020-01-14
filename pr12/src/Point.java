
public class Point implements Comparable<Point> {

    public double x;
    public double y;

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

    @Override
    public int compareTo(Point o) {
        return ((Double) this.x).compareTo(o.x);
    }
}
