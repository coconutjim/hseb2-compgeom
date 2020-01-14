import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Task12 extends JFrame {

    java.util.List<Point> points = new ArrayList<Point>();
    java.util.List<Edge> result = new ArrayList<Edge>();



    public Task12() {
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


        setLayout(new GridLayout(0, 2));

        /** Панель отрисовки */
        final JPanel drawPanel = new JPanel() {

            {
                this.addMouseListener(new MouseAdapter() {
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
                //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLUE);
                this.setBackground(Color.LIGHT_GRAY);
                for (Point point: points) {
                    g2.fillOval((int) point.getX() - 4, (int) point.getY() - 4, 8, 8);
                }
                g2.setColor(Color.RED);
                for (Edge e : result) {
                    g2.drawLine((int)e.start.x, (int)e.start.y, (int)e.end.x, (int)e.end.y);
                }
            }

            @Override
            public void setInheritsPopupMenu(boolean value) {
                super.setInheritsPopupMenu(value);
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
        JButton buttonStart = new JButton("Найти пересечение");
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = Voronoi.createDiagram(points);
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
                points.clear();
                result.clear();
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
                Task12 f = new Task12();
                f.setVisible(true);
                f.setResizable(false);
                f.setTitle("Построение диаграммы Вороного. Осипов Лев 271ПИ НИУ ВШЭ");
            }
        });
    }
}
