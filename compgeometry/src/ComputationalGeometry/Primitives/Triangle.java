package ComputationalGeometry.Primitives;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/8/13
 * Time: 9:06 AM
 */
public class Triangle {

    public Point a, b, c;
    protected Triangle adjAB, adjBC, adjAC;


    public Triangle (Point A, Point B, Point C) {
        a = A;
        b = B;
        c = C;
        adjAB = adjAC = adjBC = null;
        double sum = (b.getX() - a.getX())*
                (b.getY() + a.getY()) +
                (c.getX() - b.getX())*
                        (c.getY() + b.getY()) +
                (a.getX() - c.getX())*
                        (a.getY() + c.getY());
        if (sum > 0) {
            Point temp = a;
            a = b;
            b = temp;
        }
    }

    public boolean pointOnTheEdge(Point p) {
        return getSideLine(Side.AB).pointOnLine(p) ||
                getSideLine(Side.BC).pointOnLine(p) ||
                getSideLine(Side.AC).pointOnLine(p);
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Point getC() {
        return c;
    }

    public Point getIth(int i) {
        switch (i) {
            case 0: return a;
            case 1: return b;
            case 2: return c;
            default: return null;
        }
    }

    public Line getSideLine(Side s) {
        switch(s) {
            case AB: return new Line(this.getA(), this.getB());
            case BC: return new Line(this.getB(), this.getC());
            case AC: return new Line(this.getA(), this.getC());
            default: return null;
        }
    }

    public enum Side {
        AB(0), BC(1), AC(2);

        Side(int i) {
            this.i = i;
        }

        int i;
    }

    @Override
    public Object clone() {

        Triangle res = new Triangle(a, b, c);
        res.adjAB = this.adjAB;
        res.adjBC = this.adjBC;
        res.adjAC = this.adjAC;
        //res.tag = this.tag;

        return res;
    }

    @Override
    public boolean equals(Object o2) {

        if (! (o2 instanceof Triangle)) {
            return false;
        }

        Triangle t2 = (Triangle) o2;

        return this.getA().equals(t2.getA()) &&
                this.getB().equals(t2.getB()) &&
                this.getC().equals(t2.getC()) ||
                this.getA().equals(t2.getB()) &&
                        this.getB().equals(t2.getC()) &&
                        this.getC().equals(t2.getA()) ||
                this.getA().equals(t2.getC()) &&
                        this.getB().equals(t2.getA()) &&
                        this.getC().equals(t2.getB()) ||
                this.getA().equals(t2.getC()) &&
                        this.getB().equals(t2.getB()) &&
                        this.getC().equals(t2.getA()) ||
                this.getA().equals(t2.getB()) &&
                        this.getB().equals(t2.getA()) &&
                        this.getC().equals(t2.getC()) ||
                this.getA().equals(t2.getA()) &&
                        this.getB().equals(t2.getC()) &&
                        this.getC().equals(t2.getB());
    }
}
