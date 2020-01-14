
import java.util.ArrayList;

public class HalfPlane {

    private Line line;
    private boolean isRight;

    public HalfPlane(Line line, boolean isRight) {
        this.line = line;
        this.isRight = isRight;
    }
    
    public Line getLine() {
        return line;
    }
    
    public boolean leftSurface() {
        return isRight;
    }
    
    public boolean rightSurface() {
        return ! isRight;
    }
    
    public boolean contains(Point2D p) {
        if (leftSurface()) {
            if (line.isHorizontal()) return (p.getY() < line.getYbyX(0));
            else return (p.getX() > line.getXbyY(p.getY()));
        } else {
            if (line.isHorizontal()) return (p.getY() > line.getYbyX(0));
            else return (p.getX() < line.getXbyY(p.getY()));
        }
    }
    
    public static ArrayList<HalfPlane> halfplanesBoundary(double x1, double x2, double y1, double y2) {
        ArrayList<HalfPlane> result = new ArrayList<HalfPlane>();

        if (x1 > x2) {
            double temp = x1;
            x1 = x2;
            x2 = temp;
        }

        if (y1 > y2) {
            double temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        result.add(new HalfPlane(new Line(new Point2D(x1, 0), new Point2D(x1, 1)), true));
        result.add(new HalfPlane(new Line(new Point2D(0, y2), new Point2D(1, y2)), true));
        result.add(new HalfPlane(new Line(new Point2D(x2, 0), new Point2D(x2, 1)), false));
        result.add(new HalfPlane(new Line(new Point2D(0, y1), new Point2D(1, y1)), false));
        
        return result;
    }
}
