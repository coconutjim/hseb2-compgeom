import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import delaunay_triangulation.*;

public class Task13 extends JFrame {


    MouseEvent event;
    boolean voron;
    java.util.List<Point_dt> ps;

    boolean flag;

    Delaunay_Triangulation dt;

    public Task13() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int w = d.width;
        int h = d.height;
        setSize(3 * w / 4, 3 * h / 4);
        setLocation(w / 8, h / 8);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        reset();
        createGUI();
    }

    private void reset() {
        event = null;
        voron = false;
        ps = new ArrayList<Point_dt>();
        flag = false;
        dt = new Delaunay_Triangulation();
    }

    private void createGUI() {


        setLayout(new GridLayout(0, 2));

        /** Панель отрисовки */
        final JPanel drawPanel = new JPanel() {


            {
                final JPanel link = this;
                this.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        super.mouseMoved(e);
                        event = e;
                       link.repaint();
                    }
                });
                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        ps.add(new Point_dt(e.getX(), e.getY()));
                        link.repaint();
                    }
                });
                this.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyChar() == 'v') {
                            voron = !voron;
                        }
                    }
                });
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                //((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                this.setBackground(Color.LIGHT_GRAY);
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

            public void drawPoint(Point_dt pdt, Graphics g) {
                g.setColor(Color.BLUE);
                g.fillOval((int)pdt.x() - 4, (int)pdt.y() - 4, 8, 8);
            }

            public void drawSegment(Point_dt pdt1, Point_dt pdt2, Graphics g) {
                g.setColor(Color.RED);
                g.drawLine((int)pdt1.x(), (int)pdt1.y(), (int)pdt2.x(), (int)pdt2.y());
                drawPoint(pdt1, g);
                drawPoint(pdt2, g);
            }

            public void drawTriangle(Triangle_dt tdt, Graphics g) {
                drawSegment(tdt.p1(), tdt.p2(), g);
                drawSegment(tdt.p2(), tdt.p3(), g);
                drawSegment(tdt.p3(), tdt.p1(), g);
            }
        };
        drawPanel.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
        add(drawPanel);

        /** Панель */
        final JPanel panelGUI = new JPanel();
        panelGUI.setBackground(Color.LIGHT_GRAY);
        panelGUI.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight()));
        add(panelGUI);

        panelGUI.setLayout(new BoxLayout(panelGUI, BoxLayout.Y_AXIS));

        /** Кнопка запуска алгоритма */
        JButton buttonClear = new JButton("Очистить");
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
                drawPanel.repaint();
            }
        });
        buttonClear.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(buttonClear);

    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Task13 f = new Task13();
                f.setVisible(true);
                f.setResizable(false);
                f.setTitle("Триангуляция Делоне. Осипов Лев 271ПИ НИУ ВШЭ");
            }
        });
    }
}
