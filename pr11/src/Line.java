
public class Line {

    /** Parameters */
    private double a, b, c;

    public Line(Point2D p1, Point2D p2) {
        this.a = p1.getY() - p2.getY();
        this.b = p2.getX() - p1.getX();
        this.c = p1.getX() * p2.getY() - p2.getX() * p1.getY();
    }

    public double getXbyY(double y) {
        return (-c - b * y) / a;
    }
    
    public double getYbyX(double x) {
        return (-c - a * x) / b;
    }
    
    public boolean isAscending() {
        return (b == 0 || (-a / b) >= 0);
    }
    
    public boolean isVertical() {
        return (b == 0 && a != 0);
    }
    
    public boolean isHorizontal() {
        return (a == 0 && b != 0);
    }

    public double getAngle() {
        if (isVertical()) return Math.PI / 2;
        
        double angle = Math.atan(-a / b);
        if (angle < 0) angle += Math.PI;
        
        return angle;
    }

    public static Point2D getIntersection(Line l1, Line l2) {
        if (l1.b != 0) {
            double f = l2.a - l2.b * l1.a / l1.b;
            if (f == 0) {
                return Point2D.NOT_A_POINT;
            } else {
                double x = (-l2.c + l2.b * l1.c / l1.b) / f;
                double y = (-l1.c - l1.a * x) / l1.b;
                
                return new Point2D(x, y);
            }
        } else {
            if (l1.a == 0) {
                return Point2D.NOT_A_POINT;
            } else {
                double f = l2.b - l2.a * l1.b / l1.a;
                if (f == 0) {
                    return Point2D.NOT_A_POINT;
                } else {
                    double y = (-l2.c + l2.a * l1.c / l1.a) / f;
                    double x = (-l1.c - l1.b * y) / l1.a;
                
                    return new Point2D(x, y);
                }
            }
        }
    }

}
