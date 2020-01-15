package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/21/13
 * Time: 7:36 PM
 */
public class Halfplane {
    public Line line;
    public boolean rightSide;

    public Halfplane(Line line, boolean rightSide) {
        this.line = line;
        this.rightSide = rightSide;
    }

    public boolean includes(Point p) {
        if (rightSide) {
            if ((line.a == 0 && line.b != 0)) { // == lower boundary
                return (p.getY() > -line.c / line.b);
            } else {
                return (p.getX() < (-line.c - line.b * p.getY()) / line.a);
            }
        } else {
            if ((line.a == 0 && line.b != 0)) { // == upper boundary
                return (p.getY() < -line.c / line.b);
            } else {
                return (p.getX() > (-line.c - line.b * p.getY()) / line.a);
            }
        }
    }
}
