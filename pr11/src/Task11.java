import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Task11 extends JFrame {

    private ArrayList<HalfPlane> halfPlanes;
    private Polygon intersection;
    private Point2D d_start, d_end;


    private int state;
    private double drag_x, drag_y;

    public Task11() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int w = d.width;
        int h = d.height;
        setSize(3 * w / 4, 3 * h / 4);
        setLocation(w / 8, h / 8);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createGUI();
    }

    private void createGUI() {

        halfPlanes = HalfPlane.halfplanesBoundary(-5000, 5000, -5000, 5000);
        state = 0;

        setLayout(new GridLayout(0, 2));

        /** Панель отрисовки */
        final JPanel drawPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
                //g2.clearRect(0, 0, getWidth(), getHeight());

                //d.setBackground(Color.LIGHT_GRAY);

                // line coordinates
                int x1, x2, y1, y2;
                for (HalfPlane h : halfPlanes) {
                    if (h.getLine().isVertical()) {
                        y1 = 0;
                        y2 = getHeight();
                        x1 = (int)h.getLine().getXbyY(0);
                        x2 = (int)h.getLine().getXbyY(getHeight());
                    } else {
                        x1 = 0;
                        x2 = getWidth();
                        y1 = (int)h.getLine().getYbyX(0);
                        y2 = (int)h.getLine().getYbyX(getWidth());
                    }

                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine(x1, this.getHeight() - y1, x2, this.getHeight() - y2);

                    // highlighting
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(1));

                    int isLeft = h.leftSurface() ? 1 : -1;
                    int isAscending = isLeft * (h.getLine().isAscending() ? 1 : -1);
                    if (! h.getLine().isVertical() && Math.abs(h.getLine().getYbyX(0) - h.getLine().getYbyX(1)) < 1) isLeft = 0;
                    else isAscending = 0;

                    for (int j = 1; j <= 3; j ++) {
                        g2.drawLine(x1 + j * isLeft, this.getHeight() - y1 + j * isAscending, x2 + j * isLeft, this.getHeight() - y2 + j * isAscending);
                    }
                }
                if (state == 1) { // dragging
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine((int) d_start.getX(), this.getHeight() - (int) d_start.getY(), (int) drag_x, (int) drag_y);

                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(1));
                    int isLeft = (this.getHeight() - d_start.getY() > drag_y) ? 1 : -1;
                    int isAscending = (d_start.getX() > drag_x) ? -1 : 1;
                    if (Math.abs(this.getHeight() - d_start.getY() - drag_y) > Math.abs(d_start.getX() - drag_x)) isAscending = 0;
                    else isLeft = 0;

                    for (int j = 1; j <= 3; j ++) {
                        g2.drawLine((int)d_start.getX() + j * isLeft, getHeight() - (int)d_start.getY() + j * isAscending,
                                (int)drag_x + j * isLeft, (int)drag_y + j * isAscending);
                    }
                }
                if (state == 2) { // intersected
                    g2.setColor(Color.GREEN);
                    g2.setStroke(new BasicStroke(2));
                    Point2D current,
                            next;
                    for (int i = 0; i < intersection.size(); i++) {
                        current = intersection.get(i);
                        next = intersection.get((i + 1) % intersection.size());
                        for (int j = -1; j <= 2; j ++) {
                            g2.drawLine((int)current.getX() + ((j + 1) / 2), (int)(getHeight() - current.getY() + (j / 2)),
                                    (int)next.getX() + ((j + 1) / 2), (int)(getHeight() - next.getY() + (j / 2)));
                        }
                    }
                }
            }
        };
        drawPanel.setBackground(Color.LIGHT_GRAY);
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (state == 0) {
                    d_start = new Point2D(e.getX(), drawPanel.getHeight() - e.getY());
                    state = 1;
                } else if (state == 1) {
                    d_end = new Point2D(e.getX(), drawPanel.getHeight() - e.getY());
                    boolean isRight = d_start.getY() < d_end.getY();
                    if (d_start.getY() == d_end.getY()) {
                        isRight = d_start.getX() < d_end.getX();
                    }
                    halfPlanes.add(new HalfPlane(new Line(d_start, d_end), isRight));
                    state = 0;
                }
                drawPanel.repaint();
            }
        });
        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                drag_x = e.getX();
                drag_y = e.getY();
                drawPanel.repaint();
            }
        });
        drawPanel.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
        add(drawPanel);

        /** Панель */
        final JPanel panelGUI = new JPanel();
        panelGUI.setBackground(Color.LIGHT_GRAY);
        panelGUI.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight()));
        add(panelGUI);

        panelGUI.setLayout(new BoxLayout(panelGUI, BoxLayout.Y_AXIS));

        /** Кнопка запуска алгоритма */
        JButton buttonStart = new JButton("Найти пересечение");
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intersection = HalfPlaneIntersection.intersection(halfPlanes);
                state = 2;
                drawPanel.repaint();
            }
        });
        buttonStart.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(buttonStart);

        /** Кнопка запуска алгоритма */
        JButton buttonClear = new JButton("Очистить");
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                halfPlanes = HalfPlane.halfplanesBoundary(-5000, 5000, -5000, 5000);
                state = 0;
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
                Task11 f = new Task11();
                f.setVisible(true);
                f.setResizable(false);
                f.setTitle("Поиск пересечения полуплоскостей. Осипов Лев 271ПИ НИУ ВШЭ");
            }
        });
    }
}
