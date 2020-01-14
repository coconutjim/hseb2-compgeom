import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Lev on 22.02.14.
 */
public class MapOverlay {

    /** Общая панель отрисовки */
    private DrawPanel drawPanel;

    /** Панель отрисовки состояний */
    private LegendPanel legendPanel;

    /** Строка состояния */
    private JLabel labelProcess;

    /** Задержка отображения */
    private int delay;

    /** Показывать ли визуализацию нахождения пересечений */
    private boolean intersecting;

    /** Список всех ребер */
    private ArrayList<HalfEdge> allhe;

    /** Список точек пересечения */
    ArrayList<Point> intersections;

    public MapOverlay(DrawPanel drawPanel, LegendPanel legendPanel,
                      JLabel labelProcess, int delay, boolean intersecting) {
        this.drawPanel = drawPanel;
        this.legendPanel = legendPanel;
        this.labelProcess = labelProcess;
        this.delay = delay;
        this.intersecting = intersecting;
        intersections = new ArrayList<Point>();
        allhe = new ArrayList<HalfEdge>();
    }

    /** Алгоритм */
    public ArrayList<IntersectingHalfEdges> findOverlaying(ArrayList<HalfEdge> edges)
            throws  InterruptedException {



        for (HalfEdge halfEdge : edges) {
            halfEdge.setIncidentFace(null);
            halfEdge.getTwin().setIncidentFace(null);
            allhe.add(halfEdge);
            allhe.add(halfEdge.getTwin());
            HalfEdge current = halfEdge.getNext();
            while (current != halfEdge) {

                current.setIncidentFace(null);
                current.getTwin().setIncidentFace(null);
                allhe.add(current);
                allhe.add(current.getTwin());

                current = current.getNext();
            }
        }



        /** Результат */
        ArrayList<IntersectingHalfEdges> result = new ArrayList<IntersectingHalfEdges>();
        drawPanel.setIntersectingHalfEdges(result);

        /** Формирование списка точек событий (Event Point Shedule), заодно покраска*/
        TreeSet<EventPoint> ePS = new TreeSet<EventPoint>();
        for (HalfEdge edge : edges) {

            HalfEdge current = edge;
            HalfEdge first = current;
            do {
                ePS.add(new EventPoint(current.getUp(), current, true, false));
                ePS.add(new EventPoint(current.getDown(), current, false, false));
                Random random = new Random();
                current.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));

                current = current.getNext();
            } while (current != first);
        }

        /** Состояния относительно выметающей прямой (Sweep Line Status) */
        HalfEdgeComparator HalfEdgeComparator = new HalfEdgeComparator(0);
        TreeSet<HalfEdge> sLS = new TreeSet<HalfEdge>(HalfEdgeComparator);


        if (intersecting) {
            drawPanel.setSLS(sLS);
            legendPanel.setHalfEdges(sLS);
            repaint(legendPanel);
        }

        /** Обработка всех точек событий */
        EventPoint q;

        //System.out.println("___________________");
        while (! ePS.isEmpty()) {

            /** Очередная точка */
            q = ePS.first();

            /** Задаем выметающую прямую */
            HalfEdgeComparator.setY(q.getPoint().getY());


            if (intersecting) {
                labelProcess.setText("Следующая точка");
                drawPanel.setSweepingLine(q.getPoint().getY());
                repaint(drawPanel);
                Thread.sleep(delay);
            }

            /** Обработка */
            handleEventPoint(q, ePS, sLS, result);

            /** Удаление точки */
            //ePS.remove(q);
            ePS.pollFirst();
        }

        labelProcess.setText("Вычисление поверхностей");
        //drawPanel.setEdges(edges);
        repaint(drawPanel);
        Thread.sleep(delay);


        /** Создаем список всех полуребер
        ArrayList<HalfEdge> allhe = new ArrayList<HalfEdge>();


        for (HalfEdge halfEdge : edges) {
            ArrayList<HalfEdge> temp = new ArrayList<HalfEdge>();
            halfEdge.setIncidentFace(null);
            halfEdge.getTwin().setIncidentFace(null);
            temp.add(halfEdge);
            temp.add(halfEdge.getTwin());
            HalfEdge current = halfEdge.getNext();
            while (! temp.contains(current)) {

                current.setIncidentFace(null);
                current.getTwin().setIncidentFace(null);
                temp.add(current);
                temp.add(current.getTwin());

                current = current.getNext();
            }

            int index = temp.indexOf(current);
            for (int i = 0; i < index; ++ i) {
                temp.remove(0);
            }
            allhe.addAll(temp);
        }*/
        ArrayList<Face> faces = new ArrayList<Face>();
        for (HalfEdge halfEdge : allhe) {
            /*if (faces.size() == 3) {
                break;
            }*/
            Face tempFace = new Face();
            if (halfEdge.getIncidentFace() == null) {
                halfEdge.setIncidentFace(tempFace);
                HalfEdge tempHE = halfEdge.getNext();
                HalfEdge min = halfEdge;
                double maxy = halfEdge.getUp().getY();
                double miny = halfEdge.getDown().getY();
                //allhe.remove(halfEdge);
                while (!halfEdge.equals(tempHE)) {

                    tempHE.setIncidentFace(tempFace);
                    if (min.getOrigin().getX() > tempHE.getOrigin().getX()) {
                        min = tempHE;
                    }

                    //allhe.remove(tempHE);
                    tempHE = tempHE.getNext();
                }
                Random random = new Random();
                tempFace.setOuterComponent(min);
                /*if (new Vector(min.getPrev().getOrigin(),
                        min.getOrigin()).composite(new Vector(min.getOrigin(), min.getTwin().getOrigin())) < 0) {
                    tempFace.addInnerComponent(min);
                    Face inner = new Face();
                    inner.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100));
                    inner.setOuterComponent(min);
                    min.setIncidentFace(new Face());
                } else {
                    tempFace.setOuterComponent(min);
                }*/
                if (new Vector(min.getPrev().getOrigin(),
                        min.getOrigin()).composite(new Vector(min.getOrigin(), min.getTwin().getOrigin())) < 0) {
                    tempFace.addInnerComponent(min);
                    Face inner = new Face();
                    inner.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100));
                    min.setIncidentFace(tempFace);
                    min.getTwin().setIncidentFace(inner);
                } else {
                    tempFace.setOuterComponent(min);
                }

                tempFace.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100));

                faces.add(tempFace);
                labelProcess.setText("Новая поверхность");
                drawPanel.setFaces(faces);
                drawPanel.repaint();
                Thread.sleep(delay);
            }
        }

        drawPanel.setFaces(faces);
        drawPanel.repaint();

        return result;
    }

    /** Обработка точки события */
    private void handleEventPoint(EventPoint q, TreeSet<EventPoint> ePS ,TreeSet<HalfEdge> sLS,
                                  ArrayList<IntersectingHalfEdges> result)
            throws InterruptedException {

        EventPointComparator eventPointComparator = new EventPointComparator(q.getPoint().getX(), q.getPoint().getY());

        /** Формирование U(p) */
        ArrayList<EventPoint> U = new ArrayList<EventPoint>();
        for (EventPoint eventPoint : ePS) {
            if (eventPoint.getPoint().getY() < q.getPoint().getY()) {
                break;
            }
            if (eventPoint.isUp() && eventPoint.getPoint().equals(q.getPoint())) {
                U.add(eventPoint);
            }
        }

        /** Формирование L(p) */
        ArrayList<EventPoint> L = new ArrayList<EventPoint>();
        for (EventPoint eventPoint : ePS) {
            if (eventPoint.getPoint().getY() < q.getPoint().getY()) {
                break;
            }
            if (! eventPoint.isUp() && ! eventPoint.isOn() && eventPoint.getPoint().equals(q.getPoint())) {
                L.add(eventPoint);
            }
        }

        /** Формирование C(p) */
        ArrayList<EventPoint> C = new ArrayList<EventPoint>();
        for (EventPoint eventPoint : ePS) {
            if (eventPoint.getPoint().getY() < q.getPoint().getY()) {
                break;
            }
            if (eventPoint.isOn() && eventPoint.getPoint().equals(q.getPoint())) {
                boolean flag = false;
                for (EventPoint eventPoint1 : C) {
                    if (eventPoint.getHalfEdge() == eventPoint1.getHalfEdge()) {
                        flag = true;
                    }
                }
                if (flag) {
                    continue;
                }
                C.add(eventPoint);
            }
        }

        /** Сообщить о пересечении (нет U и L)*/
        if (C.size() > 1) {

            ArrayList<HalfEdge> HalfEdges = new ArrayList<HalfEdge>();
            for (EventPoint eventPoint : U) {
                HalfEdges.add(eventPoint.getHalfEdge());
            }
            for (EventPoint eventPoint : L) {
                HalfEdges.add(eventPoint.getHalfEdge());
            }
            for (EventPoint eventPoint : C) {
                HalfEdges.add(eventPoint.getHalfEdge());
            }
            result.add(new IntersectingHalfEdges(HalfEdges, q.getPoint()));

            if (intersecting) {
                repaint(legendPanel);
            }
            labelProcess.setText("Новое пересечение");
            repaint(drawPanel);
            Thread.sleep(delay);

            /** Новые полуребра. Сразу сортируются по часовой стрелке */
            //ArrayList<HalfEdge> news = new ArrayList<HalfEdge>();
            TreeSet<HalfEdge> news = new TreeSet<HalfEdge>(new Comparator<HalfEdge>() {
                @Override
                public int compare(HalfEdge o1, HalfEdge o2) {
                    double composition = new Vector(o1.getOrigin(),
                            o1.getTwin().getOrigin()).composite(new Vector(o2.getOrigin(), o2.getTwin().getOrigin()));
                    if (composition > 0) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });

            /** Для каждого полуребра в пересечении */
            for (EventPoint eventPoint : C) {
                HalfEdge edge = eventPoint.getHalfEdge();

                HalfEdge twin = edge.getTwin();
                HalfEdge prev = edge.getPrev();
                HalfEdge next = edge.getNext();

                /** Создание двух новых полуребер с началом в точке пересечения и их близнецов */
                HalfEdge new1 = new HalfEdge(new Vertex(q.getPoint()), edge.getOrigin(), null, prev.getTwin());
                prev.getTwin().setPrev(new1);
                new1.getTwin().setPrev(prev);
                prev.setNext(new1.getTwin());

                HalfEdge new2 = new HalfEdge(new Vertex(q.getPoint()), twin.getOrigin(), null, next);
                next.setPrev(new2);
                new2.getTwin().setPrev(next.getTwin());
                next.getTwin().setNext(new2.getTwin());

                allhe.remove(edge);
                allhe.remove(edge.getTwin());
                allhe.add(new1);
                allhe.add(new2);
                allhe.add(new1.getTwin());
                allhe.add(new2.getTwin());

                /** Укорачиваю отрезки
                for (EventPoint eventPoint1 : C) {
                    HalfEdge he = eventPoint1.getHalfEdge();
                    if (he.getUp().equals(he.getOrigin())) {
                        he.setOrigin(q.getPoint());
                    }
                    else {
                        he.getTwin().setOrigin(q.getPoint());
                    }
                    he.setUp(q.getPoint());
                }*/


                news.add(new1);
                news.add(new2);
            }

            /** Для каждого нового */
            HalfEdge prev = news.first();
            labelProcess.setText("Связка полуребер");
            for (HalfEdge halfEdge : news) {

                if (halfEdge == news.first()) {
                    continue;
                }
                /** Связываем текущее ребро и его соседа */

                prev.getTwin().setNext(halfEdge);
                halfEdge.setPrev(prev.getTwin());

                drawPanel.setHe1(prev);
                drawPanel.setHe2(halfEdge.getTwin());
                repaint(drawPanel);
                Thread.sleep(delay);

                prev = halfEdge;
            }

            HalfEdge first = news.first();
            prev.getTwin().setNext(first);
            first.setPrev(prev.getTwin());

            drawPanel.setHe1(prev);
            drawPanel.setHe2(first.getTwin());
            repaint(drawPanel);
            Thread.sleep(delay);

            drawPanel.setHe1(null);
            drawPanel.setHe2(null);





            /** Зачищаем ненужные события (одно надо оставить, потом удалится) */
            for (int i = 0; i < C.size() - 1; ++ i) {
                ePS.pollFirst();
            }

        }

        /** Работа с состояниями */

        /** Удаление L и C из состояний */
        for (EventPoint eventPoint : L) {
            try {
                if (eventPoint.getHalfEdge() == sLS.first()) {
                    sLS.pollFirst();
                    continue;
                }
            }
            catch (NoSuchElementException e) {
                //
            }
            sLS.remove(eventPoint.getHalfEdge());
        }
        for (EventPoint eventPoint : C) {
            try {
                if (eventPoint.getHalfEdge() == sLS.first()) {
                    sLS.pollFirst();
                    continue;
                }
            }
            catch (NoSuchElementException e) {
                //
            }
            sLS.remove(eventPoint.getHalfEdge());
        }

        /** Добавление U и C в состояния */
        for (EventPoint eventPoint : U) {
            sLS.add(eventPoint.getHalfEdge());
        }
        for (EventPoint eventPoint : C) {
            sLS.add(eventPoint.getHalfEdge());
        }

        if (intersecting) {
            labelProcess.setText("Удаление и добавление");
            repaint(drawPanel);
            repaint(legendPanel);
            Thread.sleep(delay);
        }

        /** Если только конец отрезка */
        if (U.size() + C.size() == 0) {

            /** Левый и правый соседи */
            if (sLS.higher(q.getHalfEdge()) != null && sLS.lower(q.getHalfEdge()) != null) {
                if (intersecting) {
                    labelProcess.setText("Левый и правый соседи");
                }
                findNewEvent(sLS.higher(q.getHalfEdge()), sLS.lower(q.getHalfEdge()), q, ePS, result);
            }
        }
        /** В другом случае */
        else {

            /** Объединение U и C */
            TreeSet<EventPoint> UC = new TreeSet<EventPoint>(eventPointComparator);
            UC.addAll(U);
            UC.addAll(C);

            /** Самый левый сегмент из U и C */
            EventPoint min = UC.first();

            /** Этот сегмент в sLS и его левый сосед */
            if (sLS.floor(min.getHalfEdge()) != null && sLS.lower(sLS.floor(min.getHalfEdge())) != null)  {
                if (intersecting) {
                    labelProcess.setText("Сосед слева");
                }
                findNewEvent(sLS.floor(min.getHalfEdge()), sLS.lower(sLS.floor(min.getHalfEdge())), q, ePS, result);
            }

            /** Самый правый сегмент из U и C */
            EventPoint max = UC.last();

            /** Этот сегмент в sLS и его правый сосед */
            if (sLS.ceiling(max.getHalfEdge()) != null && sLS.higher(sLS.ceiling(max.getHalfEdge())) != null) {
                if (intersecting) {
                    labelProcess.setText("Сосед справа");
                }
                findNewEvent(sLS.ceiling(max.getHalfEdge()), sLS.higher(sLS.ceiling(max.getHalfEdge())),
                        q, ePS, result);
            }


        }

    }

    /** Нахождение новых пересечений */
    private void findNewEvent(HalfEdge sl, HalfEdge sr, EventPoint q, TreeSet<EventPoint> ePS,
                              ArrayList<IntersectingHalfEdges> result)
            throws InterruptedException {


        if (intersecting) {
            drawPanel.setS1(sl);
            drawPanel.setS2(sr);
            repaint(drawPanel);
            Thread.sleep(delay);

            drawPanel.setS1(null);
            drawPanel.setS2(null);
        }


        /** Проверка на пересечение */
        if (HalfEdgesIntersect(sl, sr)) {

            Point point = findIntersectionPoint(sl, sr);

            /** Проверка на существования такого пересечения */
            for (Point point1 : intersections) {
                if (point1.equals(point)) {
                    return;
                }
            }


            /** Вставка событий  */
            ePS.add(new EventPoint(point, sr, false, true));
            ePS.add(new EventPoint(point, sl, false, true));
            intersections.add(point);

            if (intersecting) {
                drawPanel.addEP(point);
                labelProcess.setText("Добавление события");
                repaint(drawPanel);
                Thread.sleep(delay);
            }

        }

    }

    /**
     *  Находит точку пересечения (при условии, что отрезки пересекаеются)
     * @param s1 первый отрезок
     * @param s2 втоой отрезок
     * @return точку пересечения
     */
    private Point findIntersectionPoint(HalfEdge s1, HalfEdge s2) {

        Point x1 = s1.getUp();
        Point x2 = s1.getDown();
        Point x3 = s2.getUp();
        Point x4 = s2.getDown();

        /** Если первый вертикальный */
        if (x2.getX() == x1.getX() && x4.getX() != x3.getX()) {
            double a2 = ( x4.getY() - x3.getY() ) / ( x4.getX() - x3.getX() );
            double b2 = x3.getY() - a2 * x3.getX();

            return new Point(x1.getX(), a2 * x1.getX() + b2);
        }

        /** Если второй вертикальный */
        if (x4.getX() == x3.getX() && x2.getX() != x1.getX()) {
            double a1 = ( x2.getY() - x1.getY() ) / ( x2.getX() - x1.getX() );
            double b1 = x1.getY() - a1 * x1.getX();

            return new Point(x3.getX(), a1 * x3.getX() + b1);
        }

        /** Если первый горизонтальный */
        if (x2.getY() == x1.getY() && x4.getY() != x3.getY()) {
            double a2 = ( x4.getY() - x3.getY() ) / ( x4.getX() - x3.getX() );
            double b2 = x3.getY() - a2 * x3.getX();

            return new Point(( x1.getY() - b2 ) / a2, x1.getY());
        }

        /** Если второй горизонтальный */
        if (x4.getY() == x3.getY() && x2.getY() != x1.getY()) {
            double a1 = ( x2.getY() - x1.getY() ) / ( x2.getX() - x1.getX() );
            double b1 = x1.getY() - a1 * x1.getX();

            return new Point(( x3.getY() - b1 ) / a1, x3.getY());
        }



        double a1 = ( x2.getY() - x1.getY() ) / ( x2.getX() - x1.getX() );
        double b1 = x1.getY() - a1 * x1.getX();

        double a2 = ( x4.getY() - x3.getY() ) / ( x4.getX() - x3.getX() );
        double b2 = x3.getY() - a2 * x3.getX();

        double x  = (b2 - b1) / (a1 - a2);
        double y = a1 * x + b1;

        return new Point(x, y);

    }

    /** Определяет, как лежат относительно друг друга вектора на основе векторного произведения */
    private double direction(Point pi, Point pj, Point pk) {
        return (new Vector(pi, pk).composite(new Vector(pi, pj)));
    }

    /** Определяет, лежит ли точка на отрезке (при условии, что она коллинеарна ему) */
    private boolean onHalfEdge(Point pi, Point pj, Point pk) {
        /* Опа
        if ( Math.min(pi.getX(), pj.getX()) <= pk.getX() && Math.max(pi.getX(), pj.getX()) >= pk.getX() &&
                Math.min(pi.getY(), pj.getY()) <= pk.getY() && Math.max(pi.getY(), pj.getY()) >= pk.getY() ) {
            return true;
        }*/
        return false;
    }

    /** Определяет, пересекаются ли отрезки. */
    private boolean HalfEdgesIntersect(HalfEdge s1, HalfEdge s2) {
        Point p1 = s1.getUp();
        Point p2 = s1.getDown();
        Point p3 = s2.getUp();
        Point p4 = s2.getDown();
        double d1 = direction(p3, p4, p1);
        double d2 = direction(p3, p4, p2);
        double d3 = direction(p1, p2, p3);
        double d4 = direction(p1, p2, p4);
        if ( ( (d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0) ) && ( (d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0) ) ) {
            return true;
        }
        if (d1 == 0 && onHalfEdge(p3, p4, p1)) {
            return true;
        }
        if (d2 == 0 && onHalfEdge(p3, p4, p2)) {
            return true;
        }
        if (d3 == 0 && onHalfEdge(p1, p2, p3)) {
            return true;
        }
        if (d4 == 0 && onHalfEdge(p1, p2, p4)) {
            return true;
        }
        return false;
    }

    public void repaint(DrawPanel panel) {
        panel.setPainted(false);
        panel.repaint();
        while (! panel.isPainted()) { }
    }

    public void repaint(LegendPanel panel) {
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

/** Компаратор точек событий по отрезкам */
class EventPointComparator implements Comparator<EventPoint> {

    /** Координата выметающей прямой */
    private double y;

    /** Какую точку рассматриваем */
    private double x;

    public EventPointComparator(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public int compare(EventPoint o1, EventPoint o2) {
        HalfEdge s1 = o1.getHalfEdge();
        HalfEdge s2 = o2.getHalfEdge();

        if (s1 == s2) {
            return 0;
        }

        double x1;
        double x2;

        try {
            x1 = Math.round( ( s1.calculateX(y) * 100) / 100.);
        }
        catch (IllegalStateException e) {
            /** Если первый отрезок горизонтальный, проверяем, проходит ли он через точку второго */
            try {
                x2 = Math.round( ( s2.calculateX(y) * 100) / 100.);
            }
            catch (IllegalStateException e1) {
                /** Если оба горизонтальные, по центрам */
                if (s1.getUp().getX() + s1.getDown().getX() / 2 < s2.getUp().getX() + s2.getDown().getX() / 2) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            if (s1.getUp().getX() <= x2 && x2 <= s1.getDown().getX()) {
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
                if (s1.getDown().getX() < x2) {
                    x1 = s1.getDown().getX();
                }
                else {
                    x1 = s1.getUp().getX();
                }
            }
        }

        try {
            x2 = Math.round( ( s2.calculateX(y) * 100) / 100.);
        }
        catch (IllegalStateException e) {
            /** Если второй отрезок горизонтальный, проверяем, проходит ли он через точку первого */
            try {
                x1 = Math.round( ( s1.calculateX(y) * 100) / 100.);
            }
            catch (IllegalStateException e1) {
                /** Если оба горизонтальные, по центрам */
                if (s1.getUp().getX() + s1.getDown().getX() / 2 < s2.getUp().getX() + s2.getDown().getX() / 2) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            if (s2.getUp().getX() <= x1 && x1 <= s2.getDown().getX()) {
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
                if (s2.getDown().getX() < x1) {
                    x2 = s2.getDown().getX();
                }
                else {
                    x2 = s2.getUp().getX();
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
                double composition = new Vector(new Point(x1, y), s1.getDown()).composite(new Vector(new Point(x2, y),
                        s2.getDown()));
                if (composition > 0) {
                    return -1;
                }
                else {
                    if (composition < 0) {
                        return 1;
                    }
                    else {
                        /** Если второй в этой точке заканчивается */
                        if (! new Point(x1, y).equals(s1.getDown()) && new Point(x2, y).equals(s2.getDown())) {
                            return -1;
                        }
                        /** Если первый в этой точке заканчивается */
                        if (new Point(x1, y).equals(s1.getDown()) && ! new Point(x2, y).equals(s2.getDown())) {
                            return 1;
                        }

                        return 0;
                    }
                }
            }
        }
    }
}

/** Хранилище для точек событий */
class EventPoint implements Comparable<EventPoint> {
    private Point point;
    private HalfEdge HalfEdge;
    private boolean up;
    private boolean on;
    public EventPoint(Point point, HalfEdge HalfEdge, boolean up, boolean on) {
        this.point = point;
        this.HalfEdge = HalfEdge;
        this.up = up;
        this.on = on;
    }

    public Point getPoint() {
        return  point;
    }
    public HalfEdge getHalfEdge() {
        return HalfEdge;
    }
    public boolean isUp() {
        return up;
    }
    public boolean isOn() {
        return on;
    }


    /** Сравнение (не возвращает 0) */
    @Override
    public int compareTo(EventPoint op) {

        if (this == op) {
            return 0;
        }

        if (getPoint().getY() < op.getPoint().getY()) {
            return 1;
        } else if (getPoint().getY() > op.getPoint().getY()) {
            return -1;
            /** Если когоризонтальны */
        } else if (isOn() && op.isUp() || ! isUp() && ! isOn() && op.isUp() || ! isUp() && ! isOn() && op.isUp()) {
            return 1;
        } else if (isUp() && op.isOn() || isUp() && ! op.isUp() && ! op.isOn() || isOn() && ! op.isUp() &&
                ! op.isOn()) {
            return -1;
            /** Если обе верхние (нижние, на отрезке) */
        } else if (getPoint().getX() > op.getPoint().getX()) {
            return 1;
        } else if (getPoint().getX() < op.getPoint().getX()) {
            return -1;
        } else {
            return 1;
        }
    }

}


/**
 * Моделирует вектор.
 */
class Vector {
    /**
     * Координаты вектора
     */
    private Point point;

    /**
     * Создает вектор из двух точек.
     * @param start точка начала
     * @param end точка конца
     */
    public Vector(Point start, Point end) {
        point = start.minus(end);
    }

    /**
     * Векторное произведение данного вектора на другой.
     * @param op вектор на который умножаем
     * @return векторное произведение
     */
    public double composite(Vector op) {
        return point.getX() * op.point.getY() - point.getY() * op.point.getX();
    }
}


