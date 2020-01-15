package ComputationalGeometry.IntersectHalfplanes;

import ComputationalGeometry.Primitives.Halfplane;
import ComputationalGeometry.Primitives.Line;
import ComputationalGeometry.Primitives.Point;
import ComputationalGeometry.Primitives.Polygon;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 5/12/13
 * Time: 2:26 PM
 */
public class IntersectHalfplanes {
//    public static Polygon intersectHalfplanes(List<Halfplane> halfplanes) {
//        ConvexRegion C = _intersectHalfplanes(halfplanes);

        // Building up the resulting polygon
        // from the left and from the right chain
//        Polygon res = createPolygonFromHalfplaneChains(C.left, C.right);
//
//        return res;
//    }

//    private static ConvexRegion _intersectHalfplanes(List<Halfplane> halfplanes) {
//        if (halfplanes.size() == 1) {
//            return new ConvexRegion(halfplanes.get(0));
//        }
//
//        List<Halfplane> h1 = new ArrayList<Halfplane>();
//        List<Halfplane> h2 = new ArrayList<Halfplane>();
//
//        for (int i = 0; i < halfplanes.size(); ++i) {
//            if (i < halfplanes.size() / 2) {
//                h1.add(halfplanes.get(i));
//            } else {
//                h2.add(halfplanes.get(i));
//            }
//        }
//
//        ConvexRegion c1 = _intersectHalfplanes(h1);
//        ConvexRegion c2 = _intersectHalfplanes(h2);
//
//        ConvexRegion c  = ConvexRegion.intersectConvexRegions(c1, c2);
//        System.out.println("Merged: " + c.left.size()+"+"+c.right.size());
//        System.out.println();
//
//        return c;
//
//    }

    private static Polygon createPolygonFromHalfplaneChains(List<Halfplane> left, List<Halfplane> right) {
        Polygon res = new Polygon();

        for(int i = 0; i < left.size() - 1; i++) {
            res.add(Line.intersection(left.get(i).line, left.get(i + 1).line));
        }

        if (!left.isEmpty() && !right.isEmpty()) {
            Point bottom = Line.intersection(left.get(left.size() - 1).line, right.get(right.size() - 1).line);
            Point lowestExistent = new Point(0, Double.MAX_VALUE);

            if (left.size() > 1) {
                lowestExistent = Line.intersection(left.get(left.size() - 1).line,
                        left.get(left.size()-2).line);
            }

            if (right.size() > 1) {
                Point temp = Line.intersection(right.get(right.size() - 1).line, right.get(right.size() - 2).line);
                if (temp.getY() < lowestExistent.getY()) {
                    lowestExistent = temp;
                }
            }

            if (!(bottom == null) && bottom.getY() <= lowestExistent.getY()) {
                res.add(bottom);
            }
        }

        for(int i = right.size() - 1; i > 0; i--) {
            res.add(Line.intersection(right.get(i).line, right.get(i-1).line));
        }


        if (!left.isEmpty() && !right.isEmpty()) {
            Point upper = Line.intersection(left.get(0).line, right.get(0).line);
            Point highestExistent = new Point(0, Double.MIN_VALUE);

            if (left.size() > 1) {
                highestExistent = Line.intersection(left.get(0).line, left.get(1).line);
            }

            if (right.size() > 1) {
                Point temp = Line.intersection(right.get(0).line, right.get(1).line);
                if (temp.getY() > highestExistent.getY()) {
                    highestExistent = temp;
                }
            }

            if (!(upper == null) && upper.getY() >= highestExistent.getY()) {
                res.add(upper);
            }
        }
        return res;
    }
}
