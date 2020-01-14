
import java.util.ArrayList;

public class Polygon implements Cloneable {

    private ArrayList<Point2D> vertices;

    public Polygon() {
        vertices = new ArrayList<Point2D>();
    }
    
    public void add(Point2D p) {
        if (p != null) {
            vertices.add(p);
        }
    }

    public Point2D get(int i) {
        if (i >= 0 && i < size()) {
            return vertices.get(i);
        } else {
            return null;
        }
    }

    public int size() {
        return vertices.size();
    }

    public Object clone() {
        Polygon copy = new Polygon();
        copy.vertices = (ArrayList<Point2D>)this.vertices.clone();
        return copy;
    }

}