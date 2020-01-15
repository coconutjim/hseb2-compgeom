package ComputationalGeometry.SegmentIntersection;

import ComputationalGeometry.Intersection.Intersection;
import ComputationalGeometry.Primitives.Point;
import ComputationalGeometry.misc.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 2/7/13
 * Time: 6:32 PM
 */
class sweepLine {
    public static List<Line> T = new ArrayList<Line>();

    public static void insert(Line s) {
        if (T.size() == 0) {
            T.add(s);
        } else {
            boolean insert = false;
            for(int i = 0; i < T.size(); ++i) {
                if (T.get(i).left.getY() < s.left.getY()) {
                    T.add(i, s);
                    insert = true;
                    break;
                }
            }
            if (!insert) {
                T.add(s);
            }
        }
    }

    public static void delete(Line s) {
        T.remove(s);
    }

    public static Line above(Line s) {
        int index = T.indexOf(s);
        if (index > 0) {
            return T.get(index - 1);
        } else {
            return null;
        }
    }

    public static Line below(Line s) {
        int index = T.indexOf(s);
        if (index != T.size() - 1 && index != -1) {
            return T.get(index + 1);
        } else {
            return null;
        }
    }
}

public class SegmentIntersection {

    public static void qSort(List<Point> A, int low, int high) {
        int i = low;
        int j = high;
        Point x = A.get((low+high)/2);  // x - опорный элемент посредине между low и high
        do {
            while ((A.get(i).getX() < x.getX()) || ((A.get(i).getX() == x.getX()) && (A.get(i).type == 0 && x.type == 1))
            || ((A.get(i).getX() == x.getX()) && (A.get(i).getY() < x.getY())))
                ++i;
            // поиск элемента для переноса в старшую часть
            while ((A.get(j).getX() > x.getX()) || ((A.get(j).getX() == x.getX()) && (A.get(j).type == 1 && x.type == 0))
                    || ((A.get(j).getX() == x.getX()) && (A.get(j).getY() > x.getY())))
                --j;
            // поиск элемента для переноса в младшую часть
            if(i <= j){
                // обмен элементов местами:
                Point temp = A.get(i);
                A.set(i, A.get(j));
                A.set(j, temp);
                // переход к следующим элементам:
                i++; j--;
            }
        } while(i < j);
        if(low < j) qSort(A, low, j);
        if(i < high) qSort(A, i, high);
    }

    public static boolean anySegmentsIntersect(List<Line> lines) {
        sweepLine hs = new sweepLine();
        List<Point> points = new ArrayList<Point>();
        for(Line l : lines) {
            Point temp = l.left;
            temp.type = 0;
            temp.line = l;
            points.add(temp);
            temp = l.right;
            temp.type = 1;
            temp.line = l;
            points.add(temp);
        }
        qSort(points, 0, points.size() - 1);

        for(Point p: points) {

            if (p.type == 0) {
                sweepLine.insert(p.line);
                Line above = sweepLine.above(p.line);
                Line below = sweepLine.below(p.line);
                if (above != null) {
                    if (Intersection.segmentsIntersect(above.left, above.right, p.line.left, p.line.right)) {
                        return true;
                    }
                }
                if (below != null) {
                    if (Intersection.segmentsIntersect(below.left, below.right, p.line.left, p.line.right)) {
                        return true;
                    }
                }
            } else {
                Line above = sweepLine.above(p.line);
                Line below = sweepLine.below(p.line);
                if ((above != null) && (below != null)) {
                    if (Intersection.segmentsIntersect(above.left, above.right, below.left, below.right)) {
                        return true;
                    }
                }
                sweepLine.delete(p.line);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        final Intersection intersection = new Intersection();
        final JFrame frame = new JFrame("AnySegmentsIntersect");

        frame.add(new JComponent() {
            List<Point> points = new ArrayList<Point>();
            List<Line> lines = new ArrayList<Line>();
            Line tempLine = null;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Point temp = new Point(e.getX(), e.getY());
                        points.add(temp);
                        if (tempLine != null) {
                            tempLine.right = temp;
                            lines.add(tempLine);
                            tempLine = null;
                        } else {
                            tempLine = new Line(temp, null);
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
                for(Point p: points) {
                    g.setColor(Color.BLACK);
                    g.drawOval((int) p.getX() - 4,(int) p.getY() - 4, 8, 8);
                }
                for(Line p : lines) {
                    g.drawLine((int)p.right.getX(),(int) p.right.getY(),
                            (int)p.left.getX(),(int) p.left.getY());
                }
                if (lines.size() >= 2 && points.size() % 2 == 0) {
                    if (anySegmentsIntersect(lines)) {
                        System.out.println("Пересечение есть");
                        frame.setTitle("Пересечение есть");
                    } else {
                        System.out.println("Пересечения нет");
                        frame.setTitle("Пересечения нет");
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
