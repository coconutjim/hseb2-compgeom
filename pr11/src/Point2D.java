
public class Point2D {

    public final static Point2D NOT_A_POINT = new Point2D(Double.NaN, Double.NaN);

    final private double x;
    final private double y;

    public Point2D(double x, double y) {
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

        if (! (o2 instanceof Point2D)) {
            return false;
        }

        Point2D p2 = (Point2D) o2;

        return (this.getX() == p2.getX() && this.getY() == p2.getY());
    }
    
    public boolean isNanDot() {
        return x == Double.NaN || y == Double.NaN;
    }

}
