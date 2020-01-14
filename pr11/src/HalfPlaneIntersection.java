
import java.util.ArrayList;


public class HalfPlaneIntersection {

    public static Polygon intersection(ArrayList<HalfPlane> halfPlanes) {
        
        halfPlanes = calculateHorizontalVertices(halfPlanes);
        ArrayList<HalfPlane> left = new ArrayList<HalfPlane>();
        ArrayList<HalfPlane> right = new ArrayList<HalfPlane>();
        
        for (HalfPlane hp : halfPlanes) {
            if (hp.leftSurface()) left.add(hp);
            else right.add(hp);
        }

        ArrayList<HalfPlane> leftChain = new ArrayList<HalfPlane>();
        ArrayList<HalfPlane> rightChain = new ArrayList<HalfPlane>();
        
        if (! left.isEmpty()) {
            leftChain.add(left.get(0));
            left.remove(0);
        }
        
        for (HalfPlane hp : left) {
            int position = 0;
            while (position < leftChain.size() && hp.getLine().getAngle() >
                    leftChain.get(position).getLine().getAngle()) {
                ++ position;
            }
            if (position == 0) {
                while (leftChain.size() > 1 && ! hp.contains(Line.getIntersection(leftChain.get(0).getLine(),
                        leftChain.get(1).getLine()))) {
                    leftChain.remove(0);
                }
                leftChain.add(position, hp);
            } else if (position == leftChain.size()) {
                while(leftChain.size() > 1 && ! hp.contains(Line.getIntersection(
                        leftChain.get(leftChain.size() - 1).getLine(),
                        leftChain.get(leftChain.size() - 2).getLine()))) {
                    leftChain.remove(leftChain.size()-1);
                    -- position;
                }
                leftChain.add(position, hp);
            } else {
                int vertex = 0;
                int i = 0;
                boolean b1, b2;

                while (i < leftChain.size() - 1) {
                    b1 = b2 = false;
                    
                    if (! hp.contains(Line.getIntersection(leftChain.get(i).getLine(),
                            leftChain.get(i + 1).getLine()))) {
                        ++ vertex;
                        b1 = true;
                    }
                    if (i < leftChain.size() - 2 && ! hp.contains(Line.getIntersection(leftChain.get(i + 1).getLine(),
                            leftChain.get(i + 2).getLine()))) {
                        ++ vertex;
                        b2 = true;
                    }
                    
                    if (b1 && b2) {
                        leftChain.remove(i + 1);
                        if (i + 1 < position) {
                            -- position;
                        }
                    } else {
                        ++ i;
                    }
                }
                if (vertex > 0) {
                    leftChain.add(position, hp);
                }
            }
        }
        
        if (! right.isEmpty()) {
            rightChain.add(right.get(0));
            right.remove(0);
        }
        
        for (HalfPlane hp : right) {
            int position = 0;
            while (position < rightChain.size() && hp.getLine().getAngle() <
                    rightChain.get(position).getLine().getAngle()) {
                ++ position;
            }
            if (position == 0) {
                while(rightChain.size() > 1 && ! hp.contains(Line.getIntersection(rightChain.get(0).getLine(),
                        rightChain.get(1).getLine()))) {
                    rightChain.remove(0);
                }
                rightChain.add(position, hp);
            } else if (position == rightChain.size()) {
                while (rightChain.size() > 1 && ! hp.contains(Line.getIntersection(
                        rightChain.get(rightChain.size() - 1).getLine(),
                        rightChain.get(rightChain.size() - 2).getLine()))) {
                    rightChain.remove(rightChain.size() - 1);
                    -- position;
                }
                rightChain.add(position, hp);
            } else {
                int vertex = 0;
                int i = 0;
                boolean b1, b2;
                while (i < rightChain.size() - 1) {
                    b1 = b2 = false;
                    if (! hp.contains(Line.getIntersection(rightChain.get(i).getLine(),
                            rightChain.get(i + 1).getLine()))) {
                        ++ vertex;
                        b1 = true;
                    }
                    if (i < rightChain.size() - 2 && ! hp.contains(Line.getIntersection(rightChain.get(i + 1).getLine(),
                            rightChain.get(i + 2).getLine()))) {
                        ++ vertex;
                        b2 = true;
                    }
                    if (b1 && b2) {
                        rightChain.remove(i + 1);
                        if (i + 1 < position) {
                            -- position;
                        }
                    } else {
                        ++ i;
                    }
                }
                if (vertex > 0) {
                    rightChain.add(position, hp);
                }
            }
        }
        
        ArrayList<HalfPlane> lr = new ArrayList<HalfPlane>();
        ArrayList<HalfPlane> rr = new ArrayList<HalfPlane>();

        int startLeft = 0, endLeft = leftChain.size() - 1,
            startRight = 0, endRight = rightChain.size() - 1;

        for (int i = 0; i < leftChain.size(); ++ i) {
            for (int j = 0; j < rightChain.size(); ++ j) {
                
                if (((j == 0) || ! leftChain.get(i).contains
                        (Line.getIntersection(rightChain.get(j - 1).getLine(), rightChain.get(j).getLine()))) &&
                    ((j == rightChain.size()-1) || leftChain.get(i).contains
                            (Line.getIntersection(rightChain.get(j).getLine(), rightChain.get(j + 1).getLine()))) &&
                    ((i == 0) || ! rightChain.get(j).contains
                            (Line.getIntersection(leftChain.get(i - 1).getLine(), leftChain.get(i).getLine()))) &&
                    ((i == leftChain.size()-1) || rightChain.get(j).contains
                            (Line.getIntersection(leftChain.get(i).getLine(), leftChain.get(i + 1).getLine())))) {
                    
                    startLeft = i;
                    startRight = j;
                    
                } else if (((j == 0) || leftChain.get(i).contains
                        (Line.getIntersection(rightChain.get(j - 1).getLine(), rightChain.get(j).getLine()))) &&
                    ((j == rightChain.size()-1) || ! leftChain.get(i).contains
                            (Line.getIntersection(rightChain.get(j).getLine(), rightChain.get(j + 1).getLine()))) &&
                    ((i == 0) || rightChain.get(j).contains
                            (Line.getIntersection(leftChain.get(i - 1).getLine(), leftChain.get(i).getLine()))) &&
                    ((i == leftChain.size()-1) || ! rightChain.get(j).contains
                            (Line.getIntersection(leftChain.get(i).getLine(), leftChain.get(i + 1).getLine())))) {
                    
                    endLeft = i;
                    endRight = j;
                }
            }
        }
        
        for (int i = startLeft; i <= endLeft; ++ i) {
            lr.add(leftChain.get(i));
        }
        for (int j = startRight; j <= endRight; ++ j) {
            rr.add(rightChain.get(j));
        }

        return createPolygon(lr, rr);
    }

    private static C recursive_dc(ArrayList<HalfPlane> halfPlanes) {

        if (halfPlanes.size() == 1) {
            return new C(halfPlanes.get(0));
        }
        
        ArrayList<HalfPlane> hp1 = new ArrayList<HalfPlane>();
        ArrayList<HalfPlane> hp2 = new ArrayList<HalfPlane>();
        
        for (int i = 0; i < halfPlanes.size() / 2; ++ i) {
            hp1.add(halfPlanes.get(i));
        }
        for (int i = halfPlanes.size() / 2; i < halfPlanes.size(); ++ i) {
            hp2.add(halfPlanes.get(i));
        }
        C c1 = recursive_dc(hp1);
        C c2 = recursive_dc(hp2);

        return C.intersection(c1, c2);
    }
                        
    private static Polygon createPolygon(ArrayList<HalfPlane> left, ArrayList<HalfPlane> right) {
        Polygon result = new Polygon();
        for (int i = 0; i < left.size() - 1; ++ i) {
            result.add(Line.getIntersection(left.get(i).getLine(),left.get(i + 1).getLine()));
        }
        if (! left.isEmpty() && ! right.isEmpty()) {
            Point2D bottom = Line.getIntersection(left.get(left.size() - 1).getLine(),
                    right.get(right.size() - 1).getLine());
            Point2D lowestExistent = new Point2D(0, Double.MAX_VALUE);
            if (left.size() > 1) {
                lowestExistent = Line.getIntersection(left.get(left.size() - 1).getLine(),
                        left.get(left.size() - 2).getLine());
            }
            if (right.size() > 1) {
                Point2D temp = Line.getIntersection(right.get(right.size() - 1).getLine(),
                        right.get(right.size() - 2).getLine());
                if (temp.getY() < lowestExistent.getY()) {
                    lowestExistent = temp;
                }
            }
            if (! bottom.isNanDot() && bottom.getY() <= lowestExistent.getY()) {
                result.add(bottom);
            }
        }
        for (int i = right.size()-1; i > 0; i--) {
            result.add(Line.getIntersection(right.get(i).getLine(), right.get(i - 1).getLine()));
        }

        if (! left.isEmpty() && ! right.isEmpty()) {
            Point2D upper = Line.getIntersection(left.get(0).getLine(), right.get(0).getLine());
            Point2D highestExistent = new Point2D(0, Double.MIN_VALUE);
            if (left.size() > 1) {
                highestExistent = Line.getIntersection(left.get(0).getLine(), left.get(1).getLine());
            }
            if (right.size() > 1) {
                Point2D temp = Line.getIntersection(right.get(0).getLine(), right.get(1).getLine());
                if (temp.getY() > highestExistent.getY()) {
                    highestExistent = temp;
                }
            }
            if (! upper.isNanDot() && upper.getY() >= highestExistent.getY()) {
                result.add(upper);
            }
        }

        return result;
    }
    
    private static ArrayList<HalfPlane> calculateHorizontalVertices(ArrayList<HalfPlane> halfPlanes) {

        ArrayList<HalfPlane> result = new ArrayList<HalfPlane>();

        HalfPlane lv = null,
                  rv = null,
                  uh = null,
                  lh = null;
        
        for (HalfPlane hp : halfPlanes) {
            if (hp.leftSurface() && hp.getLine().isVertical()) {
                if (lv == null || hp.getLine().getXbyY(0) > lv.getLine().getXbyY(0)) {
                    lv = hp;
                }
            } else if (hp.rightSurface() && hp.getLine().isVertical()) {
                if (rv == null || hp.getLine().getXbyY(0) < rv.getLine().getXbyY(0)) {
                    rv = hp;
                }
            } else if (hp.leftSurface() && hp.getLine().isHorizontal()) {
                if (uh == null || hp.getLine().getYbyX(0) < uh.getLine().getYbyX(0)) {
                    uh = hp;
                }
            } else if (hp.rightSurface() && hp.getLine().isHorizontal()) {
                if (lh == null || hp.getLine().getYbyX(0) > lh.getLine().getYbyX(0)) {
                    lh = hp;
                }
            } else {
                result.add(hp);
            }
        }
        
        if (lv != null) result.add(lv);
        if (rv != null) result.add(rv);
        if (uh != null) result.add(uh);
        if (lh != null) result.add(lh);
        
        return result;
    }

}
