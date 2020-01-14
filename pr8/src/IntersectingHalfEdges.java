import java.util.ArrayList;
/**
 * Created by Lev on 25.02.14.
 */
public class IntersectingHalfEdges {

    private ArrayList<HalfEdge> HalfEdges;
    private Point point;

    public IntersectingHalfEdges(ArrayList<HalfEdge> HalfEdges, Point point) {
        this.HalfEdges = HalfEdges;
        this.point = point;
    }

    public ArrayList<HalfEdge> getHalfEdges() {
        return HalfEdges;
    }

    public  Point getPoint() {
        return point;
    }
}
