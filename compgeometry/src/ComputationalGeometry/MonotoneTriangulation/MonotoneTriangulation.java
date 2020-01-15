package ComputationalGeometry.MonotoneTriangulation;

import ComputationalGeometry.Intersection.Intersection;
import ComputationalGeometry.Primitives.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/8/13
 * Time: 9:02 AM
 */
public class MonotoneTriangulation {

    static boolean isClockwise;
    public static ArrayList<Triangle> result;

    public static ArrayList<Triangle> triangulate(ComputationalGeometry.Primitives.Polygon p) {
        result = new ArrayList<Triangle>();
        isClockwise = p.isClockwise();
        divideAndConquer(p);
        return result;
    }

    private static void divideAndConquer(ComputationalGeometry.Primitives.Polygon p) {

        if(p.size() < 3) {
            return;
        }
        if(p.size() == 3) {
            result.add(new Triangle(p.get(0), p.get(1), p.get(2)));
            return;
        }

        boolean diag = true;
        int start = 0, finish = 0;
        int prev = 0, next = 0;

        outer: for(int i = 0; i < p.size()-1; i++) {
            for (int j = i+2; j < p.size(); j++) {

                if (i == j || Math.abs(i-j) == 1 ||
                        (i == p.size()-1 && j == 0) ||
                        (j == p.size()-1 && i == 0)) {
                    continue;
                }

                start = i;
                finish = j;
                diag = true;

                for (int k = 0; k < p.size(); k++) {

                    if (i != k && j != k && i != (k+1)%p.size() && j != (k+1)%p.size() &&
                            Intersection.segmentsIntersect(p.get(i), p.get(j),
                                    p.get(k), p.get((k + 1) % p.size()))) {
                        diag = false; break;
                    }
                }

                prev = i - 1;
                if (prev < 0) prev += p.size();
                next = (i + 1) % p.size();

                if (p.isConvex(i)) {
                    if (! (Intersection.isRightTurn(p.get(i), p.get(j), p.get(next)) ^ isClockwise &&
                            Intersection.isRightTurn(p.get(i), p.get(prev), p.get(j)) ^ isClockwise)) {
                        diag = false;
                    }
                } else {
                    if (! (Intersection.isLeftTurn(p.get(i), p.get(j), p.get(prev)) ^ isClockwise ||
                            Intersection.isLeftTurn(p.get(i), p.get(next), p.get(j)) ^ isClockwise)) {
                        diag = false;
                    }
                }

                if (diag) {
                    break outer;
                }
            }
        }


        divideAndConquer(p.subPolygon(start, finish));
        divideAndConquer(p.subPolygon(finish, start));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MonotonePolygonTriangulation");
        final List<ComputationalGeometry.Primitives.Point> points = new ArrayList<ComputationalGeometry.Primitives.Point>();
        final ComputationalGeometry.Primitives.Polygon polygon = new ComputationalGeometry.Primitives.Polygon();

        frame.add(new JComponent() {
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        ComputationalGeometry.Primitives.Point newPoint = new ComputationalGeometry.Primitives.Point(e.getX(), e.getY());
                        boolean  isFinished = false;
                        for (ComputationalGeometry.Primitives.Point point: points) {
                            if (Math.abs(point.getX() - newPoint.getX()) < 5 &&
                                    Math.abs(point.getY() - newPoint.getY()) < 5) {
                                polygon.finished = true;
                                isFinished = true;
                                break;
                            }
                        }
                        if (!isFinished) {
                            points.add(newPoint);
                            polygon.add(newPoint);
                        }
                        if (polygon.finished) {
                            polygon.finished = false;
                            triangulate(polygon);
                            polygon.vertices.clear();
                            points.clear();
                        }
                    }
                    public void mouseReleased(MouseEvent e) {
                        repaint();
                    }
                });
                setPreferredSize(new Dimension(400, 300));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(Color.WHITE);
                for (int i = 0; i < polygon.vertices.size(); ++i) {
                    g.drawLine((int) polygon.vertices.get(i).getX(),
                            (int) polygon.vertices.get(i).getY(),
                            (int) polygon.vertices.get((i + 1) % polygon.vertices.size()).getX(),
                            (int) polygon.vertices.get((i + 1) % polygon.vertices.size()).getY());
                }

                for (int i = 0; i < polygon.vertices.size(); ++i) {
                    g.fillOval((int)polygon.vertices.get(i).getX() - 4, (int)polygon.vertices.get(i).getY() - 4, 8, 8);
                }

                if (result != null) {
                    for (Triangle triangle: result) {
                        g.drawLine((int)triangle.a.getX(),(int) triangle.a.getY(),(int)
                                triangle.b.getX(),(int) triangle.b.getY());
                        g.drawLine((int)triangle.b.getX(),(int) triangle.b.getY(),(int)
                                triangle.c.getX(),(int) triangle.c.getY());
                        g.drawLine((int) triangle.c.getX(), (int) triangle.c.getY(), (int)
                                triangle.a.getX(), (int) triangle.a.getY());
                        g.fillOval((int) triangle.a.getX() - 4, (int) triangle.a.getY() - 4, 8, 8);
                        g.fillOval((int)triangle.b.getX() - 4, (int)triangle.b.getY() - 4, 8, 8);
                        g.fillOval((int)triangle.c.getX() - 4, (int)triangle.c.getY() - 4, 8, 8);

                    }
                }

            }

            @Override
            public void setInheritsPopupMenu(boolean value) {
                super.setInheritsPopupMenu(value);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
