package ComputationalGeometry.HalfplanesIntersection;

import ComputationalGeometry.Primitives.Halfplane;
import ComputationalGeometry.Primitives.Line;
import ComputationalGeometry.Primitives.Point;
import ComputationalGeometry.Primitives.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 4/22/13
 * Time: 1:07 AM
 */
public class HalfplanesIntersectionForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HalfplanesIntersection");
        frame.add(new JComponent() {

            List<Halfplane> halfplanes = new ArrayList<Halfplane>();
            Polygon result = null;
            Line inputLine = null;
            Point temp = null;
            Point temp2 = null;
            {
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (inputLine != null) {
                            char input = e.getKeyChar();
                            if (input == 'r') {
                                halfplanes.add(new Halfplane(inputLine, false));
                                inputLine = null;
                                temp = temp2 = null;
                            } else if (input == 'l') {
                                halfplanes.add(new Halfplane(inputLine, true));
                                inputLine = null;
                                temp = temp2 = null;
                            }
                            repaint();
                        } else {
                            char input = e.getKeyChar();
                            if (input == 'c') {
                                halfplanes.clear();
                                inputLine = null;
                                temp = temp2 = null;
                            }  else if (input == 's') {
                                result = HalfplanesIntersection.intersectHalfplanes(halfplanes);
//                                result = IntersectHalfplanes.intersectHalfplanes(halfplanes);
                                inputLine = null;
                                temp = temp2 = null;
                            }
                            repaint();
                        }
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (temp == null && inputLine == null) {
                            temp = new Point(e.getX(), e.getY());
                        } else if (inputLine == null) {
                            temp2 = new Point(e.getX(), e.getY());
                            inputLine = new Line(temp, temp2);
                        }
                        repaint();
                    }
                });
                setPreferredSize(new Dimension(400, 300));
                setFocusable(true);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                this.setBackground(Color.WHITE);
                if (temp != null) {
                    g2.drawOval((int)temp.getX() - 2, (int)temp.getY() - 2, 4, 4);
                }
                if (temp2 != null) {
                    g2.drawOval((int)temp2.getX() - 2, (int)temp2.getY() - 2, 4, 4);
                    g2.drawLine((int)temp.getX(), (int)temp.getY(), (int)temp2.getX(), (int)temp2.getY());
                }

                for (Halfplane halfplane : halfplanes) {
                    if (halfplane.rightSide) {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.setStroke(new BasicStroke(4));
                        g2.drawLine((int)(-halfplane.line.c / halfplane.line.a) - 4, -4,
                                (int)((-halfplane.line.c - halfplane.line.b * this.getHeight()) / halfplane.line.a) - 4,
                                this.getHeight() - 4);
                    } else {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.setStroke(new BasicStroke(4));
                        g2.drawLine((int)(-halfplane.line.c / halfplane.line.a) + 4, 4,
                                (int)((-halfplane.line.c - halfplane.line.b * this.getHeight()) / halfplane.line.a) + 4,
                                this.getHeight() + 4);
                    }
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine((int)(-halfplane.line.c / halfplane.line.a), 0,
                            (int)((-halfplane.line.c - halfplane.line.b * this.getHeight()) / halfplane.line.a),
                            this.getHeight());
                }

                if (result != null) {
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(3));
                    for (int i = 0; i < result.vertices.size(); ++i) {
                        g2.drawLine((int)result.vertices.get(i).getX(),
                                (int)result.vertices.get(i).getY(),
                                (int)result.vertices.get((i + 1) % result.vertices.size()).getX(),
                                (int)result.vertices.get((i + 1) % result.vertices.size()).getY());
                    }
                    result = null;
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