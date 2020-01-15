package ComputationalGeometry.MapOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/4/13
 * Time: 2:45 AM
 */
class Face {
    public HalfEdge outerComponent;
    public List<HalfEdge> innerComponents = new ArrayList<HalfEdge>();
    public List<Face> parents = new ArrayList<Face>();

    public HalfEdge getOuterComponent() {
        return outerComponent;
    }

    public void addParents(Face face) {
        if (!parents.contains(face)) parents.add(face);
    }
}