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

    /** Все поверхности */
    private ArrayList<Face> faces;

    /** Дополнительные точки событий*/
    private ArrayList<Point> addEPS;

    /** Результирующие отрезки */
    ArrayList<IntersectingHalfEdges> intersectingHalfEdges;

    /** Состояния относительно выметающей прямой (Sweep Line Status) */
    TreeSet<HalfEdge> sLS;

    /** Первый отрезок сравнения */
    private Segment s1;

    /** Второй отрезок сравнения */
    private Segment s2;

    /** Первое полуребро в перелинковке */
    private HalfEdge he1;

    /** Второе полуребро в перелинковке */
    private HalfEdge he2;

    /** Координата выметающей линии */
    private double sweepingLine;

    /** Текущее ребро */
    private HalfEdge current;

    /** Пока ближайшее ребро */
    private HalfEdge temp;

    /** Следующее ребро */
    private HalfEdge nextOne;

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

    public void setSweepingLine(double sweepingLine) {
        this.sweepingLine = sweepingLine;
    }

    public void setIntersectingHalfEdges(ArrayList<IntersectingHalfEdges> intersectingHalfEdges) {
        this.intersectingHalfEdges = intersectingHalfEdges;
    }

    public void setSLS(TreeSet<HalfEdge> sLS) {
        this.sLS = sLS;
    }

    public void setS1(Segment s1) {
        this.s1 = s1;
    }

    public void setS2(Segment s2) {
        this.s2 = s2;
    }

    public void setHe1(HalfEdge he1) {
        this.he1 = he1;
    }

    public void setHe2(HalfEdge he2) {
        this.he2 = he2;
    }

    public void setCurrent(HalfEdge current) {
        this.current = current;
    }

    public void setTemp(HalfEdge temp) {
        this.temp = temp;
    }

    public void setNextOne(HalfEdge nextOne) {
        this.nextOne = nextOne;
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

    public void setTempEdge(HalfEdge tempEdge) {
        this.tempEdge = tempEdge;
    }

    public void setEdges(ArrayList<HalfEdge> edges) {
        this.edges = edges;
    }

    public void setFaces(ArrayList<Face> faces) {
        this.faces = faces;
    }

    /** Очищает отрисовку */
    public void setNULL() {
        first = null;
        tempEdge = null;
        sweepingLine = -1;
        intersectingHalfEdges = null;
        s1 = null;
        s2 = null;
        he1 = null;
        he2 = null;
        sLS = null;
        addEPS = null;
        current = null;
        temp = null;
        nextOne = null;
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

        /** Отрисовка всех поверхностей */
        if (faces != null) {
            for (Face face : faces) {
                if (! (face.getInnerComponents() == null && face.getOuterComponent() != null)) {
                    continue;
                }
                ArrayList<Integer> x = new ArrayList<Integer>();
                ArrayList<Integer> y = new ArrayList<Integer>();
                HalfEdge outer = face.getOuterComponent();
                HalfEdge first = outer;
                if (outer != null) {
                    do {

                        x.add((int) outer.getOrigin().getX());
                        y.add((int) outer.getOrigin().getY());

                        outer = outer.getNext();
                    } while (outer != first);

                    g.setColor(face.getColor());
                    g.fillPolygon(new Polygon(toArray(x, false), toArray(y, true), x.size()));
                }

                if (face.getInnerComponents() != null) {
                    for (HalfEdge innerEdge : face.getInnerComponents()) {
                        x = new ArrayList<Integer>();
                        y = new ArrayList<Integer>();
                        first = innerEdge;
                        do {

                            x.add((int) innerEdge.getOrigin().getX());
                            y.add((int) innerEdge.getOrigin().getY());

                            innerEdge = innerEdge.getNext();
                        } while (innerEdge != first);

                        g.setColor(innerEdge.getTwin().getIncidentFace().getColor());
                        g.fillPolygon(new Polygon(toArray(x, false), toArray(y, true), x.size()));

                    }
                }
            }
        }


        /** Отрисовка всех полигонов */
        if (edges != null) {
            for (HalfEdge edge : edges) {
                Random random = new Random();
                Color col = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100);
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
                    g.setColor(Color.BLUE);
                    g.fillOval(x.get(i) - 4, SIZE - y.get(i) - 4, 8, 8);
                    g.fillOval(x.get(i + 1) - 4, SIZE - y.get(i + 1) - 4, 8, 8);
                }
                g.setColor(Color.BLACK);
                g.drawLine(x.get(x.size() - 1), SIZE - y.get(x.size() - 1), x.get(0), SIZE - y.get(0));
                g.setColor(Color.BLUE);
                g.fillOval(x.get(x.size() - 1) - 4, SIZE - y.get(x.size() - 1) - 4, 8, 8);
                g.fillOval(x.get(0) - 4, SIZE - y.get(0) - 4, 8, 8);
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

        /** Отрисовка дополниительных точек событий */
        if (addEPS != null) {
            for (Point point : addEPS) {
                g.setColor(Color.BLUE);
                g.fillOval((int)point.getX() - 4, SIZE - (int)point.getY() - 4, 8, 8);
            }
        }


        /** Отрисовка результата */
        if (intersectingHalfEdges != null) {

            for (IntersectingHalfEdges iS : intersectingHalfEdges) {
                g.setColor(darkGreen);
                for (Segment segment : iS.getHalfEdges()) {
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

        /** Отрисовка первого полуребра */
        if (he1 != null) {
            g.setColor(Color.RED);
            g.drawLine((int) he1.getUp().getX(), SIZE - (int) he1.getUp().getY(),
                    (int) he1.getDown().getX(), SIZE - (int) he1.getDown().getY());
            int xC = ( (int)he1.getUp().getX() + (int)he1.getDown().getX() ) / 2;
            int yC = ( (int)he1.getUp().getY() + (int)he1.getDown().getY() ) / 2;
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine((int) he1.getOrigin().getX(), SIZE - (int) he1.getOrigin().getY(), xC, SIZE - yC);
            g.fillOval((int)he1.getUp().getX() - 4, SIZE - (int)he1.getUp().getY() - 4, 8, 8);
            g.fillOval((int)he1.getDown().getX() - 4, SIZE - (int)he1.getDown().getY() - 4, 8, 8);
            g2d.setStroke(new BasicStroke(1));
        }

        /** Отрисовка второго полуребра */
        if (he2 != null) {
            g.setColor(Color.RED);
            g.drawLine((int) he2.getUp().getX(), SIZE - (int) he2.getUp().getY(),
                    (int) he2.getDown().getX(), SIZE - (int) he2.getDown().getY());
            int xC = ( (int)he2.getUp().getX() + (int)he2.getDown().getX() ) / 2;
            int yC = ( (int)he2.getUp().getY() + (int)he2.getDown().getY() ) / 2;
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine((int) he2.getOrigin().getX(), SIZE - (int) he2.getOrigin().getY(), xC, SIZE - yC);
            g.fillOval((int)he2.getUp().getX() - 4, SIZE - (int)he2.getUp().getY() - 4, 8, 8);
            g.fillOval((int)he2.getDown().getX() - 4, SIZE - (int)he2.getDown().getY() - 4, 8, 8);
            g2d.setStroke(new BasicStroke(1));
        }


        /** Отрисовка текущего ребра */
        if (current != null) {
            g.setColor(Color.BLUE);
            g.drawLine((int)current.getUp().getX(), SIZE - (int)current.getUp().getY(),
                    (int)current.getDown().getX(), SIZE - (int)current.getDown().getY());
            g.fillOval((int)current.getUp().getX() - 4, SIZE - (int)current.getUp().getY() - 4, 8, 8);
            g.fillOval((int)current.getDown().getX() - 4, SIZE - (int)current.getDown().getY() - 4, 8, 8);
        }
        /** Отрисовка временного ближайшего ребра */
        if (temp != null) {
            g.setColor(Color.RED);
            g.drawLine((int)temp.getUp().getX(), SIZE - (int)temp.getUp().getY(),
                    (int)temp.getDown().getX(), SIZE - (int)temp.getDown().getY());
            g.fillOval((int)temp.getUp().getX() - 4, SIZE - (int)temp.getUp().getY() - 4, 8, 8);
            g.fillOval((int)temp.getDown().getX() - 4, SIZE - (int)temp.getDown().getY() - 4, 8, 8);
        }
        /** Отрисовка следующего ребра */
        if (nextOne != null) {
            g.setColor(Color.YELLOW);
            g.drawLine((int)nextOne.getUp().getX(), SIZE - (int)nextOne.getUp().getY(),
                    (int)nextOne.getDown().getX(), SIZE - (int)nextOne.getDown().getY());
            g.fillOval((int)nextOne.getUp().getX() - 4, SIZE - (int)nextOne.getUp().getY() - 4, 8, 8);
            g.fillOval((int)nextOne.getDown().getX() - 4, SIZE - (int)nextOne.getDown().getY() - 4, 8, 8);
        }

        painted = true;
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

    public ArrayList<HalfEdge> copyData(ArrayList<HalfEdge> list) {
        if (list == null) {
            return null;
        }
        ArrayList<HalfEdge> newList = new ArrayList<HalfEdge>();
        for (HalfEdge halfEdge : list) {
            HalfEdge edge = new HalfEdge(halfEdge.getOrigin(), halfEdge.getTwin().getOrigin());
            edge.setOrigin(halfEdge.getOrigin());
            edge.setTwin(new HalfEdge(halfEdge.getTwin().getOrigin(), halfEdge.getOrigin()));
            edge.getTwin().setTwin(edge);

            edge.getOrigin().setIncidentEdge(edge);
            edge.getTwin().getOrigin().setIncidentEdge(edge.getTwin());
            // Разворачиваем
            HalfEdge current = edge;
            HalfEdge next = halfEdge.getNext();
            while (next != halfEdge) {
                HalfEdge nextEdge = new HalfEdge(next.getOrigin(), next.getTwin().getOrigin());
                nextEdge.setOrigin(next.getOrigin());
                nextEdge.setTwin(new HalfEdge(next.getTwin().getOrigin(), next.getOrigin()));
                nextEdge.getTwin().setTwin(nextEdge);

                nextEdge.getOrigin().setIncidentEdge(nextEdge);
                nextEdge.getTwin().getOrigin().setIncidentEdge(nextEdge.getTwin());

                current.setNext(nextEdge);
                current.getTwin().setPrev(nextEdge.getTwin());
                nextEdge.setPrev(current);
                nextEdge.getTwin().setNext(current.getTwin());

                next = next.getNext();
                current = current.getNext();
            }

            current.setNext(edge);
            current.getTwin().setPrev(edge.getTwin());
            edge.setPrev(current);
            edge.getTwin().setNext(current.getTwin());

            newList.add(edge);

        }
        return newList;
    }
}

