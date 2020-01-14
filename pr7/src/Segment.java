import java.awt.Color;

/**
 * Created by Lev on 22.02.14.
 */
public class Segment {
    private Point up;
    private Point down;
    private Color color;
    public Segment() {

    }
    public Segment(Point up, Point down) {

        /** Если горизонтальный */
        if (down.getY() == up.getY()) {
            this.up = up.getX() < down.getX()? up : down;
            this.down = up.getX() < down.getX()? down : up;
            return;
        }

        this.up = up.getY() < down.getY()? down : up;
        this.down = up.getY() < down.getY()? up : down;
    }
    public Point getUp() {
        return up;
    }
    public Point getDown() {
        return down;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Segment)) return false;

        Segment segment = (Segment) o;

        return down.equals(segment.down) && up.equals(segment.up) ||
                up.equals(segment.down) && down.equals(segment.up) ||
                down.equals(segment.up) && up.equals(segment.down);
    }

    @Override
    public int hashCode() {
        int result = up.hashCode();
        result = 31 * result + down.hashCode();
        return result;
    }

    public double length() {
        return Math.sqrt( (up.getX() - down.getX()) * (up.getX() - down.getX()) +
                (up.getY() - down.getY()) * (up.getY() - down.getY()));
    }

    public boolean containsPoint(Point point) {
        /** y = ax + b */
        double a = ( down.getY() - up.getY() ) / ( down.getX() - up.getX() );
        double b = up.getY() - a * up.getX();

        return a * point.getX() + b == point.getY();
    }

    public double calculateX(double y) throws IllegalStateException{

        /** Если вертикальный */
        if (down.getX() == up.getX()) {
            return down.getX();
        }
        /** Если горизонтальный */
        if (down.getY() == up.getY()) {
            throw new IllegalStateException();
        }

        /** y = ax + b */
        double a = ( down.getY() - up.getY() ) / ( down.getX() - up.getX() );        double b = up.getY() - a * up.getX();
        return  (y - b) / a;
    }

    public void setUp(Point up) {
        this.up = up;
    }
}
