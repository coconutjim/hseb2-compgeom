package ComputationalGeometry.Primitives;

import ComputationalGeometry.VoronoiDiagram.VoronoiDiagram;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 5/19/13
 * Time: 3:48 PM
 */
public class Parabola {
    public Point focus;
    public Parabola next;
    public Parabola prev;
    public VoronoiDiagram.CircleEvent event;
    public Edge edgel;
    public Edge edger;

    public Parabola(Point focus) {
        this.focus = focus;
    }
}
