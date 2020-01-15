package ComputationalGeometry.HalfplanesIntersection;

import ComputationalGeometry.Primitives.Halfplane;
import ComputationalGeometry.Primitives.Line;
import ComputationalGeometry.Primitives.Point;
import ComputationalGeometry.Primitives.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/21/13
 * Time: 8:18 PM
 */
public class HalfplanesIntersection {
    public static Polygon intersectHalfplanes(List<Halfplane> halfplanes) {
        List<Halfplane> left = new ArrayList<Halfplane>();
        List<Halfplane> right = new ArrayList<Halfplane>();
        List<Halfplane> leftChain = new ArrayList<Halfplane>();
        List<Halfplane> rightChain = new ArrayList<Halfplane>();
        List<Halfplane> resLeft = new ArrayList<Halfplane>();
        List<Halfplane> resRight = new ArrayList<Halfplane>();

        for (Halfplane h : halfplanes) {
            if (h.rightSide) {
                right.add(h);
            } else {
                left.add(h);
            }
        }

        if (!left.isEmpty()) {
            leftChain.add(left.get(0));
            left.remove(0);
        }
        for (Halfplane h : left) {
            int insertPos = 0;

            while (insertPos < leftChain.size() && h.line.angle > leftChain.get(insertPos).line.angle) {
                insertPos++;
            }
            if (insertPos == 0) {
                while(leftChain.size() > 1 && !h.includes(
                        Line.intersection(leftChain.get(0).line, leftChain.get(1).line))) {
                    leftChain.remove(0);
                }
                leftChain.add(insertPos, h);
            } else if (insertPos == leftChain.size()) {
                while(leftChain.size() > 1 && !h.includes(
                        Line.intersection(leftChain.get(leftChain.size() - 1).line, leftChain.get(leftChain.size() - 2).line))) {
                    leftChain.remove(leftChain.size() - 1);
                    insertPos--;
                }
                leftChain.add(insertPos, h);
            }
            else {
                int vertices = 0;
                int i = 0;
                boolean intersection1;
                boolean intersection2;

                while (i < leftChain.size() - 1) {
                    intersection1 = intersection2 = false;

                    if (!h.includes(Line.intersection(leftChain.get(i).line, leftChain.get(i + 1).line))) {
                        vertices++;
                        intersection1 = true;
                    }
                    if (i < leftChain.size() - 2 && !h.includes(
                            Line.intersection(leftChain.get(i + 1).line, leftChain.get(i + 2).line))) {
                        vertices ++;
                        intersection2 = true;
                    }
                    if (intersection1 && intersection2) {
                        leftChain.remove(i + 1);
                        if (i + 1 < insertPos) {
                            insertPos--;
                        }
                    } else {
                        i++;
                    }
                }
                if (vertices > 0) {
                    leftChain.add(insertPos, h);
                }
            }
        }

        if (! right.isEmpty()) {
            rightChain.add(right.get(0));
            right.remove(0);
        }
        for (Halfplane h : right) {
            int insertPos = 0;

            while (insertPos < rightChain.size() && h.line.angle < rightChain.get(insertPos).line.angle) {
                insertPos++;
            }
            if (insertPos == 0) {
                while(rightChain.size() > 1 && !h.includes(
                        Line.intersection(rightChain.get(0).line, rightChain.get(1).line))) {
                    rightChain.remove(0);
                }
                rightChain.add(insertPos, h);
            } else if (insertPos == rightChain.size()) {
                while(rightChain.size() > 1 && !h.includes(
                        Line.intersection(rightChain.get(rightChain.size() - 1).line, rightChain.get(rightChain.size() - 2).line))) {
                    rightChain.remove(rightChain.size() - 1);
                    insertPos--;
                }
                rightChain.add(insertPos, h);
            } else {
                int vertices = 0;
                int i = 0;
                boolean intersection1;
                boolean intersection2;

                while (i < rightChain.size() - 1) {
                    intersection1 = intersection2 = false;

                    if (!h.includes(
                            Line.intersection(rightChain.get(i).line, rightChain.get(i+1).line))) {
                        vertices++;
                        intersection1 = true;
                    }
                    if (i < rightChain.size() - 2 && !h.includes(
                            Line.intersection(rightChain.get(i+1).line, rightChain.get(i+2).line))) {
                        vertices++;
                        intersection2 = true;
                    }
                    if (intersection1 && intersection2) {
                        rightChain.remove(i + 1);
                        if (i + 1 < insertPos) {
                            insertPos--;
                        }
                    } else {
                        i++;
                    }
                }

                if (vertices > 0) {
                    rightChain.add(insertPos, h);
                }
            }
        }

        int startLeft = 0, endLeft = leftChain.size() - 1,
                startRight = 0, endRight = rightChain.size() - 1;
        Point intersection;

        for (int i = 0; i < leftChain.size(); i++) {
            for (int j = 0; j < rightChain.size(); j++) {
                if (((j == 0) || !leftChain.get(i).includes(Line.intersection(rightChain.get(j-1).line, rightChain.get(j).line))) &&
                        ((j == rightChain.size()-1) || leftChain.get(i).includes(Line.intersection(rightChain.get(j).line, rightChain.get(j+1).line))) &&
                        ((i == 0) || ! rightChain.get(j).includes(Line.intersection(leftChain.get(i - 1).line, leftChain.get(i).line))) &&
                        ((i == leftChain.size()-1) || rightChain.get(j).includes(Line.intersection(leftChain.get(i).line, leftChain.get(i + 1).line)))) {
                    startLeft = i;
                    startRight = j;
                } else if (((j == 0) || leftChain.get(i).includes(Line.intersection(rightChain.get(j-1).line, rightChain.get(j).line))) && ((j == rightChain.size()-1) || !leftChain.get(i).includes(Line.intersection(rightChain.get(j).line, rightChain.get(j+1).line))) &&
                        ((i == 0) || rightChain.get(j).includes(Line.intersection(leftChain.get(i - 1).line, leftChain.get(i).line))) &&
                        ((i == leftChain.size()-1) || ! rightChain.get(j).includes(Line.intersection(leftChain.get(i).line, leftChain.get(i + 1).line)))) {
                    endLeft = i;
                    endRight = j;
                }
            }
        }

        for (int i = startLeft; i <= endLeft; i++) {
            resLeft.add(leftChain.get(i));
        }
        for (int j = startRight; j <= endRight; j++) {
            resRight.add(rightChain.get(j));
        }

        Polygon res = createPolygonFromHalfplaneChains(resLeft, resRight);

        return res;
    }

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
