/**
 * Created by Lev on 08.03.14.
 */
public class HalfEdge extends Segment {
    /** Начало */
    private Vertex origin;

    /** Близнец */
    private HalfEdge twin;

    /** Поверхность, которую это полуребро ограничивает */
    private Face incidentFace;

    /** Предыдущее полуребро на границе incidentFace */
    private HalfEdge prev;

    /** Следующее полуребро на границе incidentFace */
    private HalfEdge next;

    public HalfEdge(Vertex origin, Vertex end) {
        super(origin, end);
        this.origin = origin;
    }

    public HalfEdge(Vertex origin, Vertex end, HalfEdge prev, HalfEdge next) {
        super(origin, end);
        this.origin = origin;
        this.twin = new HalfEdge(end, origin);
        twin.setTwin(this);
        if (prev != null) {
            twin.setNext(prev.getTwin());
        }
        if (next != null) {
            twin.setPrev(next.getTwin());
        }
        this.prev = prev;
        this.next = next;

        this.origin.setIncidentEdge(this);
        twin.origin.setIncidentEdge(twin);
    }

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    public HalfEdge getTwin() {
        return twin;
    }

    public void setTwin(HalfEdge twin) {
        this.twin = twin;
    }

    public Face getIncidentFace() {
        return incidentFace;
    }

    public void setIncidentFace(Face incidentFace) {
        this.incidentFace = incidentFace;
    }

    public HalfEdge getNext() {
        return next;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    public HalfEdge getPrev() {
        return prev;

    }

    public void setPrev(HalfEdge prev) {
        this.prev = prev;
    }

    public void setOrigin (Point point) {
        origin.x = point.getX();
        origin.y = point.getY();
    }
}
