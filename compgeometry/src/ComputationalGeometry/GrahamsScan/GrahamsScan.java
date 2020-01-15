package ComputationalGeometry.GrahamsScan;

import ComputationalGeometry.Intersection.Intersection;
import ComputationalGeometry.Primitives.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 1/27/13
 * Time: 12:08 PM
 */
public class GrahamsScan {
    static Stack<Point> stack = new Stack<Point>();

    public static void qSort(List<Point> A, int low, int high) {
        int i = low;
        int j = high;
        Point x = A.get((low+high)/2);  // x - опорный элемент посредине между low и high
        do {
            while(Intersection.crossProduct(A.get(i).subtract(A.get(0)), x.subtract(A.get(0))) > 0) ++i;
                    // поиск элемента для переноса в старшую часть
            while(Intersection.crossProduct(A.get(j).subtract(A.get(0)), x.subtract(A.get(0))) < 0) --j;
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

    private static void grahamScan(List<Point> points, Graphics g) throws InterruptedException {
        firstElement(points);
        qSort(points, 1, points.size() - 1);
        stack.add(points.get(0));
        stack.add(points.get(1));
        for(int i = 2; i < points.size(); ++i) {
            while (Intersection.direction(stack.elementAt(stack.size() - 2), stack.elementAt(stack.size() - 1),
                    points.get(i)) >= 0) {
                stack.pop();

            }
            stack.add(points.get(i));
        }
    }

    private static void firstElement(List<Point> points) {
        for(int i = 1; i < points.size(); ++i) {
            if (points.get(i).getY() < points.get(0).getY()) {
                Point supertemp = points.get(0);
                points.set(0, points.get(i));
                points.set(i, supertemp);
            } else if (points.get(i).getY() == points.get(0).getY()) {
                if (points.get(i).getX() < points.get(0).getX()) {
                    Point supertemp = points.get(0);
                    points.set(0, points.get(i));
                    points.set(i, supertemp);
                }
            }
        }
    }

    public static void main(String[] args) {
        final Intersection intersection = new Intersection();
        JFrame frame = new JFrame("Graham");

        frame.add(new JComponent() {
            List<Point> points = new ArrayList<Point>();

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Point temp = new Point(e.getX(), e.getY());
                        points.add(temp);
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
                    g.drawOval((int)p.getX() - 4,(int) p.getY() - 4, 8, 8);
                }
                if (points.size() > 3) {
                    try {
                        grahamScan(points, g);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    for(int i = 0; i < stack.size() - 1; ++i) {
                        g.drawLine((int)stack.elementAt(i).getX(), (int)stack.elementAt(i).getY(),
                                (int)stack.elementAt(i + 1).getX(), (int)stack.elementAt(i + 1).getY());
                    }
                    g.drawLine((int)stack.elementAt((int)stack.size() - 1).getX(), (int)stack.elementAt(stack.size() - 1).getY(),
                            (int)stack.elementAt(0).getX(), (int)stack.elementAt(0).getY());
                    stack.clear();
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
