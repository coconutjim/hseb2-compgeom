import java.awt.*;

/**
 * Created by Lev on 22.02.14.
 */
public class Segment {
    private Point left;
    private Point right;
    private Color color;
    public Segment(Point left, Point right) {
        this.left = left.getX() > right.getX()? right : left;
        this.right = left.getX() > right.getX()? left : right;
    }
    public Point getLeft() {
        return left;
    }
    public Point getRight() {
        return right;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
    public boolean equal(Segment op) {
        return left.equal(op.left) && right.equal(op.right);
    }
}
