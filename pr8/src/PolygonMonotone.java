import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Lev on 22.02.14.
 */
public class PolygonMonotone {

    /** Общая панель отрисовки */
    final private DrawPanel drawPanel;

    /** Строка состояния */
    final private JLabel labelProcess;

    /** Задержка отображения */
    final private int delay;

    /** Показывать ли визуализацию монотонности */
    final private boolean monotone;

    public PolygonMonotone(DrawPanel drawPanel,
                      JLabel labelProcess, int delay, boolean monotone) {
        this.drawPanel = drawPanel;
        this.labelProcess = labelProcess;
        this.delay = delay;
        this.monotone = monotone;
    }

    /** Алгоритм */
    public ArrayList<HalfEdge> makeMonotone(HalfEdge edge) throws  InterruptedException {


        labelProcess.setText("Монотонизация");
        Thread.sleep(delay);

        /** Диагонали */
        ArrayList<HalfEdge> diagonals = new ArrayList<HalfEdge>();
        drawPanel.setDiagonals(diagonals);

        /** Определение вертексов */
        ArrayList<Vertex> eps = new ArrayList<Vertex>();
        drawPanel.setVertexes(eps);

        eps.add(edge.getOrigin());
        edge.getOrigin().setType(-1);

        HalfEdge next = edge.getNext();
        while (next != edge) {
            eps.add(next.getOrigin());
            next.getOrigin().setType(-1);

            next = next.getNext();
        }
        defineVertexes(eps);


        Collections.sort(eps);

        /** Состояния относительно выметающей прямой (Sweep Line Status) */
        HalfEdgeComparator HalfEdgeComparator = new HalfEdgeComparator(0);
        TreeSet<HalfEdge> sls = new TreeSet<HalfEdge>(HalfEdgeComparator);


        /** Обработка всех точек событий */
        for (Vertex q : eps) {

            /** Задаем выметающую прямую */
            HalfEdgeComparator.setY(q.getY());
            HalfEdgeComparator.setX(q.getX());

            drawPanel.setSweepingLine(q.getY());

            switch (q.getType()) {
                case Vertex.VERTEX_START :
                    handleStartVertex(q, sls);
                    break;
                case Vertex.VERTEX_MERGE :
                    handleMergeVertex(q, sls, diagonals);
                    break;
                case Vertex.VERTEX_SPLIT :
                    handleSplitVertex(q, sls, diagonals);
                    break;
                case Vertex.VERTEX_END :
                    break;
                case Vertex.VERTEX_REGULAR :
                    handleRegularVertex(q, sls, diagonals);
                    break;
            }

        }

        return HalfEdge.split(edge, diagonals, drawPanel);
    }

    /** Обработка вертекса начала */
    private void handleStartVertex(Vertex vertex, TreeSet<HalfEdge> sls) throws InterruptedException {

        HalfEdge halfEdge = vertex.getNext();
        sls.add(halfEdge);

        if (monotone) {
            labelProcess.setText("Вертекс старта");
            repaint(drawPanel);
            Thread.sleep(delay);
        }

        halfEdge.setHelper(vertex);

        if (monotone) {
            labelProcess.setText("Ставим helper");
            drawPanel.setS1(halfEdge);
            drawPanel.setHelper(vertex);
            repaint(drawPanel);
            Thread.sleep(delay);
            drawPanel.setS1(null);
            drawPanel.setHelper(null);
        }

    }

    /** Обработка вертекса разбиения */
    private void handleSplitVertex(Vertex vertex, TreeSet<HalfEdge> sls, ArrayList<HalfEdge> diagonals)
            throws InterruptedException {

        if (monotone) {
            labelProcess.setText("Вертекс разбиения");
            repaint(drawPanel);
            Thread.sleep(delay);
        }

        HalfEdge left = sls.lower(new HalfEdge(vertex, vertex, null, null));

        if (left != null) {

            Vertex helper = left.getHelper();


            /** Диагональ */
            diagonals.add(new HalfEdge(vertex, helper, null, null));

            if (monotone) {
                labelProcess.setText("Вставляем диагональ");
                repaint(drawPanel);
                Thread.sleep(delay);
            }



            left.setHelper(vertex);


            if (monotone) {
                labelProcess.setText("Ставим helper левому");
                drawPanel.setS1(left);
                drawPanel.setHelper(vertex);
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setS1(null);
                drawPanel.setHelper(null);
            }
        }


        HalfEdge halfEdge = vertex.getNext();
        sls.add(halfEdge);
        halfEdge.setHelper(vertex);

        if (monotone) {
            labelProcess.setText("Ставим helper");
            drawPanel.setS1(halfEdge);
            drawPanel.setHelper(vertex);
            repaint(drawPanel);
            Thread.sleep(delay);
            drawPanel.setS1(null);
            drawPanel.setHelper(null);
        }
    }

    /** Обработка вертекса слияния */
    private void handleMergeVertex(Vertex vertex, TreeSet<HalfEdge> sls, ArrayList<HalfEdge> diagonals)
            throws InterruptedException {

        if (monotone) {
            labelProcess.setText("Вертекс слияния");
            repaint(drawPanel);
            Thread.sleep(delay);
        }


        if (monotone) {
            labelProcess.setText("Проверка helper");
            drawPanel.setS1(vertex.getPrev());
            drawPanel.setHelper(vertex.getPrev().getHelper());
            repaint(drawPanel);
            Thread.sleep(delay);
            drawPanel.setS1(null);
            drawPanel.setHelper(null);
        }


        if (vertex.getPrev().getHelper().getType() == Vertex.VERTEX_MERGE) {
            /** Диагональ */
            diagonals.add(new HalfEdge(vertex, vertex.getPrev().getHelper(), null, null));

            if (monotone) {
                labelProcess.setText("Вставляем диагональ");
                repaint(drawPanel);
                Thread.sleep(delay);
            }

        }
        sls.remove(vertex.getPrev());

        HalfEdge left = sls.lower(new HalfEdge(vertex, vertex, null, null));

        if (monotone) {
            labelProcess.setText("Проверка helper левого");
            drawPanel.setS1(left);
            drawPanel.setHelper(left.getHelper());
            repaint(drawPanel);
            Thread.sleep(delay);
            drawPanel.setS1(null);
            drawPanel.setHelper(null);
        }


        if (left != null) {

            if (left.getHelper().getType() == Vertex.VERTEX_MERGE) {
                /** Диагональ */
                diagonals.add(new HalfEdge(vertex, left.getHelper(), null, null));

                if (monotone) {
                    labelProcess.setText("Вставляем диагональ");
                    repaint(drawPanel);
                    Thread.sleep(delay);
                }
            }

            left.setHelper(vertex);

            if (monotone) {
                labelProcess.setText("Ставим helper");
                drawPanel.setS1(left);
                drawPanel.setHelper(vertex);
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setS1(null);
                drawPanel.setHelper(null);
            }
        }

    }

    /** Обработка обычного вертекса */
    private void handleRegularVertex(Vertex vertex, TreeSet<HalfEdge> sls, ArrayList<HalfEdge> diagonals)
            throws InterruptedException {

        if (monotone) {
            labelProcess.setText("Обычный вертекс");
            repaint(drawPanel);
            Thread.sleep(delay);
        }


        if (vertex.getY() > vertex.getNext().getEnd().getY()) {

            if (monotone) {
                labelProcess.setText("Проверка helper");
                drawPanel.setS1(vertex.getPrev());
                drawPanel.setHelper(vertex.getPrev().getHelper());
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setS1(null);
                drawPanel.setHelper(null);
            }

            if (vertex.getPrev().getHelper() != null && vertex.getPrev().getHelper().getType() ==
                    Vertex.VERTEX_MERGE) {
                /** Диагональ */
                diagonals.add(new HalfEdge(vertex, vertex.getPrev().getHelper(), null, null));

                if (monotone) {
                    labelProcess.setText("Вставляем диагональ");
                    repaint(drawPanel);
                    Thread.sleep(delay);
                }

            }
            sls.remove(vertex.getPrev());
            sls.add(vertex.getNext());
            vertex.getNext().setHelper(vertex);

            if (monotone) {
                labelProcess.setText("Ставим helper");
                drawPanel.setS1(vertex.getNext());
                drawPanel.setHelper(vertex);
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setS1(null);
                drawPanel.setHelper(null);
            }
        }
        else {
            HalfEdge left = sls.lower(new HalfEdge(vertex, vertex, null, null));

            if (monotone) {
                labelProcess.setText("Проверка helper левого");
                drawPanel.setS1(left);
                drawPanel.setHelper(left.getHelper());
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setS1(null);
                drawPanel.setHelper(null);
            }

            if (left != null) {
                if (left.getHelper().getType() == Vertex.VERTEX_MERGE) {
                    /** Диагональ */
                    diagonals.add(new HalfEdge(vertex, left.getHelper(), null, null));

                    if (monotone) {
                        labelProcess.setText("Вставляем диагональ");
                        repaint(drawPanel);
                        Thread.sleep(delay);
                    }
                }
                left.setHelper(vertex);

                if (monotone) {
                    labelProcess.setText("Ставим helper");
                    drawPanel.setS1(left);
                    drawPanel.setHelper(vertex);
                    repaint(drawPanel);
                    Thread.sleep(delay);
                    drawPanel.setS1(null);
                    drawPanel.setHelper(null);
                }
            }
        }
    }


    /** Определение типов вертексов */
    private void defineVertexes(ArrayList<Vertex> vertexes) throws InterruptedException {
        labelProcess.setText("Определение вертексов");
        for (Vertex vertex : vertexes) {
            Vertex n1 = vertex.getNext().getEnd();
            Vertex n2 = vertex.getPrev().getOrigin();
            Vector v1 = new Vector(vertex, n1);
            Vector v2 = new Vector(vertex, n2);


            if (monotone) {
                drawPanel.setS1(v1);
                drawPanel.setS2(v2);
                repaint(drawPanel);
                Thread.sleep(delay);
            }

            /** Если оба ниже */
            if (n1.getY() < vertex.getY() && n2.getY() < vertex.getY()) {
                /** Угол меньше пи (учитывая то, что все ребра идут против часовой */
                if (v2.composite(v1) < 0) {
                    vertex.setType(Vertex.VERTEX_START);
                }
                else {
                    vertex.setType(Vertex.VERTEX_SPLIT);
                }
            }
            else {
                /** Если оба выше */
                if (n1.getY() > vertex.getY() && n2.getY() > vertex.getY()) {
                    /** Угол меньше пи (учитывая то, что все ребра идут против часовой */
                    if (v2.composite(v1) < 0) {
                        vertex.setType(Vertex.VERTEX_END);
                    }
                    else {
                        vertex.setType(Vertex.VERTEX_MERGE);
                    }
                }
                else {
                    vertex.setType(Vertex.VERTEX_REGULAR);
                }
            }

            if (monotone) {
                drawPanel.setS1(null);
                drawPanel.setS2(null);
                repaint(drawPanel);
                Thread.sleep(delay);
            }
        }
    }

    public void repaint(DrawPanel panel) {
        panel.setPainted(false);
        panel.repaint();
        while (! panel.isPainted()) { }
    }
}

/** Компаратор для отрезков с учетом выметающей прямой (и на основе векторного произведения) */
class HalfEdgeComparator implements Comparator<HalfEdge> {

    /** Координата выметающей прямой */
    private double y;

    /** Какую точку рассматриваем */
    private double x;

    public HalfEdgeComparator(double y) {
        this.y = y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public int compare(HalfEdge o1, HalfEdge o2) {

        if (o1 == o2) {
            return 0;
        }

        double x1;
        double x2;

        try {
            x1 = Math.round( ( o1.calculateX(y) * 100) / 100.);
        }
        catch (IllegalStateException e) {
            /** Если первый отрезок горизонтальный, проверяем, проходит ли он через точку второго */
            try {
                x2 = Math.round( ( o2.calculateX(y) * 100) / 100.);
            }
            catch (IllegalStateException e1) {
                /** Если оба горизонтальные, по центрам */
                if (o1.getUp().getX() + o1.getDown().getX() / 2 < o2.getUp().getX() + o2.getDown().getX() / 2) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            if (o1.getUp().getX() <= x2 && x2 <= o1.getDown().getX()) {
                x1 = x2;
                /** Проверка, какая точка рассматривается (от этого зависит порядок отрезка)*/
                if (x2 > x) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            else {
                if (o1.getDown().getX() < x2) {
                    x1 = o1.getDown().getX();
                }
                else {
                    x1 = o1.getUp().getX();
                }
            }
        }

        try {
            x2 = Math.round( ( o2.calculateX(y) * 100) / 100.);
        }
        catch (IllegalStateException e) {
            /** Если второй отрезок горизонтальный, проверяем, проходит ли он через точку первого */
            try {
                x1 = Math.round( ( o1.calculateX(y) * 100) / 100.);
            }
            catch (IllegalStateException e1) {
                /** Если оба горизонтальные, по центрам */
                if (o1.getUp().getX() + o1.getDown().getX() / 2 < o2.getUp().getX() + o2.getDown().getX() / 2) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            if (o2.getUp().getX() <= x1 && x1 <= o2.getDown().getX()) {
                x2 = x1;
                /** Проверка, какая точка рассматривается (от этого зависит порядок отрезка)*/
                if (x1 > x) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            else {
                if (o2.getDown().getX() < x1) {
                    x2 = o2.getDown().getX();
                }
                else {
                    x2 = o2.getUp().getX();
                }
            }
        }


        if (x1 < x2 && Math.abs(x1 - x2) > 0.1) {
            return -1;
        }
        else {
            if (x1 > x2 && Math.abs(x1 - x2) > 0.1) {
                return 1;
            }
            else {
                /** Векторное произведение */
                double composition = new Vector(new Point(x1, y), o1.getDown()).composite(new Vector(new Point(x1, y),
                        o2.getDown()));
                if (composition > 0) {
                    return -1;
                }
                else {
                    if (composition < 0) {
                        return 1;
                    }
                    else {
                        /** Если второй в этой точке заканчивается */
                        if (! new Point(x1, y).equals(o1.getDown()) && new Point(x2, y).equals(o2.getDown())) {
                            return -1;
                        }
                        /** Если первый в этой точке заканчивается */
                        if (new Point(x1, y).equals(o1.getDown()) && ! new Point(x2, y).equals(o2.getDown())) {
                            return 1;
                        }
                        return 0;
                    }
                }
            }
        }
    }

}




