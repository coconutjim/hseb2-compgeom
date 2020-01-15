package ComputationalGeometry.VoronoiDiagram;

import ComputationalGeometry.Primitives.Edge;
import ComputationalGeometry.Primitives.Point;

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
 * Date: 5/12/13
 * Time: 3:22 PM
 */
public class VoronoiDiagramForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("VoronoiDiagram");
        frame.setSize(400, 300);
        frame.add(new JComponent() {

            List<Point> points = new ArrayList<Point>();
            List<Edge> result = new ArrayList<Edge>();
            {
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        char input = e.getKeyChar();
                        if (input == 'c') {
                            points.clear();
                            result.clear();
                        }  else if (input == 's') {
                            result = VoronoiDiagram.voronoiDiagram(points);
                        }
                        repaint();
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        points.add(new Point(e.getX(), e.getY()));
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
                for (Point point: points) {
                    g2.drawOval((int) point.getX() - 2, (int) point.getY() - 2, 4, 4);
                }

                for(Edge e : result) {
                    g2.drawLine((int)e.start.x, (int)e.start.y, (int)e.end.x, (int)e.end.y);
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
