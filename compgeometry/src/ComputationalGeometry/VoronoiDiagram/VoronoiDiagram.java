package ComputationalGeometry.VoronoiDiagram;

import ComputationalGeometry.Primitives.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 5/12/13
 * Time: 3:05 PM
 */
public class VoronoiDiagram {
    public static LinkedList<Line> lines = new LinkedList<Line>();
    public static List<Edge> result = new ArrayList<Edge>();
    private static PriorityQueue sites = new PriorityQueue();
    private static PriorityQueue events = new PriorityQueue();
    public static Parabola root;

    public static List<Edge> voronoiDiagram(List<Point> points) {
        if(points.size() > 2)
            sites.clear();
        events.clear();
        result.clear();
        root = null;
        ListIterator i = points.listIterator(0);
        while (i.hasNext()) {
            sites.offer(i.next());
        }
        while (sites.size() > 0) {
            if ((events.size() > 0) && ((((CircleEvent) events.peek()).xpos) <= (((Point) sites.peek()).x))) {
                processCircleEvent((CircleEvent) events.poll());
            } else {
                frontInsert((Point) sites.poll());
            }
        }
        while (events.size() > 0) {
            processCircleEvent((CircleEvent) events.poll());
        }
        finishEdges();
        return result;
    }

    private static void processCircleEvent(CircleEvent event) {
        if (event.valid) {
            Edge edgy = new Edge(event.p);
            result.add(edgy);

            Parabola parc = event.a;
            if (parc.prev != null) {
                parc.prev.next = parc.next;
                parc.prev.edger = edgy;
            }
            if (parc.next != null) {
                parc.next.prev = parc.prev;
                parc.next.edgel = edgy;
            }
            if (parc.edgel != null) {
                parc.edgel.finish(event.p);
            }
            if (parc.edger != null) {
                parc.edger.finish(event.p);
            }
            if (parc.prev != null) {
                checkCircleEvent(parc.prev, event.xpos);
            }
            if (parc.next != null) {
                checkCircleEvent(parc.next, event.xpos);
            }
        }
    }

    static void frontInsert(Point focus) {
        if (root == null) {
            root = new Parabola(focus);
            return;
        }
        Parabola parc = root;
        while (parc != null) {
            Circle rez = intersect(focus, parc);
            if (rez.finished) {
                if (parc.next != null) {
                    Circle rezz = intersect(focus, parc.next);
                    if (!rezz.finished){
                        Parabola bla = new Parabola(parc.focus);
                        bla.prev = parc;
                        bla.next = parc.next;
                        parc.next.prev = bla;
                        parc.next = bla;
                    }
                } else {
                    parc.next = new Parabola(parc.focus);
                    parc.next.prev = parc;
                }
                parc.next.edger = parc.edger;
                Parabola bla = new Parabola(focus);
                bla.prev = parc;
                bla.next = parc.next;
                parc.next.prev = bla;
                parc.next = bla;
                parc = parc.next;
                parc.edgel = new Edge(rez.center);
                result.add(parc.edgel);
                parc.prev.edger = parc.edgel;
                parc.edger = new Edge(rez.center);
                result.add(parc.edger);
                parc.next.edgel = parc.edger;
                checkCircleEvent(parc, focus.x);
                checkCircleEvent(parc.prev, focus.x);
                checkCircleEvent(parc.next, focus.x);
                return;
            }
            parc = parc.next;
        }

        parc = root;
        while (parc.next != null) {
            parc = parc.next; // Find the last node.
        }
        parc.next = new Parabola(focus);
        parc.next.prev = parc;
        Point start = new Point(0, (parc.next.focus.y + parc.focus.y) / 2);
        parc.next.edgel = new Edge(start);
        result.add(parc.next.edgel);
        parc.edger = parc.next.edgel;

    }

    static void checkCircleEvent(Parabola parc, double xpos) {
        if ((parc.event != null) && (parc.event.xpos != xpos)) {
            parc.event.valid = false;
        }
        parc.event = null;
        if ((parc.prev == null) || (parc.next == null)) {
            return;
        }
        Circle result = circle(parc.prev.focus, parc.focus, parc.next.focus);
        if (result.finished && result.rightmostX > xpos) {
            parc.event = new CircleEvent(result.rightmostX, result.center, parc);
            events.offer(parc.event);
        }
    }

    static Circle circle(Point a, Point b, Point c) {
        Circle result = new Circle();
        if ((b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y) > 0) {
            result.finished = false;
            return result;
        }
        double A = b.x - a.x;
        double B = b.y - a.y;
        double C = c.x - a.x;
        double D = c.y - a.y;
        double E = A * (a.x + b.x) + B * (a.y + b.y);
        double F = C * (a.x + c.x) + D * (a.y + c.y);
        double G = 2 * (A * (c.y - b.y) - B * (c.x - b.x));
        if (G == 0) {
            result.finished = false;
            return result;
        }
        Point o = new Point((D * E - B * F) / G, (A * F - C * E) / G);
        result.center = o;
        result.rightmostX = o.x + Math.sqrt(Math.pow(a.x - o.x, 2.0) + Math.pow(a.y - o.y, 2.0));
        result.finished = true;
        return result;
    }

    static Circle intersect(Point p, Parabola i) {
        Circle res = new Circle();
        res.finished = false;
        if (i.focus.x == p.x) {
            return res;
        }
        double a = 0.0;
        double b = 0.0;
        if (i.prev != null) {
            a = intersection(i.prev.focus, i.focus, p.x).y;
        }
        if (i.next != null) {
            b = intersection(i.focus, i.next.focus, p.x).y;
        }
        if ((i.prev == null || a <= p.y) && (i.next == null || p.y <= b)) {
            res.center = new Point(0, p.y);
            res.center.x = (i.focus.x * i.focus.x + (i.focus.y - res.center.y) * (i.focus.y - res.center.y) - p.x * p.x) / (2 * i.focus.x - 2 * p.x);
            res.finished = true;
            return res;
        }
        return res;
    }

    static Point intersection(Point p0, Point p1, double l) {
        Point res = new Point(0, 0);
        Point p = p0;
        if (p0.x == p1.x) {
            res.y = (p0.y + p1.y) / 2;
        } else if (p1.x == l) {
            res.y = p1.y;
        } else if (p0.x == l) {
            res.y = p0.y;
            p = p1;
        } else {
            double z0 = 2 * (p0.x - l);
            double z1 = 2 * (p1.x - l);
            double a = 1 / z0 - 1 / z1;
            double b = -2 * (p0.y / z0 - p1.y / z1);
            double c = (p0.y * p0.y + p0.x * p0.x - l * l) / z0 - (p1.y * p1.y + p1.x * p1.x - l * l) / z1;
            res.y = (-b - Math.sqrt((b * b - 4 * a * c))) / (2 * a);
        }
        res.x = (p.x * p.x + (p.y - res.y) * (p.y - res.y) - l * l) / (2 * p.x - 2 * l);
        return res;
    }

    static void finishEdges() {
        double l = 1100;
        Parabola i = root;
        while (i != null) {
            if (i.edger != null) {
                i.edger.finish(intersection(i.focus, i.next.focus, l * 2));
            }
            i = i.next;
        }
    }

    public static class CircleEvent implements Comparable<CircleEvent> {
        public double xpos;
        public Point p;
        public Parabola a;
        public boolean valid;
        public CircleEvent(double X, Point P, Parabola A) {
            xpos = X;
            a = A;
            p = P;
            valid = true;
        }
        public int compareTo(CircleEvent foo) {
            return ((Double) this.xpos).compareTo((Double) foo.xpos);
        }
    }
}

