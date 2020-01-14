
public class Line {

    /** Line parameters*/
    public double a;
    public double b;
    public double c;
    public double angle;
    public double x1;
    public double x2;
    public double y1;
    public double y2;


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

    private double getAngle() {
        if ((b == 0 && a != 0)) return Math.PI/2;

        double atan = Math.atan(-a/b);
        if (atan < 0) atan += Math.PI;

        return atan;
    }

    @Override
    public String toString() {
        return a + "*x + " + b + "*y + " + c + " = 0.0";
    }
}
