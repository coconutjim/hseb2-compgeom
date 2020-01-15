package ComputationalGeometry.FindIntersections;

import ComputationalGeometry.Primitives.Point;
import ComputationalGeometry.Intersection.Intersection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/1/13
 * Time: 3:00 PM
 */
class LineAndPoint implements Comparable<LineAndPoint> {
    public Line line;
    public Point point;

    public int compareTo(LineAndPoint lap) {
        return (int)(this.point.getX() - lap.point.getX());
    }
}

public class SweepLine {
    private static List<LineAndPoint> T = new ArrayList<LineAndPoint>();
    private static Line sweepLine = new Line(null, null);

    public static void sort(EventPoint ep) {
        sweepLine = new Line(new Point(0, ep.point.getY() + 1), new Point(1000, ep.point.getY() + 3));
        for (LineAndPoint lap : T) {
            if (lap.line.upper.getY() == lap.line.lower.getY()) {
                Point inters = new Point(lap.line.upper.getX(), 0);
                lap.point = inters;
            } else {
                Point inters = getIntersectionPoint(lap.line, sweepLine);
                if (inters != null) {
                    lap.point = inters;
                }
            }
        }
        Collections.sort(T);
    }

    public static void insertAll(List<Line> new_lines) {
        for (Line line : new_lines) {
            insert(line);
        }
    }

    public static void insert(Line new_line) {
        Point p = null;
        if (new_line.upper.getY() == new_line.lower.getY()) {
            p = new Point(new_line.upper.getX(), 0);
        } else {
            p = getIntersectionPoint(new_line, sweepLine);
        }
        if (p != null) {
            LineAndPoint lap = new LineAndPoint();
            lap.line = new_line;
            lap.point = p;
            if (T.size() == 0) {
                T.add(lap);
            } else {
                for (int i = 0; i < T.size(); ++i) {
                    if (T.get(i).point.getX() > lap.point.getX()) {
                        T.add(i, lap);
                        return;
                    }
                }
                T.add(lap);
            }
        }
    }

    public static void removeAll(List<Line> lines) {
        for (int i = lines.size() - 1; i >= 0; --i) {
            for (int j = T.size() - 1; j >= 0; --j) {
                if (T.get(j).line.upper.equals(lines.get(i).upper) &&
                        T.get(j).line.lower.equals(lines.get(i).lower)) {
                    T.remove(j);
                    break;
                }
            }
        }
    }

    public static List<Line> getL(EventPoint p) {
        List<Line> L = new ArrayList<Line>();

        for (LineAndPoint line : T) {
            if (line.line.lower.equals(p.point)) {
                L.add(line.line);
            }
        }

        return L;
    }

    public static Line getLFromList(List<Line> l) {
        for (LineAndPoint lap : T) {
            if (l.contains(lap.line)) {
                for (Line line : l) {
                    if (lap.line.upper.equals(line.upper) &&
                            lap.line.lower.equals(line.lower)) {
                        return lap.line;
                    }
                }
            }
        }
        return null;
    }


    public static Line getRFromList(List<Line> l) {
        List<LineAndPoint> rT = new ArrayList<LineAndPoint>(T);
        Collections.reverse(rT);
        for (LineAndPoint lap : rT) {
            for (Line line : l) {
                if (lap.line.upper.equals(line.upper) &&
                        lap.line.lower.equals(line.lower)) {
                    return lap.line;
                }
            }
        }
        return null;
    }

    public static Line getLFromLine(Line l) {
        for (int i = 0; i < T.size(); ++i) {
            if (l.upper.equals(T.get(i).line.upper) &&
                    l.lower.equals(T.get(i).line.lower)) {
                if (i != 0) {
                    return T.get(i - 1).line;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static Line getRFromLine(Line l) {
        for (int i = T.size() - 1; i >= 0; --i) {
            if (l.upper.equals(T.get(i).line.upper) &&
                    l.lower.equals(T.get(i).line.lower)) {
                if (i != T.size() - 1) {
                    return T.get(i + 1).line;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static List<Line> getC(EventPoint p) {
        List<Line> C = new ArrayList<Line>();

        for (LineAndPoint line : T) {
            double dist =
                    (p.point.getX() - line.line.upper.getX()) * (line.line.lower.getY() - line.line.upper.getY()) -
                            (p.point.getY() - line.line.upper.getY()) * (line.line.lower.getX() - line.line.upper.getX());
            if (!line.line.lower.equals(p.point) &&
                    !line.line.upper.equals(p.point) &&
                     Math.abs(dist) < 0.1) {
                C.add(line.line);
            }
        }

        return C;
    }

    public static Line getLeftNeighbor(EventPoint ep) {
        Line candidate = null;
        double min = Integer.MAX_VALUE;

        for (LineAndPoint line : T) {
            if (ep.point.getX() - line.line.lower.getX() > 0 &&
                    ep.point.getX() - line.line.lower.getX() < min) {
                min = ep.point.getX() - line.line.lower.getX();
                candidate = line.line;
            }
        }

        return candidate;
    }

    public static Line getRightNeighbor(EventPoint ep) {
        Line candidate = null;
        double min = Integer.MAX_VALUE;

        for (LineAndPoint line : T) {
            if (line.line.lower.getX() - ep.point.getX() > 0 &&
                    line.line.lower.getX() - ep.point.getX() < min) {
                min = line.line.lower.getX() - ep.point.getX();
                candidate = line.line;
            }
        }

        return candidate;
    }

    public static Point getIntersectionPoint(Line sl, Line sr) {
        if (Intersection.segmentsIntersect(sl.lower, sl.upper, sr.lower, sr.upper)) {
            double d = (sl.upper.getX() - sl.lower.getX()) * (sr.lower.getY() - sr.upper.getY()) -
                    (sl.upper.getY() - sl.lower.getY()) * (sr.lower.getX() - sr.upper.getX());
            double da = (sl.upper.getX() - sr.upper.getX()) * (sr.lower.getY() - sr.upper.getY()) -
                    (sl.upper.getY() - sr.upper.getY()) * (sr.lower.getX() - sr.upper.getX());
            double db = (sl.upper.getX() - sl.lower.getX()) * (sl.upper.getY() - sr.upper.getY()) -
                    (sl.upper.getY() - sl.lower.getY()) * (sl.upper.getX() - sr.upper.getX());
            double ta = da / d;
            double tb = db / d;
            if (0 <= ta && ta <= 1 && 0 <= tb && tb <= 1) {
                return new Point(sl.upper.getX() + ta * (sl.lower.getX() - sl.upper.getX()),
                        sl.upper.getY() + ta * (sl.lower.getY() - sl.upper.getY()));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
