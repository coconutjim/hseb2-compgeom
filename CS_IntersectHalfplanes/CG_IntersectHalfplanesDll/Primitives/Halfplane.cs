namespace CG_IntersectHalfplanesDll.Primitives
{
    public class Halfplane {
        public Line line;
        public bool rightSide;
        public Halfplane(Line line, bool rightSide) {
            this.line = line;
            this.rightSide = rightSide;
        }
        public bool includes(Point p) {
            if (rightSide) {
                if ((line.a == 0 && line.b != 0)) { // == lower boundary
                    return (p.getY() > -line.c / line.b);
                } else {
                    return (p.getX() < (-line.c - line.b * p.getY()) / line.a);
                }
            } else {
                if ((line.a == 0 && line.b != 0)) { // == upper boundary
                    return (p.getY() < -line.c / line.b);
                } else {
                    return (p.getX() > (-line.c - line.b * p.getY()) / line.a);
                }
            }
        }
    }
}
