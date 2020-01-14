import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Lev on 22.02.14.
 */
public class AllIntersections {

    /** Алгоритм */
    public ArrayList<IntersectingSegments> findAllIntersections(ArrayList<Segment> segments, DrawPanel drawPanel,
                                                                LegendPanel legendPanel, JLabel labelProcess, int delay)
            throws  InterruptedException {




        /** Результат */
        ArrayList<IntersectingSegments> result = new ArrayList<IntersectingSegments>();
        drawPanel.setIntersectingSegments(result);

        /** Формирование списка точек событий (Event Point Shedule)*/
        TreeSet<EventPoint> ePS = new TreeSet<EventPoint>();
        for (Segment s : segments) {
            ePS.add(new EventPoint(s.getUp(), s, true, false));
            ePS.add(new EventPoint(s.getDown(), s, false, false));
        }

        /** Состояния относительно выметающей прямой (Sweep Line Status) */
        SegmentComparator segmentComparator = new SegmentComparator(0);
        TreeSet<Segment> sLS = new TreeSet<Segment>(segmentComparator);


        /** Если меньше 10, визуализация */
        if (segments.size() <= 10) {
            //ArrayList<Color> colors = LegendPanel.getRandomColors();
            ArrayList<Color> colors = LegendPanel.getColors();
            for (int i = 0; i < segments.size(); ++ i) {
                segments.get(i).setColor(colors.get(i));
            }
            drawPanel.setSLS(sLS);
            legendPanel.setSegments(sLS);
        }

        /** Обработка всех точек событий */
        EventPoint q;

        while (! ePS.isEmpty()) {

            /** Очередная точка */
            q = ePS.first();

            /** Задаем выметающую прямую */
            segmentComparator.setX(q.getPoint().getX());
            segmentComparator.setY(q.getPoint().getY());

            labelProcess.setText("Следующая точка");
            drawPanel.setSweepingLine(q.getPoint().getY());
            repaint(drawPanel);
            Thread.sleep(delay);

            /** Обработка */
            handleEventPoint(q, ePS, sLS, result, drawPanel, legendPanel, labelProcess, delay);

            /** Удаление точки */
            ePS.pollFirst();
        }


        return result;
    }


    /** Обработка точки события */
    private void handleEventPoint(EventPoint q, TreeSet<EventPoint> ePS ,TreeSet<Segment> sLS,
                                  ArrayList<IntersectingSegments> result,
                                  DrawPanel drawPanel, LegendPanel legendPanel, JLabel labelProcess, int delay)
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
            /*if (eventPoint.getPoint().getY() < q.getPoint().getY()) {
                break;
            }*/
            if (! eventPoint.isUp() && ! eventPoint.isOn() && eventPoint.getPoint().equals(q.getPoint())) {
                L.add(eventPoint);
            }
        }

        /** Формирование C(p) */
        ArrayList<EventPoint> C = new ArrayList<EventPoint>();
        for (EventPoint eventPoint : ePS) {
            /*if (eventPoint.getPoint().getY() < q.getPoint().getY()) {
                break;
            }*/
            if (eventPoint.isOn() && eventPoint.getPoint().equals(q.getPoint())) {
                C.add(eventPoint);
            }
        }

        /** Сообщить о пересечении */
        if (U.size() + L.size() + C.size() > 1) {

            ArrayList<Segment> segments = new ArrayList<Segment>();
            for (EventPoint eventPoint : U) {
                segments.add(eventPoint.getSegment());
            }
            for (EventPoint eventPoint : L) {
                segments.add(eventPoint.getSegment());
            }
            for (EventPoint eventPoint : C) {
                segments.add(eventPoint.getSegment());
            }
            result.add(new IntersectingSegments(segments, q.getPoint()));

            labelProcess.setText("Новое пересечение");
            repaint(drawPanel);
            repaint(legendPanel);
            Thread.sleep(delay);


            /** Зачищаем ненужные события (одно надо оставить, потом удалится)*/
            /*for (EventPoint eventPoint : C) {
                System.out.println(ePS.remove(eventPoint));
            }*/
            for (int i = 0; i < C.size() - 1; ++ i) {
                ePS.pollFirst();
            }

        }

        /** Работа с состояниями */

        /** Удаление L и C из состояний */
        for (EventPoint eventPoint : L) {
            try {
                if (eventPoint.getSegment() == sLS.first()) {
                    sLS.pollFirst();
                    continue;
                }
            }
            catch (NoSuchElementException e) {
                //
            }
            sLS.remove(eventPoint.getSegment());
        }
        for (EventPoint eventPoint : C) {
            try {
                if (eventPoint.getSegment() == sLS.first()) {
                    sLS.pollFirst();
                    continue;
                }
            }
            catch (NoSuchElementException e) {
                //
            }
            sLS.remove(eventPoint.getSegment());
        }

        /** Добавление U и C в состояния */
        for (EventPoint eventPoint : U) {
            sLS.add(eventPoint.getSegment());
        }
        for (EventPoint eventPoint : C) {
            sLS.add(eventPoint.getSegment());
        }

        labelProcess.setText("Удаление и добавление");
        repaint(drawPanel);
        repaint(legendPanel);
        Thread.sleep(delay);

        /** Если только конец отрезка */
        if (U.size() + C.size() == 0) {

            /** Левый и правый соседи */
            if (sLS.higher(q.getSegment()) != null && sLS.lower(q.getSegment()) != null) {
                labelProcess.setText("Левый и правый соседи");
                findNewEvent(sLS.higher(q.getSegment()), sLS.lower(q.getSegment()), q, ePS, result,
                    drawPanel, legendPanel, labelProcess, delay);
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
            if (sLS.floor(min.getSegment()) != null && sLS.lower(sLS.floor(min.getSegment())) != null)  {
                labelProcess.setText("Сосед слева");
                findNewEvent(sLS.floor(min.getSegment()), sLS.lower(sLS.floor(min.getSegment())), q, ePS, result,
                    drawPanel, legendPanel, labelProcess, delay);
            }

            /** Самый правый сегмент из U и C */
            EventPoint max = UC.last();

            /** Этот сегмент в sLS и его правый сосед */
            if (sLS.ceiling(max.getSegment()) != null && sLS.higher(sLS.ceiling(max.getSegment())) != null) {
                labelProcess.setText("Сосед справа");
                findNewEvent(sLS.ceiling(max.getSegment()), sLS.higher(sLS.ceiling(max.getSegment())), q, ePS, result,
                    drawPanel, legendPanel, labelProcess, delay);
            }


        }

    }


    /** Нахождение новых пересечений */
    private void findNewEvent(Segment sl, Segment sr, EventPoint q, TreeSet<EventPoint> ePS,
                              ArrayList<IntersectingSegments> result,
                              DrawPanel drawPanel, LegendPanel legendPanel, JLabel labelProcess, int delay)
            throws InterruptedException {


        drawPanel.setS1(sl);
        drawPanel.setS2(sr);
        repaint(drawPanel);
        Thread.sleep(delay);

        drawPanel.setS1(null);
        drawPanel.setS2(null);


        /** Проверка на пересечение */
        if (segmentsIntersect(sl, sr)) {

            Point point = findIntersectionPoint(sl, sr);

            /** Проверка на существования такого пересечения */
            for (IntersectingSegments intersectingSegments : result) {
                if (intersectingSegments.getPoint().equals(point)) {
                    return;
                }
            }


            /** Вставка событий  */
            ePS.add(new EventPoint(point, sr, false, true));
            ePS.add(new EventPoint(point, sl, false, true));

            drawPanel.addEP(point);
            labelProcess.setText("Добавление события");
            repaint(drawPanel);
            Thread.sleep(delay);

        }

    }

    /**
     *  Находит точку пересечения (при условии, что отрезки пересекаеются)
     * @param s1 первый отрезок
     * @param s2 втоой отрезок
     * @return точку пересечения
     */
    private Point findIntersectionPoint(Segment s1, Segment s2) {

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
    private boolean onSegment(Point pi, Point pj, Point pk) {
        if ( Math.min(pi.getX(), pj.getX()) <= pk.getX() && Math.max(pi.getX(), pj.getX()) >= pk.getX() &&
                Math.min(pi.getY(), pj.getY()) <= pk.getY() && Math.max(pi.getY(), pj.getY()) >= pk.getY() ) {
            return true;
        }
        return false;
    }

    /** Определяет, пересекаются ли отрезки. */
    private boolean segmentsIntersect(Segment s1, Segment s2) {
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
        if (d1 == 0 && onSegment(p3, p4, p1)) {
            return true;
        }
        if (d2 == 0 && onSegment(p3, p4, p2)) {
            return true;
        }
        if (d3 == 0 && onSegment(p1, p2, p3)) {
            return true;
        }
        if (d4 == 0 && onSegment(p1, p2, p4)) {
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
class SegmentComparator implements Comparator<Segment> {

    /** Координата выметающей прямой */
    private double y;

    /** Какую точку рассматриваем */
    private double x;

    public SegmentComparator(double y) {
        this.y = y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public int compare(Segment o1, Segment o2) {

        if (o1 == o2) {
            return 0;
        }

        double x1;
        double x2;

        try {
            x1 = (int) ( o1.calculateX(y) * 100) / 100.;
        }
        catch (IllegalStateException e) {
            /** Если первый отрезок горизонтальный, проверяем, проходит ли он через точку второго */
            try {
                x2 = (int) ( o2.calculateX(y) * 100) / 100.;
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
            x2 = (int) ( o2.calculateX(y) * 100) / 100.;
        }
        catch (IllegalStateException e) {
            /** Если второй отрезок горизонтальный, проверяем, проходит ли он через точку первого */
            try {
                x1 = (int) ( o1.calculateX(y) * 100) / 100.;
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


        if (x1 < x2) {
            return -1;
        }
        else {
            if (x1 > x2) {
                return 1;
            }
            else {
                /** Векторное произведение */
                double composition = new Vector(new Point(x1, y), o1.getDown()).composite(new Vector(new Point(x2, y),
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
        Segment s1 = o1.getSegment();
        Segment s2 = o2.getSegment();

        if (s1 == s2) {
            return 0;
        }

        double x1;
        double x2;

        try {
            x1 = (int) ( s1.calculateX(y) * 100) / 100.;
        }
        catch (IllegalStateException e) {
            /** Если первый отрезок горизонтальный, проверяем, проходит ли он через точку второго */
            try {
                x2 = (int) ( s2.calculateX(y) * 100) / 100.;
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
            x2 = (int) ( s2.calculateX(y) * 100) / 100.;
        }
        catch (IllegalStateException e) {
            /** Если второй отрезок горизонтальный, проверяем, проходит ли он через точку первого */
            try {
                x1 = (int) ( s1.calculateX(y) * 100) / 100.;
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

        if (x1 < x2) {
            return -1;
        }
        else {
            if (x1 > x2) {
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
    private Segment segment;
    private boolean up;
    private boolean on;
    public EventPoint(Point point, Segment segment, boolean up, boolean on) {
        this.point = point;
        this.segment = segment;
        this.up = up;
        this.on = on;
    }

    public Point getPoint() {
        return  point;
    }
    public Segment getSegment() {
        return segment;
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


