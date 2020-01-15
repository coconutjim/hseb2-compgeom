package ComputationalGeometry.MapOverlay;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/4/13
 * Time: 2:42 AM
 */
public class Vertex extends java.awt.Point implements Comparable<Vertex> {
    //private HalfEdge segment;
    public ArrayList<HalfEdge> interHalfEdges = new ArrayList<HalfEdge>();
    public ArrayList<HalfEdge> halfEdges = new ArrayList<HalfEdge>();
    // for ex 6
    public HalfEdge incidentEdge;

    public Vertex(int x, int y) {
        super(x,y);
    }

    public Vertex(java.awt.Point p) {
        x = p.x;
        y = p.y;
    }

    public void setSegment(HalfEdge seg) {
        if (!halfEdges.contains(seg)) {
            halfEdges.add(seg);
        }
    }

    public ArrayList<HalfEdge> getSegment() {
        return halfEdges;
    }

    @Override
    public int compareTo(Vertex o) {
        return x - o.x;
    }

    public void addInter (HalfEdge s) {
        if (!interHalfEdges.contains(s)) {
            interHalfEdges.add(s);
        }
    }

    public void clearInter () {
        interHalfEdges.clear();
    }

    public ArrayList<HalfEdge> getInter () {
        return interHalfEdges;
    }

    public boolean equal(Vertex p) {
        return p.x < x + 4 && p.x > x - 4 && p.y < y + 4 && p.y > y - 4;
    }

    // for ex 6

    public HalfEdge getIncidentEdge() {
        return incidentEdge;
    }

    public void setIncidentEdge(HalfEdge incidentEdge) {
        this.incidentEdge = incidentEdge;
    }
}