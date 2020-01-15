package ComputationalGeometry.FindIntersections;

import ComputationalGeometry.Primitives.Point;
import ComputationalGeometry.misc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/1/13
 * Time: 2:54 PM
 */
public class FindIntersections {
    private static AATree Q = null;
    private static SweepLine sT = null;
    private static List<Line> all_lines = null;
    private static List<Line> intersections = null;
    private static List<Answer> answer = null;
    private static List<EventPoint> intersection_points = null;

    public static List<Line> findIntersections(List<Line> S) {

        for (Line line : S) {
            Q.insert(new EventPoint(line.lower, false));
            Q.insert(new EventPoint(line.upper, false));
        }

        while (!Q.isEmpty()) {
            EventPoint ep = (EventPoint) Q.getMinAndRemove();
            handleEventPoint(ep);
        }

        return intersections;
    }

    private static void handleEventPoint(EventPoint p) {
        List<Line> U = new ArrayList<Line>();
        for (Line line : all_lines) {
            if (line.upper.equals(p.point)) {
                U.add(line);
            }
        }
        List<Line> L = new ArrayList<Line>(sT.getL(p));
        List<Line> C = new ArrayList<Line>(sT.getC(p));

        if (U.size() + L.size() + C.size() > 1) {
            Answer ans = new Answer();
            ans.intersection_l.addAll(U);
            ans.intersection_l.addAll(L);
            ans.intersection_l.addAll(C);
            ans.intersection_p = p.point;
            answer.add(ans);
        }

        sT.removeAll(L);
        sT.removeAll(C);

        sT.sort(p);

        sT.insertAll(U);
        sT.insertAll(C);

        if (U.size() + C.size() == 0) {
            Line sl = sT.getLeftNeighbor(p);
            Line sr = sT.getRightNeighbor(p);
            if (sl != null && sr != null) {
                findNewEvent(sl, sr, p);
            }
        } else {
            U.addAll(C);
            Line stl = sT.getLFromList(U);
            Line str = sT.getRFromList(U);

            Line sl = sT.getLFromLine(stl);
            if (sl != null && sl.upper.equals(stl.upper) && sl.lower.equals(stl.lower)) {
                sl = null;
            }
            Line sr = sT.getRFromLine(str);
            if (sr != null && sr.upper.equals(str.upper) && sr.lower.equals(str.lower)) {
                sr = null;
            }
            if (sl != null) {
                for (EventPoint point : intersection_points) {
                    if (point.point.equals(p)) {
                        return;
                    }
                }
                findNewEvent(sl, stl, p);
            }
            if (sr != null) {
                for (EventPoint point : intersection_points) {
                    if (point.point.equals(p)) {
                        return;
                    }
                }
                findNewEvent(str, sr, p);
            }
        }
    }

    private static void findNewEvent(Line sl, Line sr, EventPoint ep) {
        Point inters = SweepLine.getIntersectionPoint(sl, sr);
        if (inters != null && inters.getY() >= ep.point.getY()) {
            EventPoint ep2 = new EventPoint(inters, true);
            Q.insert(ep2);
            intersection_points.add(ep2);
        }
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("FindIntersections");
        final JTextArea textArea = new JTextArea(5, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        final JFrame answerFrame = new JFrame("OutputFrame");
        answerFrame.add(textArea);
        answerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        answerFrame.pack();
        answerFrame.setVisible(true);

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
                            if ((tempLine.upper.getY() < temp.getY()) ||
                                    (tempLine.upper.getY() == temp.getY() &&
                                     tempLine.upper.getX() <= temp.getX())) {
                                tempLine.lower = temp;
                                lines.add(tempLine);
                            } else {
                                tempLine.lower = tempLine.upper;
                                tempLine.upper = temp;
                                lines.add(tempLine);
                            }
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
                points.add(new Point(50, 50));
                points.add(new Point(150, 50));
                points.add(new Point(50, 150));
                points.add(new Point(20, 50));
                points.add(new Point(100, 100));
                points.add(new Point(200, 200));
                points.add(new Point(100, 150));
                points.add(new Point(200, 150));
                points.add(new Point(200, 100));
                points.add(new Point(100, 200));
                lines.add(new Line(new Point(50, 50), new Point(150, 50)));
                lines.add(new Line(new Point(50, 50), new Point(50, 150)));
                lines.add(new Line(new Point(20, 50), new Point(50, 50)));
                lines.add(new Line(new Point(100, 100), new Point(200, 200)));
                lines.add(new Line(new Point(100, 150), new Point(200, 150)));
                lines.add(new Line(new Point(200, 100), new Point(100, 200)));
                super.paintComponent(g);
                for(Point p: points) {
                    g.setColor(Color.BLACK);
                    g.drawOval((int)p.getX() - 4, (int)p.getY() - 4, 8, 8);
                }
                for(Line p : lines) {
                    g.drawLine((int)p.upper.getX(), (int)p.upper.getY(),
                            (int)p.lower.getX(),(int) p.lower.getY());
                }
                if (lines.size() >= 2 && points.size() % 2 == 0) {
                    textArea.setText("");
                    all_lines = lines;
                    intersection_points = new ArrayList<EventPoint>();
                    Q = new AATree();
                    sT = new SweepLine();
                    intersections = new ArrayList<Line>();
                    answer = new ArrayList<Answer>();
                    FindIntersections fi = new FindIntersections();
                    fi.findIntersections(lines);
                    g.setColor(Color.GREEN);
                    for (Answer a : answer) {
                        for (Line l : a.intersection_l) {
                            textArea.append(l.toString() + " ");
                            g.drawLine((int)l.upper.getX(), (int)l.upper.getY(),
                                    (int) l.lower.getX(), (int)l.lower.getY());
                        }
                        textArea.append("пересекаются в точке [" + a.intersection_p.getX() + ", " +
                                a.intersection_p.getY() + "]\n");

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

class Answer {
    public Point intersection_p;
    public List<Line> intersection_l = new ArrayList<Line>();
}