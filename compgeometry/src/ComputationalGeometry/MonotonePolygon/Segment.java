package ComputationalGeometry.MonotonePolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/31/13
 * Time: 1:07 AM
 */
public class Segment implements Comparable {
    public Segment next;
    public Segment previous;

    public List<Vertex> vertexes = new ArrayList<Vertex>();

    public Segment(Vertex v1, Vertex v2) {
        vertexes.add(v1);
        vertexes.add(v2);
        v1.segments.add(this);
        v2.segments.add(this);
    }

    public Vertex getFirstVertex() {
        return vertexes.get(0);
    }

    public Vertex getSecondVertex() {
        return vertexes.get(1);
    }

    public Vertex getUpperVertex() {
        Vertex result;
        if (vertexes.get(0).y < vertexes.get(1).y) {
            result = vertexes.get(0);
        } else if (vertexes.get(1).y < vertexes.get(0).y) {
            result = vertexes.get(1);
        } else {
            result = vertexes.get(0).x < vertexes.get(1).x ? vertexes.get(0) : vertexes.get(1);
        }
        return result;
    }

    public Vertex getLowerVertex() {
        Vertex result;
        if (vertexes.get(0).y < vertexes.get(1).y) {
            result = vertexes.get(1);
        } else if (vertexes.get(1).y < vertexes.get(0).y) {
            result = vertexes.get(0);
        } else {
            result = vertexes.get(0).x < vertexes.get(1).x ? vertexes.get(1) : vertexes.get(0);
        }
        return result;
    }


    @Override
    public int compareTo(Object o) {
        Segment otherSegment = (Segment)o;
        if (this == otherSegment) {
            return 0;
        } else {
            int result = this.getUpperVertex().compareTo(otherSegment.getUpperVertex());
            return result != 0 ? result : -1;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", getFirstVertex().toString(), getSecondVertex().toString());
    }
}
