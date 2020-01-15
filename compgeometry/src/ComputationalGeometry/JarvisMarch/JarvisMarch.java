package ComputationalGeometry.JarvisMarch;

import ComputationalGeometry.Intersection.Intersection;
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
 * Date: 1/27/13
 * Time: 12:08 PM
 */
public class JarvisMarch {

    public static List<Point> jarvisMarch(List<Point> points)
    {

        Point min = points.get(0);
        int minIndex = 0;
        for (int i = 1; i < points.size(); ++i)
        {
            if (points.get(i).getY() < min.getY())
            {
                min = points.get(i);
                minIndex = i;
            }
            else
            {
                if (points.get(i).getY() == min.getY() && points.get(i).getX() < min.getX())
                {
                    min = points.get(i);
                    minIndex = i;
                }
            }
        }

        boolean[] marked = new boolean[points.size()];

        Point p = min;
        Point dir = min;
        List<Point> convex = new ArrayList<Point>();

        do
        {
            int i = 0;
            while (marked[i] && i < points.size())
            {
                i++;
            }
            Point temp_min = points.get(i);
            int index = i;
            for (; i < points.size(); i++)
            {
                if (!marked[i])
                {
                    double res = Intersection.direction(temp_min, points.get(i), dir);
                    if (res < 0)
                    {
                        temp_min = points.get(i);
                        index = i;
                    }
                    else
                    {
                        if (res == 0 && p.distance(temp_min) < p.distance(points.get(i)))
                        {
                            temp_min = points.get(i);
                            index = i;
                        }

                    }
                }
            }
            p = temp_min;
            marked[index] = true;
            convex.add(p);
            dir = p;
        }
        while (p != min);

        return convex;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jarvis");

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
                    List<Point> points1 = jarvisMarch(points);
                    for(int i = 0; i < points1.size() - 1; ++i) {
                        g.drawLine((int)points1.get(i).getX(), (int)points1.get(i).getY(),
                                (int)points1.get(i + 1).getX(), (int)points1.get(i + 1).getY());
                    }
                    g.drawLine((int)points1.get(points1.size() - 1).getX(), (int)points1.get(points1.size() - 1).getY(),
                            (int)points1.get(0).getX(), (int)points1.get(0).getY());
                    points1.clear();
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
