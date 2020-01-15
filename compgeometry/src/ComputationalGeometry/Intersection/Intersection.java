package ComputationalGeometry.Intersection;

import ComputationalGeometry.Primitives.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 1/14/13
 * Time: 12:49 PM
 */
public class Intersection {

    public static boolean segmentsIntersect(Point p1, Point p2, Point p3, Point p4) {
        double d1, d2, d3, d4;
        d1 = direction(p3, p4, p1);
        d2 = direction(p3, p4, p2);
        d3 = direction(p1, p2, p3);
        d4 = direction(p1, p2, p4);

        if ((d1 == 0) && onSegment(p3, p4, p1)) {
            return true;
        } else if ((d2 == 0) && onSegment(p3, p4, p2)) {
            return true;
        } else if ((d3 == 0) && onSegment(p1, p2, p3)) {
            return true;
        } else if ((d3 == 0) && onSegment(p1, p2, p4)) {
            return true;
        } else if ((d1 / Math.abs(d1) != d2 / Math.abs(d2)) &&
                   (d3 / Math.abs(d3) != d4 / Math.abs(d4))) {
            return true;
        } else {
            return false;
        }
    }

    public static double direction(Point p1, Point p2, Point p3) {
        return crossProduct(p3.subtract(p1), p2.subtract(p1));
    }

    public static boolean onSegment(Point p1, Point p2, Point p3) {
        return
            Math.min(p1.getX(), p2.getX()) <= p3.getX() &&
            p3.getX() <= Math.max(p1.getX(), p2.getX()) &&
            Math.min(p1.getY(), p2.getY()) <= p3.getY() &&
            p3.getY() <= Math.max(p1.getY(), p2.getY());
    }

    public static double crossProduct(Point p1, Point p2) {
        return p1.getX() * p2.getY() - p1.getY() * p2.getX();
    }

    public static double crossProduct(Point p0, Point p1, Point p2) {
        return (p1.getX() - p0.getX()) * (p2.getY() - p0.getY()) -
                (p2.getX() - p0.getX()) * (p1.getY() - p0.getY());
    }

    public static boolean isLeftTurn(Point p0, Point p1, Point p2) {
        return (crossProduct(p0, p1, p2) > 0);
    }

    public static boolean isRightTurn(Point p0, Point p1, Point p2) {
        return (crossProduct(p0, p1, p2) < 0);
    }

    public static void main(String[] args) {
        final Intersection intersection = new Intersection();
        JFrame frame = new JFrame("Intersection");

        frame.add(new JComponent() {
            int numberOfPoints = 0;
            List<Point> points = new ArrayList<Point>();
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (numberOfPoints == 0) {
                            points.clear();
                        }
                        Point temp = new Point(e.getX(), e.getY());
                        points.add(temp);
                        numberOfPoints += 1;
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
                for(Point p : points) {
                    g.setColor(Color.BLACK);
                    g.drawOval((int)p.getX() - 4, (int)p.getY() - 4, 8, 8);
                }
                if (numberOfPoints == 2 | numberOfPoints == 3) {
                    g.drawLine((int)points.get(0).getX(), (int)points.get(0).getY(),
                            (int)points.get(1).getX(), (int)points.get(1).getY());
                } else if (numberOfPoints == 4) {
                    numberOfPoints = 0;
                    if (intersection.segmentsIntersect(
                            points.get(0), points.get(1), points.get(2), points.get(3))) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawLine((int)points.get(0).getX(), (int)points.get(0).getY(),
                            (int)points.get(1).getX(), (int)points.get(1).getY());

                    g.drawLine((int)points.get(2).getX(), (int)points.get(2).getY(),
                            (int)points.get(3).getX(), (int)points.get(3).getY());

                    g.setColor(Color.BLACK);
                }
            }

            @Override
            public void setInheritsPopupMenu(boolean value) {
                super.setInheritsPopupMenu(value);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
