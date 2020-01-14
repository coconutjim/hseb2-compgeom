
public class Parabola {


    public Point focus;
    public Parabola next;
    public Parabola prev;
    public Voronoi.CircleEvent event;
    public Edge edgel;
    public Edge edger;

    public Parabola(Point focus) {
        this.focus = focus;
    }
}
