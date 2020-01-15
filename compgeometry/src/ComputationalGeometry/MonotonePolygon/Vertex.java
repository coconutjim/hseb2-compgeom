package ComputationalGeometry.MonotonePolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/31/13
 * Time: 1:08 AM
 */
public class Vertex implements Comparable {
    public float x;
    public float y;

    public List<Segment> segments = new ArrayList<Segment>();

    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Segment secondVertexOf() {
        return segments.get(0).getSecondVertex() == this ? segments.get(0) : segments.get(1);
    }

    public Segment firstVertexOf() {
        return segments.get(0).getFirstVertex() == this ? segments.get(0) : segments.get(1);
    }

    public Segment upperVertexOf() {
        return segments.get(0).getUpperVertex() == this ? segments.get(0) : segments.get(1);
    }

    public Segment lowerVertexOf() {
        return segments.get(0).getLowerVertex() == this ? segments.get(0) : segments.get(1);
    }

    public int getIntX() {
        return (int)x;
    }

    public int getIntY() {
        return (int)y;
    }

    @Override
    public int compareTo(Object o) {
        Vertex thisVertex = this;
        Vertex otherVertex = (Vertex) o;
        if (thisVertex.y < otherVertex.y) {
            return -1;
        } else if (thisVertex.y == otherVertex.y) {
            if (thisVertex.x < otherVertex.x) {
                return -1;
            } else if (thisVertex.x == otherVertex.x) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static double direction(Vertex p1, Vertex p2, Vertex p3) {
        return crossProduct(p3.subtract(p1), p2.subtract(p1));
    }

    public static double crossProduct(Vertex p1, Vertex p2) {
        return p1.x * p2.y - p1.y * p2.x;
    }

    public Vertex subtract(Vertex p) {
        return new Vertex(x - p.x, y - p.y);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }
}
