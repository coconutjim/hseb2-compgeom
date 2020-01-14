/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
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

    /** Временное ребро */
    private HalfEdge tempEdge;

    /** Все полигоны */
    private ArrayList<HalfEdge> edges;

    /** Все вертексы */
    private ArrayList<Vertex> vertexes;

    /** Диагонали (пока рисуем) */
    private ArrayList<HalfEdge> diagonals;

    /** Helper */
    private Vertex helper;

    /** Prev helper */
    private Vertex prevHelper;

    /** Первый отрезок сравнения */
    private Segment s1;

    /** Второй отрезок сравнения */
    private Segment s2;

    /** Координата выметающей линии */
    private double sweepingLine;

    /** Ждать перерисовку */
    private volatile boolean painted;


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

    public void setSweepingLine(double sweepingLine) {
        this.sweepingLine = sweepingLine;
    }

    public boolean isPainted() {
        return painted;
    }

    public void setPainted(boolean painted) {
        this.painted = painted;
    }

    public void setTempEdge(HalfEdge tempEdge) {
        this.tempEdge = tempEdge;
    }

    public void setEdges(ArrayList<HalfEdge> edges) {
        this.edges = edges;
    }

    public void addEdge(HalfEdge edge) {
        if (edges == null) {
            edges = new ArrayList<HalfEdge>();
        }
        edges.add(edge);
    }

    public void setVertexes(ArrayList<Vertex> vertexes) {
        this.vertexes = vertexes;
    }

    public void setS2(Segment s2) {
        this.s2 = s2;
    }

    public void setS1(Segment s1) {
        this.s1 = s1;
    }

    public void setPrevHelper(Vertex prevHelper) {
        this.prevHelper = prevHelper;
    }

    public void setHelper(Vertex helper) {
        this.helper = helper;
    }

    public void setDiagonals(ArrayList<HalfEdge> diagonals) {
        this.diagonals = diagonals;
    }

    public void addDiagonal(HalfEdge diagonal) {
        if (diagonals == null) {
            diagonals = new ArrayList<HalfEdge>();
        }
        diagonals.add(diagonal);
    }

    /** Очищает отрисовку */
    public void setNULL() {
        first = null;
        tempEdge = null;
        sweepingLine = -1;
        s1 = null;
        s2 = null;
        vertexes = null;
        helper = null;
        prevHelper = null;
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


        /** Отрисовка всех полигонов */
        if (edges != null) {
            for (HalfEdge edge : edges) {
                Random random = new Random();
                ArrayList<Integer> x = new ArrayList<Integer>();
                ArrayList<Integer> y = new ArrayList<Integer>();
                HalfEdge current = edge;
                HalfEdge first = current;
                do {

                    x.add((int) current.getOrigin().getX());
                    y.add((int) current.getOrigin().getY());

                    current = current.getNext();
                } while (current != first);

                for (int i = 0; i < x.size() - 1; ++ i) {
                    g.setColor(Color.BLACK);
                    g.drawLine(x.get(i), SIZE - y.get(i), x.get(i + 1), SIZE - y.get(i + 1));
                    if (vertexes == null) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x.get(i) - 4, SIZE - y.get(i) - 4, 8, 8);
                        g.fillOval(x.get(i + 1) - 4, SIZE - y.get(i + 1) - 4, 8, 8);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawLine(x.get(x.size() - 1), SIZE - y.get(x.size() - 1), x.get(0), SIZE - y.get(0));
                if (vertexes == null) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x.get(x.size() - 1) - 4, SIZE - y.get(x.size() - 1) - 4, 8, 8);
                    g.fillOval(x.get(0) - 4, SIZE - y.get(0) - 4, 8, 8);
                }
            }
        }


        /** Отрисовка первой точки */
        if (first != null) {
            g.setColor(Color.RED);
            g.fillOval((int)first.getX() - 4, SIZE - (int)first.getY() - 4, 8, 8);
        }

        /** Отрисовка временной линии */
        if (tempEdge != null) {
            while (tempEdge != null) {
                g.setColor(Color.DARK_GRAY);
                g.drawLine((int)tempEdge.getUp().getX(), SIZE - (int)tempEdge.getUp().getY(),
                        (int)tempEdge.getDown().getX(), SIZE - (int)tempEdge.getDown().getY());
                g.setColor(Color.BLUE);
                g.fillOval((int)tempEdge.getUp().getX() - 4, SIZE - (int)tempEdge.getUp().getY() - 4, 8, 8);
                g.fillOval((int)tempEdge.getDown().getX() - 4, SIZE - (int)tempEdge.getDown().getY() - 4, 8, 8);
                tempEdge = tempEdge.getPrev();
            }
        }

        /** Отрисовка выметающей прямой */
        if (sweepingLine != -1) {
            g.setColor(Color.BLACK);
            g.fillRect(0, SIZE - (int)sweepingLine - 1, SIZE, 2);
        }

        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(3));

        /** Отрисовка первого отрезка */
        if (s1 != null) {
            g.setColor(Color.RED);
            g.drawLine((int)s1.getUp().getX(), SIZE - (int)s1.getUp().getY(),
                    (int)s1.getDown().getX(), SIZE - (int)s1.getDown().getY());
            //g.fillOval((int)s1.getUp().getX() - 4, SIZE - (int)s1.getUp().getY() - 4, 8, 8);
            //g.fillOval((int)s1.getDown().getX() - 4, SIZE - (int)s1.getDown().getY() - 4, 8, 8);
        }

        /** Отрисовка второго отрезка */
        if (s2 != null) {
            g.setColor(Color.RED);
            g.drawLine((int)s2.getUp().getX(), SIZE - (int)s2.getUp().getY(),
                    (int)s2.getDown().getX(), SIZE - (int)s2.getDown().getY());
            //g.fillOval((int)s2.getUp().getX() - 4, SIZE - (int)s2.getUp().getY() - 4, 8, 8);
            //g.fillOval((int)s2.getDown().getX() - 4, SIZE - (int)s2.getDown().getY() - 4, 8, 8);
        }

        g2d.setStroke(new BasicStroke(1));

        /** Отрисовка диагоналей */
        if (diagonals != null) {
            for (Segment s : diagonals) {
                g.setColor(Color.RED);
                g.drawLine((int)s.getUp().getX(), SIZE - (int)s.getUp().getY(),
                        (int)s.getDown().getX(), SIZE - (int)s.getDown().getY());
            }
        }

        /** Отрисовка вертексов */
        if (vertexes != null) {
            for (Vertex vertex : vertexes) {
                g.setColor(Color.BLUE);
                switch (vertex.getType()) {
                    case Vertex.VERTEX_START :
                        g2d.setStroke(new BasicStroke(3));
                        g.drawRect((int) vertex.getX() - 4, SIZE - (int) vertex.getY() - 4, 8, 8);
                        g2d.setStroke(new BasicStroke(1));
                        break;
                    case Vertex.VERTEX_MERGE :
                        drawReversedTriangle(g, vertex);
                        break;
                    case Vertex.VERTEX_SPLIT :
                        drawTriangle(g, vertex);
                        break;
                    case Vertex.VERTEX_END :
                        g.fillRect((int) vertex.getX() - 5, SIZE - (int) vertex.getY() - 5, 10, 10);
                        break;
                    case Vertex.VERTEX_REGULAR :
                        g.setColor(Color.BLACK);
                        g.fillOval((int)vertex.getX() - 4, SIZE - (int)vertex.getY() - 4, 8, 8);
                        break;
                }
            }
        }

        /** Отрисовка helper */
        if (helper != null) {
            g.setColor(Color.RED);
            g.fillOval((int)helper.getX() - 10, SIZE - (int)helper.getY() - 10, 20, 20);
        }
        /** Отрисовка prev */
        if (prevHelper != null) {
            g.setColor(Color.BLUE);
            g.fillOval((int)prevHelper.getX() - 10, SIZE - (int)prevHelper.getY() - 10, 20, 20);
        }

        painted = true;
    }

    private void drawTriangle(Graphics g, Point center) {
        int[] x = { (int)center.getX() - 7, (int)center.getX(), (int)center.getX() + 7 };
        int[] y = { SIZE - (int)center.getY() + 7, SIZE - (int)center.getY() - 7, SIZE - (int)center.getY() + 7 };
        g.fillPolygon(x, y, x.length);
    }

    private void drawReversedTriangle(Graphics g, Point center) {
        int[] x = { (int)center.getX() - 7, (int)center.getX(), (int)center.getX() + 7 };
        int[] y = { SIZE - (int)center.getY() - 7, SIZE - (int)center.getY() + 7, SIZE - (int)center.getY() - 7 };
        g.fillPolygon(x, y, x.length);
    }


    private int[] toArray(ArrayList<Integer> list, boolean isY) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); ++ i) {
            array[i] = list.get(i);
            if (isY) {
                array[i] = SIZE - array[i];
            }
        }
        return array;
    }
}

