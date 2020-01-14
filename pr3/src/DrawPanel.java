/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

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
    private Stack<Point> st;

    /** Временная точка */
    private Point tempPoint;

    /** Временная минимальная точка (с минимальным полярным углом)*/
    private Point tempMinPoint;

    /** Рисовать ли последнюю линию */
    private boolean end = false;

    public void setPoints(Stack<Point> st) {
        this.st = st;
    }

    public void setAllPoints(ArrayList<Point> all) {
        this.all = all;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setTempPoint(Point tempPoint) {
        this.tempPoint = tempPoint;
    }

    public void setTempMinPoint(Point tempMinPoint) {
        this.tempMinPoint = tempMinPoint;
    }

    public static int getSIZE() {
        return SIZE;
    }

    /**
     * Рисует точки и линии в зависимости от состояния алгоритма.
     * Координаты центра точек смещены.
     * СК перевернута.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        /** Отрисовка линий - границ */
        /* g.setColor(Color.DARK_GRAY);
        g.drawLine(0, SIZE, SIZE, SIZE);
        g.drawLine(SIZE, 0, SIZE, SIZE); */

        /** Отрисовка всех точек множества */
        g.setColor(Color.BLUE);
        if (all != null) {
            for (Point p : all) {
                g.fillOval((int)p.getX() - 4, SIZE - (int)p.getY() - 4, 8, 8);
            }
        }

        /** Отрисовка линий между точками ch */
        g.setColor(Color.BLACK);
        if (st != null) {
            for (int i = 1; i < st.size(); ++ i) {
                g.drawLine((int)st.get(i).getX(), SIZE - (int)st.get(i).getY(),
                        (int)st.get(i - 1).getX(),SIZE - (int)st.get(i - 1).getY());
            }
            /** Отрисовка последней линии */
            if (end) {
                g.drawLine((int)st.peek().getX(), SIZE -  (int)st.peek().getY(),
                        (int)st.get(0).getX(), SIZE -  (int)st.get(0).getY());
                tempPoint = null;
                tempMinPoint = null;

            }
            /** Закрашивание точек ch */
            g.setColor(Color.RED);
            for (int i = 0; i < st.size(); ++ i) { // красные точки
                g.fillOval((int) st.get(i).getX() - 4, SIZE - (int) st.get(i).getY() - 4, 8, 8);
            }
            /** Отрисовка временной точки и линии к ней */
            if (tempPoint != null) {
                g.setColor(Color.BLACK);
                g.drawLine((int)st.peek().getX(), SIZE -  (int)st.peek().getY(),
                        (int)tempPoint.getX(),SIZE -  (int)tempPoint.getY());
                g.setColor(Color.YELLOW);
                g.fillOval((int) tempPoint.getX() - 4, SIZE - (int) tempPoint.getY() - 4, 8, 8);
            }
            /** Отрисовка временной минимальной точки и линии к ней */
            if (tempMinPoint != null) {
                g.setColor(Color.BLACK);
                g.drawLine((int)st.peek().getX(), SIZE -  (int)st.peek().getY(),
                        (int)tempMinPoint.getX(),SIZE -  (int)tempMinPoint.getY());
                g.setColor(Color.GREEN);
                g.fillOval((int) tempMinPoint.getX() - 4, SIZE - (int) tempMinPoint.getY() - 4, 8, 8);
            }
        }
    }
}

