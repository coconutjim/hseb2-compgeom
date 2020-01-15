package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 5/19/13
 * Time: 3:48 PM
 */
public class Edge {
    public Point start;
    public Point end;
    public boolean finished = false;

    public Edge(Point start) {
        this.start = start;
        this.end = new Point(0, 0);
    }

    public void finish(Point end) {
        if (!finished) {
            this.end = end;
            finished = true;
        }
    }
}
