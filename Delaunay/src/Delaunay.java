import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import delaunay_triangulation.*;

/**
 * Created by iwno on 28.05.2014.
 */
public class Delaunay extends JFrame {
    MouseEvent event;
    boolean voron = false;
    List<Point_dt> ps = new ArrayList<Point_dt>();

    boolean flag = false;

    Delaunay_Triangulation dt = new Delaunay_Triangulation();

    JPanel panel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.BLACK);
            List<Point_dt> result = new ArrayList<Point_dt>();
            for (Point_dt p : ps) {
                result.add(p);
            }
            if (event != null) {
                result.add(new Point_dt(event.getX(), event.getY()));
            }
            dt = new Delaunay_Triangulation(result.toArray(new Point_dt[result.size()]));
            if (flag) {
                ps.remove(ps.size() - 1);
                flag = true;
            }
            Iterator<Triangle_dt> iter = dt.trianglesIterator();
            while(iter.hasNext()) {
                Triangle_dt tdt = iter.next();
                if (tdt.p3() != null) {
                    drawTriangle(tdt, g);
                }
            }
        }
    };

    public void drawPoint(Point_dt pdt, Graphics g) {
        g.fillOval((int)pdt.x() - 5, (int)pdt.y() - 5, 10, 10);
    }

    public void drawSegment(Point_dt pdt1, Point_dt pdt2, Graphics g) {
        g.drawLine((int)pdt1.x(), (int)pdt1.y(), (int)pdt2.x(), (int)pdt2.y());
        drawPoint(pdt1, g);
        drawPoint(pdt2, g);
    }

    public void drawTriangle(Triangle_dt tdt, Graphics g) {
        drawSegment(tdt.p1(), tdt.p2(), g);
        drawSegment(tdt.p2(), tdt.p3(), g);
        drawSegment(tdt.p3(), tdt.p1(), g);
    }

    public Delaunay() {
        setSize(600, 600);
        add(panel);
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                event = e;
                panel.repaint();
            }
        });
        panel.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ps.add(new Point_dt(e.getX(), e.getY()));
                panel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == 'v') {
                    voron = !voron;
                }
            }
        });
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Delaunay();
    }
}
