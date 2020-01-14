/**
 * Created by Lev on 08.03.14.
 */
public class Vertex extends Point {

    /** Полуребро, имеющее это начало */
    private HalfEdge incidentEdge;

    public Vertex(double x, double y) {
        super(x, y);
    }

    public Vertex(Point point) {
        super(point.getX(), point.getY());
    }

    public Vertex(Point point, HalfEdge incidentEdge) {
        super(point.getX(), point.getY());

        this.incidentEdge = incidentEdge;

    }

    public Vertex(double x, double y, HalfEdge incidentEdge) {
        super(x, y);

        this.incidentEdge = incidentEdge;
    }

    public HalfEdge getIncidentEdge() {
        return incidentEdge;
    }

    public void setIncidentEdge(HalfEdge incidentEdge) {
        this.incidentEdge = incidentEdge;
    }
}
