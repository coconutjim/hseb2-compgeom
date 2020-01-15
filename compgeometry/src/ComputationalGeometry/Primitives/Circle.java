package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 5/19/13
 * Time: 3:46 PM
 */
public class Circle {
    public boolean finished;
    public Point center;
    public double rightmostX;

    public Circle() {}

    public Circle(boolean finished, Point center, double rightmostX) {
        this.finished = finished;
        this.center = center;
        this.rightmostX = rightmostX;
    }
}
