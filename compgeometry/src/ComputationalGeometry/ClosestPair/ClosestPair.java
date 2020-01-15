package ComputationalGeometry.ClosestPair;

import ComputationalGeometry.misc.Point;

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
 * Time: 2:53 PM
 */
class Answer {
    public double distance;
    public Point firstP;
    public Point secondP;

    public Answer(double d, Point f, Point s) {
        distance = d;
        firstP = f;
        secondP = s;
    }
}

public class ClosestPair {

    public static void qSortX(List<Point> A, int low, int high) {
        int i = low;
        int j = high;
        Point x = A.get((low+high)/2);  // x - опорный элемент посредине между low и high
        do {
            while(A.get(i).getX() < x.getX()) ++i;
            // поиск элемента для переноса в старшую часть
            while(A.get(j).getX() > x.getX()) --j;
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
        if(low < j) qSortX(A, low, j);
        if(i < high) qSortX(A, i, high);
    }

    public static void qSortY(List<Point> A, int low, int high) {
        int i = low;
        int j = high;
        Point x = A.get((low+high)/2);  // x - опорный элемент посредине между low и high
        do {
            while(A.get(i).getY() < x.getY()) ++i;
            // поиск элемента для переноса в старшую часть
            while(A.get(j).getY() > x.getY()) --j;
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
        if(low < j) qSortY(A, low, j);
        if(i < high) qSortY(A, i, high);
    }

    public static Answer closestPair(List<Point> xP, List<Point> yP)
    {
        if (xP.size() <= 3) {
            if (xP.size() < 2) {
                return new Answer(Integer.MAX_VALUE, xP.get(0), xP.get(0));
            } else {
                double minDistance = xP.get(0).distance(xP.get(1));
                Answer answer = new Answer(minDistance, xP.get(0), xP.get(1));
                for(int i = 0; i < xP.size() - 1; ++i) {
                    for(int j = i + 1; j < xP.size(); ++j) {
                        if (xP.get(i).distance(xP.get(j)) < answer.distance) {
                            answer.distance = xP.get(i).distance(xP.get(j));
                            answer.firstP = xP.get(i);
                            answer.secondP = xP.get(j);
                        }
                    }
                }
                return answer;
            }
        } else {
            List<Point> xL = new ArrayList<Point>();
            List<Point> xR = new ArrayList<Point>();
            for (int i = 0; i < xP.size(); ++i) {
                if (i <= (xP.size() + 1) / 2) {
                    xL.add(xP.get(i));
                } else {
                    xR.add(xP.get(i));
                }
            }
            int xM = xP.get((xP.size() + 1) / 2).getIntX();
            List<Point> yL = new ArrayList<Point>();
            List<Point> yR = new ArrayList<Point>();
            for (int i = 0; i < yP.size(); ++i) {
                if (yP.get(i).getX() <= xM) {
                    yL.add(yP.get(i));
                } else {
                    yR.add(yP.get(i));
                }
            }
            Answer answer1, answer2, minAnswer;
            answer1 = closestPair(xL, yL);
            answer2 = closestPair(xR, yR);
            if (answer1.distance < answer2.distance) {
                minAnswer = answer1;
            } else {
                minAnswer = answer2;
            }
            List<Point> yS = new ArrayList<Point>();
            for(int i = 0; i < yP.size(); ++i) {
                if (Math.abs(xM - yP.get(i).getX()) < minAnswer.distance) {
                    yS.add(yP.get(i));
                }
            }
            int nS = yS.size();
            Answer closest = new Answer(minAnswer.distance, minAnswer.firstP, minAnswer.secondP);
            for(int i = 0; i < nS - 1; ++i) {
                int k = i + 1;
                while ((k < nS) && (Math.abs(yS.get(k).getY() - yS.get(i).getY()) < minAnswer.distance)) {
                    if (Math.abs(yS.get(k).distance(yS.get(i))) < closest.distance) {
                        closest.distance = Math.abs(yS.get(k).distance(yS.get(i)));
                        closest.firstP = yS.get(k);
                        closest.secondP = yS.get(i);
                    }
                    ++k;
                }
            }
            return closest;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClosestPair");

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
                    g.drawOval(p.getIntX() - 4, p.getIntY() - 4, 8, 8);
                }
                if (points.size() >= 3) {
                    List<Point> xP = new ArrayList<Point>(points);
                    List<Point> yP = new ArrayList<Point>(points);
                    qSortX(xP, 0, xP.size() - 1);
                    qSortY(yP, 0, yP.size() - 1);
                    System.out.println();
                    Answer closestPair = closestPair(xP, yP);
                    System.out.println(closestPair.distance);
                    System.out.println(closestPair.firstP.print());
                    System.out.println(closestPair.secondP.print());
                    System.out.println();
                    g.setColor(new Color(0, 100, 0));
                    g.drawOval(closestPair.firstP.getIntX() - 4, closestPair.firstP.getIntY() - 4, 8, 8);
                    g.drawOval(closestPair.secondP.getIntX() - 4, closestPair.secondP.getIntY() - 4, 8, 8);
                    g.fillOval(closestPair.firstP.getIntX() - 4, closestPair.firstP.getIntY() - 4, 8, 8);
                    g.fillOval(closestPair.secondP.getIntX() - 4, closestPair.secondP.getIntY() - 4, 8, 8);
                    g.setColor(Color.BLACK);
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
