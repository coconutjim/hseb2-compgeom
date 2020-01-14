/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Рисование области точек и выпуклой оболочки.
 */
public class DrawPanel extends JPanel {

    /** Размер области для рисования */
    private static final int SIZE = 300;

    DrawPanel() {
        setBackground(Color.LIGHT_GRAY);
    }

    /** Все точки */
    private ArrayList<Point> all;

    /** Точки оболочки */
    private StackG<Point> st;

    /** Рисовать ли последнюю линию */
    private boolean end = false;

    public void setPoints(StackG<Point> st) {
        this.st = st;
    }

    public void setAllPoints(ArrayList<Point> all) {
        this.all = all;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public static int getSIZE() {
        return SIZE;
    }

    /**
     * Рисует точки и линии в зависимости от состояния стека
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        if (all != null) {
            for (Point p : all) {
                g.fillOval((int)p.getX() - 4, SIZE - (int)p.getY() - 4, 8, 8); // координаты не центра
            }
        }
        g.setColor(Color.BLACK);
        if (st != null) {
            for (int i = 1; i < st.size(); ++ i) {
                g.drawLine((int)st.get(i).getX(), SIZE - (int)st.get(i).getY(),
                        (int)st.get(i - 1).getX(),SIZE - (int)st.get(i - 1).getY()); // отрисвока линий
            }
            if (end) {
                g.drawLine((int)st.get(st.size() - 1).getX(), SIZE -  (int)st.get(st.size() - 1).getY(),
                        (int)st.get(0).getX(),SIZE -  (int)st.get(0).getY()); // отрисовка последней линии

            }
            g.setColor(Color.RED);
            for (int i = 0; i < st.size(); ++ i) { // красные точки
                g.fillOval((int) st.get(i).getX() - 4, SIZE - (int) st.get(i).getY() - 4, 8, 8); // координаты не центра
            }

        }
    }
}

