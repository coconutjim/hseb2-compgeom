package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/8/13
 * Time: 9:21 AM
 */
public class Segment {

    public Segment(Point left, Point right) {

        if(left.getX() > right.getX()) {
            Point temp = right;
            right = left;
            left = temp;
        }

        left.setSegment(this);
        right.setSegment(this);

        this.left = left;
        this.right = right;
    }

    public Point getLeft() {
        return left;
    }

    public Point getRight() {
        return right;
    }

    public Point getBisectionalPoint() {
        return new Point((left.getX() + right.getX()) / 2,
                (left.getY() + right.getY()) / 2);
    }

    public Line getLine() {
        return new Line(left, right);
    }


    @Override
    public boolean equals(Object o2) {

        if (! (o2 instanceof Segment)) {
            return false;
        }

        Segment s2 = (Segment) o2;

        return ((this.getLeft().equals(s2.getLeft()) &&
                this.getRight().equals(s2.getRight())) ||
                (this.getLeft().equals(s2.getRight()) &&
                        this.getRight().equals(s2.getLeft())));
    }

    final private Point left;
    final private Point right;
}
