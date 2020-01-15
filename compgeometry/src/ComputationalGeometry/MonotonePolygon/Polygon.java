package ComputationalGeometry.MonotonePolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/31/13
 * Time: 1:13 AM
 */
public class Polygon {
    public List<Segment> segments = new ArrayList<Segment>();
    private boolean isFinished = false;

    public Polygon(Segment segment) {
        segments.add(segment);
    }

    public Segment addSegment(Vertex vertex) throws Exception {
        if (isFinished) {
            throw new Exception("Polygon is finished");
        }
        Segment lastSegment = getLast();
        Segment newSegment = new Segment(lastSegment.vertexes.get(1), vertex);
        newSegment.previous = lastSegment;
        lastSegment.next = newSegment;
        segments.add(newSegment);
        return newSegment;
    }

    public Segment finishPolygon() throws Exception {
        Segment first = getFirst();
        Segment last = getLast();
        Segment newSegment = new Segment(last.vertexes.get(1), first.vertexes.get(0));
        segments.add(newSegment);
        return newSegment;
    }

    private Segment getFirst() throws Exception {
        if (segments.size() < 2) {
            throw new Exception("Polygon must to have more than 2 segments");
        }
        return segments.get(0);
    }

    private Segment getLast() throws Exception {
        if (segments.size() < 1) {
            throw new Exception("Polygon doesn't have any segments");
        }
        return segments.get(segments.size() - 1);
    }
}
