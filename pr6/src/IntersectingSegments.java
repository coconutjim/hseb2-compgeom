import java.util.ArrayList;
/**
 * Created by Lev on 25.02.14.
 */
public class IntersectingSegments {

    private ArrayList<Segment> segments;
    private Point point;

    public IntersectingSegments(ArrayList<Segment> segments, Point point) {
        this.segments = segments;
        this.point = point;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public  Point getPoint() {
        return point;
    }
}
