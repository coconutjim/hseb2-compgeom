/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Рисование области точек и выпуклой оболочки.
 */
public class DrawPanel extends JPanel {

    /** Размер области для рисования */
    private static final int SIZE = 500;

    /** Количество рядов сетки */
    private static final int gridRowCount = 10;

    /** Сетка */
    private boolean grid;

    DrawPanel() {
        setBackground(Color.LIGHT_GRAY);
    }

    /** Первая точка отрезка */
    private Point first;

    /** Первый отрезок */
    private Segment s1;

    /** Второй отрезок */
    private Segment s2;

    /** Все отрезки */
    private ArrayList<Segment> allSegments;

    /** Результирующие отрезки */
    AlgResult algResult;

    /** Состояния относительно выметающей прямой (Sweep Line Status) */
    TreeSet<Segment> sLS;


    /** Координата выметающей линии */
    private double sweepingLine;

    final private Color darkGreen = new Color(0, 120, 0);

    public static int getSIZE() {
        return SIZE;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }

    public static int getGridRowCount() {
        return  gridRowCount;
    }

    public void setFirst(Point first) {
        this.first = first;
    }

    public void setAllSegMents(ArrayList<Segment> allSegments) {
        this.allSegments = allSegments;
    }

    public void setSweepingLine(double sweepingLine) {
        this.sweepingLine = sweepingLine;
    }

    public void setS1(Segment s1) {
        this.s1 = s1;
    }

    public void setS2(Segment s2) {
        this.s2 = s2;
    }

    public void setAlgResult(AlgResult algResult) {
        this.algResult = algResult;
    }

    public void setSLS(TreeSet<Segment> sLS) {
        this.sLS = sLS;
    }

    /** Очищает отрисовку */
    public void setNULL() {
        first = null;
        sweepingLine = -1;
        s1 = null;
        s2 = null;
        algResult = null;
        sLS = null;
    }

    /**
     * Рисует точки и линии в зависимости от состояния алгоритма.
     * Координаты центра точек смещены.
     * СК перевернута.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /** Отрисовка сетки */
        if (grid) {
            g.setColor(Color.WHITE);
            for (int i = 0; i <= SIZE; i += SIZE / gridRowCount) {
                g.drawLine(0, i, SIZE, i);
                g.drawLine(i, 0, i, SIZE);
            }
        }

        /** Отрисовка первой точки */
        if (first != null) {
            g.setColor(Color.RED);
            g.fillOval((int)first.getX() - 4, SIZE - (int)first.getY() - 4, 8, 8);
        }


        /** Отрисовка всех отрезков */
        if (allSegments != null) {
            for (Segment seg : allSegments) {
                g.setColor(Color.BLACK);
                g.drawLine((int)seg.getLeft().getX(), SIZE - (int)seg.getLeft().getY(),
                        (int)seg.getRight().getX(), SIZE - (int)seg.getRight().getY());
                g.setColor(Color.BLUE);
                g.fillOval((int)seg.getLeft().getX() - 4, SIZE - (int)seg.getLeft().getY() - 4, 8, 8);
                g.fillOval((int)seg.getRight().getX() - 4, SIZE - (int)seg.getRight().getY() - 4, 8, 8);
            }
        }

        /** Отрисовка выметающей прямой */
        if (sweepingLine != -1) {
            g.setColor(Color.BLACK);
            g.fillRect((int)sweepingLine - 1, 0, 2, SIZE);
        }

        /** Отрисовка результата */
        if (algResult != null) {
            g.setColor(darkGreen);

            g.drawLine((int)algResult.getSegment1().getLeft().getX(),
                    SIZE - (int)algResult.getSegment1().getLeft().getY(),
                    (int)algResult.getSegment1().getRight().getX(),
                    SIZE - (int)algResult.getSegment1().getRight().getY());
            g.fillOval((int)algResult.getSegment1().getLeft().getX() - 4,
                    SIZE - (int)algResult.getSegment1().getLeft().getY() - 4, 8, 8);
            g.fillOval((int)algResult.getSegment1().getRight().getX() - 4,
                    SIZE - (int)algResult.getSegment1().getRight().getY() - 4, 8, 8);

            g.drawLine((int)algResult.getSegment2().getLeft().getX(),
                    SIZE - (int)algResult.getSegment2().getLeft().getY(),
                    (int)algResult.getSegment2().getRight().getX(),
                    SIZE - (int)algResult.getSegment2().getRight().getY());
            g.fillOval((int)algResult.getSegment2().getLeft().getX() - 4,
                    SIZE - (int)algResult.getSegment2().getLeft().getY() - 4, 8, 8);
            g.fillOval((int)algResult.getSegment2().getRight().getX() - 4,
                    SIZE - (int)algResult.getSegment2().getRight().getY() - 4, 8, 8);
        }

        /** Отрисовка текущих состояний */
        if (sLS != null) {
            for (Segment segment : sLS) {
                g.setColor(segment.getColor());
                g.drawLine((int)segment.getLeft().getX(), SIZE - (int)segment.getLeft().getY(),
                        (int)segment.getRight().getX(), SIZE - (int)segment.getRight().getY());
                g.fillOval((int)segment.getLeft().getX() - 4, SIZE - (int)segment.getLeft().getY() - 4, 8, 8);
                g.fillOval((int)segment.getRight().getX() - 4, SIZE - (int)segment.getRight().getY() - 4, 8, 8);
            }
        }


        /** Отрисовка первого отрезка */
        if (s1 != null) {
            g.setColor(Color.RED);
            g.drawLine((int)s1.getLeft().getX(), SIZE - (int)s1.getLeft().getY(),
                    (int)s1.getRight().getX(), SIZE - (int)s1.getRight().getY());
            g.fillOval((int)s1.getLeft().getX() - 4, SIZE - (int)s1.getLeft().getY() - 4, 8, 8);
            g.fillOval((int)s1.getRight().getX() - 4, SIZE - (int)s1.getRight().getY() - 4, 8, 8);
        }

        /** Отрисовка второго отрезка */
        if (s2 != null) {
            g.setColor(Color.RED);
            g.drawLine((int)s2.getLeft().getX(), SIZE - (int)s2.getLeft().getY(),
                    (int)s2.getRight().getX(), SIZE - (int)s2.getRight().getY());
            g.fillOval((int)s2.getLeft().getX() - 4, SIZE - (int)s2.getLeft().getY() - 4, 8, 8);
            g.fillOval((int)s2.getRight().getX() - 4, SIZE - (int)s2.getRight().getY() - 4, 8, 8);
        }

    }
}

