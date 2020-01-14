/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Рисование области точек и выпуклой оболочки.
 */
public class DrawPanel extends JPanel {

    /** Размер области для рисования */
    private static final int SIZE = 500;

    DrawPanel() {
        setBackground(Color.LIGHT_GRAY);
    }

    /** Все точки */
    private ArrayList<Point> all;

    /** Минимальная пара */
    private Pair minPair;

    /** Пары из рекурсии */
    private ArrayList<Pair> pairs;



    /** Текущая пара */
    private Pair tempPair;

    /** Минимальная левая пара*/
    private Pair minLeftPair;

    /** Минимальная правая пара*/
    private Pair minRightPair;

    /** Координата разделительной линии */
    private double xLine;

    /** Координата левой границы */
    private double xLeftLine;

    /** Координата правой границы */
    private double xRightLine;

    /** Чем рисовать линии */
    private Color color1;

    /** Чем рисовать миниимальные точки */
    private Color color2;


    public static int getSIZE() {
        return SIZE;
    }

    public void setAllPoints(ArrayList<Point> all) {
        this.all = all;
    }

    public void setMinPair(Pair minPair) {
        this.minPair = minPair;
    }

    public void setMinLeftPair(Pair minLeftPair) {
        this.minLeftPair = minLeftPair;
    }

    public void setMinRightPair(Pair minRightPair) {
        this.minRightPair = minRightPair;
    }

    public void setTempPair(Pair tempPair) {
        this.tempPair = tempPair;
    }

    public void setPair(Pair pair) {
        if (pairs == null) {
            pairs = new ArrayList<Pair>();
        }
        pairs.add(pair);
    }

    public void deletePair(Pair pair) {
        pairs.remove(pair);
    }

    public void setXLine(double xLine) {
        this.xLine = xLine;
    }

    public void setXLeftLine(double xLeftLine) {
        this.xLeftLine = xLeftLine;
    }

    public void setXRightLine(double xRightLine) {
        this.xRightLine = xRightLine;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }



    /** Очищает отрисовку */
    public void setNULL() {
        minPair = null;
        tempPair = null;
        minLeftPair = null;
        minRightPair = null;
        xLine = -1;
        xLeftLine = -1;
        xRightLine = -1;
        pairs = null;
    }

    /**
     * Рисует точки и линии в зависимости от состояния алгоритма.
     * Координаты центра точек смещены.
     * СК перевернута.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);



        /** Отрисовка всех точек множества */
        g.setColor(Color.BLUE);
        if (all != null) {
            for (Point p : all) {
                g.fillOval((int)p.getX() - 4, SIZE - (int)p.getY() - 4, 8, 8);
            }
        }

        /** Отрисовка разделительной линии */
        if (xLine != -1) {
            g.setColor(color1);
            g.fillRect((int)xLine - 1, 0, 2, SIZE);
        }

        /** Отрисовка левой границы */
        if (xLeftLine != -1) {
            g.setColor(color1);
            g.drawLine((int)xLeftLine, 0, (int)xLeftLine, SIZE);
        }

        /** Отрисовка левой границы */
        if (xRightLine != -1) {
            g.setColor(color1);
            g.drawLine((int) xRightLine, 0, (int) xRightLine, SIZE);
        }

        /** Отрисовка точек из рекурсии и линии между ними */
        if (pairs != null) {
            for (Pair p : pairs) {
                g.setColor(Color.BLACK);
                g.drawLine((int)p.getP1().getX(), SIZE - (int)p.getP1().getY(),
                        (int)p.getP2().getX(), SIZE - (int)p.getP2().getY());
                g.setColor(Color.RED);
                g.fillOval((int)p.getP1().getX() - 4, SIZE - (int)p.getP1().getY() - 4, 8, 8);
                g.fillOval((int)p.getP2().getX() - 4, SIZE - (int)p.getP2().getY() - 4, 8, 8);
            }
        }


        /** Отрисовка пары временных точек и линии между ними */
        if (tempPair != null) {
            g.setColor(Color.BLACK);
            g.drawLine((int)tempPair.getP1().getX(), SIZE - (int)tempPair.getP1().getY(),
                    (int)tempPair.getP2().getX(), SIZE - (int)tempPair.getP2().getY());
            g.setColor(color2);
            g.fillOval((int)tempPair.getP1().getX() - 4, SIZE - (int)tempPair.getP1().getY() - 4, 8, 8);
            g.fillOval((int)tempPair.getP2().getX() - 4, SIZE - (int)tempPair.getP2().getY() - 4, 8, 8);
        }

        /** Отрисовка пары временных минимальных точек слева и линии между ними */
        if (minLeftPair != null) {
            g.setColor(Color.BLACK);
            g.drawLine((int)minLeftPair.getP1().getX(), SIZE - (int)minLeftPair.getP1().getY(),
                    (int)minLeftPair.getP2().getX(), SIZE - (int)minLeftPair.getP2().getY());
            g.setColor(Color.ORANGE);
            g.fillOval((int)minLeftPair.getP1().getX() - 4, SIZE - (int)minLeftPair.getP1().getY() - 4, 8, 8);
            g.fillOval((int)minLeftPair.getP2().getX() - 4, SIZE - (int)minLeftPair.getP2().getY() - 4, 8, 8);
        }

        /** Отрисовка пары временных минимальных точек справа и линии между ними */
        if (minRightPair != null) {
            g.setColor(Color.BLACK);
            g.drawLine((int)minRightPair.getP1().getX(), SIZE - (int)minRightPair.getP1().getY(),
                    (int)minRightPair.getP2().getX(), SIZE - (int)minRightPair.getP2().getY());
            g.setColor(Color.DARK_GRAY);
            g.fillOval((int)minRightPair.getP1().getX() - 4, SIZE - (int)minRightPair.getP1().getY() - 4, 8, 8);
            g.fillOval((int)minRightPair.getP2().getX() - 4, SIZE - (int)minRightPair.getP2().getY() - 4, 8, 8);
        }

        /** Отрисовка минимальных точек и линии между ними */
        if (minPair != null) {
            g.setColor(Color.BLACK);
            g.drawLine((int)minPair.getP1().getX(), SIZE - (int)minPair.getP1().getY(),
                    (int)minPair.getP2().getX(), SIZE - (int)minPair.getP2().getY());
            g.setColor(Color.RED);
            g.fillOval((int)minPair.getP1().getX() - 4, SIZE - (int)minPair.getP1().getY() - 4, 8, 8);
            g.fillOval((int)minPair.getP2().getX() - 4, SIZE - (int)minPair.getP2().getY() - 4, 8, 8);
        }

    }
}

