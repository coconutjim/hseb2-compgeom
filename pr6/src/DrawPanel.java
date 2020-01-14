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

    /** Все отрезки */
    private ArrayList<Segment> allSegments;

    /** Дополнительные точки событий*/
    private ArrayList<Point> addEPS;

    /** Результирующие отрезки */
    ArrayList<IntersectingSegments> intersectingSegments;

    /** Состояния относительно выметающей прямой (Sweep Line Status) */
    TreeSet<Segment> sLS;

    /** Первый отрезок сравнения */
    private Segment s1;

    /** Второй отрезок сравнения */
    private Segment s2;

    /** Координата выметающей линии */
    private double sweepingLine;

    /** Ждать перерисовку */
    private volatile boolean painted;

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

    public void setIntersectingSegments(ArrayList<IntersectingSegments> intersectingSegments) {
        this.intersectingSegments = intersectingSegments;
    }

    public void setSLS(TreeSet<Segment> sLS) {
        this.sLS = sLS;
    }

    public void setS1(Segment s1) {
        this.s1 = s1;
    }

    public void setS2(Segment s2) {
        this.s2 = s2;
    }

    public void addEP(Point point) {
        if (addEPS == null) {
            addEPS = new ArrayList<Point>();
        }

        addEPS.add(point);
    }

    public boolean isPainted() {
        return painted;
    }

    public void setPainted(boolean painted) {
        this.painted = painted;
    }

    /** Очищает отрисовку */
    public void setNULL() {
        first = null;
        sweepingLine = -1;
        intersectingSegments = null;
        s1 = null;
        s2 = null;
        sLS = null;
        addEPS = null;
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
                g.drawLine((int)seg.getUp().getX(), SIZE - (int)seg.getUp().getY(),
                        (int)seg.getDown().getX(), SIZE - (int)seg.getDown().getY());
                g.setColor(Color.BLUE);
                g.fillOval((int)seg.getUp().getX() - 4, SIZE - (int)seg.getUp().getY() - 4, 8, 8);
                g.fillOval((int)seg.getDown().getX() - 4, SIZE - (int)seg.getDown().getY() - 4, 8, 8);
            }
        }

        /** Отрисовка выметающей прямой */
        if (sweepingLine != -1) {
            g.setColor(Color.BLACK);
            g.fillRect(0, SIZE - (int)sweepingLine - 1, SIZE, 2);
        }

        /** Отрисовка дополниительных точек событий */
        if (addEPS != null) {
            for (Point point : addEPS) {
                g.setColor(Color.BLUE);
                g.fillOval((int)point.getX() - 4, SIZE - (int)point.getY() - 4, 8, 8);
            }
        }


        /** Отрисовка результата */
        if (intersectingSegments != null) {

            for (IntersectingSegments iS : intersectingSegments) {
                g.setColor(darkGreen);
                for (Segment segment : iS.getSegments()) {
                    g.drawLine((int) segment.getUp().getX(), SIZE - (int) segment.getUp().getY(),
                            (int) segment.getDown().getX(), SIZE - (int) segment.getDown().getY());
                    g.fillOval((int)segment.getUp().getX() - 4, SIZE - (int)segment.getUp().getY() - 4, 8, 8);
                    g.fillOval((int)segment.getDown().getX() - 4, SIZE - (int)segment.getDown().getY() - 4, 8, 8);
                }

                g.setColor(Color.RED);
                g.fillOval((int)iS.getPoint().getX() - 5, SIZE - (int)iS.getPoint().getY() - 5, 10, 10);
            }
        }

        /** Отрисовка текущих состояний */
        if (sLS != null) {
            for (Segment segment : sLS) {
                g.setColor(segment.getColor());
                g.drawLine((int)segment.getUp().getX(), SIZE - (int)segment.getUp().getY(),
                        (int)segment.getDown().getX(), SIZE - (int)segment.getDown().getY());
                g.fillOval((int)segment.getUp().getX() - 4, SIZE - (int)segment.getUp().getY() - 4, 8, 8);
                g.fillOval((int)segment.getDown().getX() - 4, SIZE - (int)segment.getDown().getY() - 4, 8, 8);
            }
        }

        /** Отрисовка первого отрезка */
        if (s1 != null) {
            g.setColor(Color.RED);
            g.drawLine((int)s1.getUp().getX(), SIZE - (int)s1.getUp().getY(),
                    (int)s1.getDown().getX(), SIZE - (int)s1.getDown().getY());
            g.fillOval((int)s1.getUp().getX() - 4, SIZE - (int)s1.getUp().getY() - 4, 8, 8);
            g.fillOval((int)s1.getDown().getX() - 4, SIZE - (int)s1.getDown().getY() - 4, 8, 8);
        }

        /** Отрисовка второго отрезка */
        if (s2 != null) {
            g.setColor(Color.RED);
            g.drawLine((int)s2.getUp().getX(), SIZE - (int)s2.getUp().getY(),
                    (int)s2.getDown().getX(), SIZE - (int)s2.getDown().getY());
            g.fillOval((int)s2.getUp().getX() - 4, SIZE - (int)s2.getUp().getY() - 4, 8, 8);
            g.fillOval((int)s2.getDown().getX() - 4, SIZE - (int)s2.getDown().getY() - 4, 8, 8);
        }

        painted = true;
    }
}

