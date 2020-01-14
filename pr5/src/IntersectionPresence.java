import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Lev on 22.02.14.
 */
public class IntersectionPresence {

    public AlgResult findIntersection(ArrayList<Segment> segments, DrawPanel drawPanel,
                                      LegendPanel legendPanel, JLabel labelProcess, int delay) throws  InterruptedException {
        /** Формирование списка точек событий(Event Point Shedule)*/
        ArrayList<EventPoint> ePS = new ArrayList<EventPoint>();
        for (Segment s : segments) {
            ePS.add(new EventPoint(s.getLeft(), s, true));
            ePS.add(new EventPoint(s.getRight(), s, false));
        }


        /** Сортировка списка */
        Collections.sort(ePS, getEventPointComparator());

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


        /** Сам алгоритм */
        for (EventPoint eventPoint : ePS) {

            segmentComparator.setX(eventPoint.getPoint().getX());
            Segment currentSegment = eventPoint.getSegment();


            drawPanel.setS1(null);
            drawPanel.setS2(null);
            String text = eventPoint.isLeft()? "Точка начала" : "Точка конца";
            labelProcess.setText(text);
            drawPanel.setSweepingLine(eventPoint.getPoint().getX());
            drawPanel.repaint();
            Thread.sleep(delay);


            /** Если левая */
            if (eventPoint.isLeft()) {

                /** Отсортирован по убыванию */
                Segment above = sLS.floor(currentSegment);
                Segment bottom = sLS.ceiling(currentSegment);

                /** Insert */
                sLS.add(currentSegment);

                labelProcess.setText("Вставка");
                legendPanel.repaint();
                drawPanel.repaint();
                Thread.sleep(delay);

                /** "над" */
                if (above != null) {

                    labelProcess.setText("Проверка \"над\"");
                    drawPanel.setS1(currentSegment);
                    drawPanel.setS2(above);
                    drawPanel.repaint();
                    Thread.sleep(delay);

                    /** Проверка на пересечение */
                    if (segmentsIntersect(above, currentSegment)) {
                        return new AlgResult(true, above, currentSegment);
                    }
                }

                /** "под" */
                if (bottom != null) {

                    labelProcess.setText("Проверка \"под\"");
                    drawPanel.setS1(currentSegment);
                    drawPanel.setS2(bottom);
                    drawPanel.repaint();
                    Thread.sleep(delay);

                    /** Проверка на пересечение */
                    if (segmentsIntersect(bottom, currentSegment)) {
                        return new AlgResult(true, bottom, currentSegment);
                    }
                }

            }
            /** Если правая */
            else {

                /** Delete */
                sLS.remove(currentSegment);

                labelProcess.setText("Удаление");
                drawPanel.repaint();
                legendPanel.repaint();
                Thread.sleep(delay);



                /** Отсортирован по убыванию */
                Segment above = sLS.floor(currentSegment);
                Segment bottom = sLS.ceiling(currentSegment);


                /** Если есть "над" и "под" и они пересекаются */
                if (above != null && bottom != null) {

                    labelProcess.setText("Проверка \"над\"\" и \"под\"");
                    drawPanel.setS1(above);
                    drawPanel.setS2(bottom);
                    drawPanel.repaint();
                    Thread.sleep(delay);

                    if (segmentsIntersect(above, bottom)) {
                        return new AlgResult(true, bottom, above);
                    }
                }
            }
        }
        return new AlgResult(false, null, null);
    }


    /** Упорядочение точек */
    private Comparator<EventPoint> getEventPointComparator() {
        return new Comparator<EventPoint>() {
            @Override
            public int compare(EventPoint o1, EventPoint o2) {
                if (o1.getPoint().getX() > o2.getPoint().getX()) {
                    return 1;
                } else if (o1.getPoint().getX() < o2.getPoint().getX()) {
                    return -1;
                    /** Если ковертикальны */
                } else if (! o1.isLeft() && o2.isLeft()) {
                    return 1;
                } else if (o1.isLeft() && ! o2.isLeft()) {
                    return -1;
                    /** Если обе левые (правые) */
                } else if (o1.getPoint().getY() > o2.getPoint().getY()) {
                    return 1;
                } else if (o1.getPoint().getY() < o2.getPoint().getY()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
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
        Point p1 = s1.getLeft();
        Point p2 = s1.getRight();
        Point p3 = s2.getLeft();
        Point p4 = s2.getRight();
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
}

/** Компаратор для отрезков с учетом выметающей прямой */
class SegmentComparator<T extends Segment> implements Comparator<T> {

    /** Координата выметающей прямой */
    private double x;

    public SegmentComparator(double x) {
        this.x = x;
    }

    public void setX(double x) {
        this.x = x;
    }

    /** Сортируем по убыванию для удобства отображения */
    @Override
    public int compare(T o1, T o2) {

        double y1 = calculateY(o1);
        double y2 = calculateY(o2);

        if (y1 > y2) {
            return -1;
        }
        else {
            if (y1 < y2) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    /** Вычисление Y - координат у отрезков */
    private double calculateY(Segment segment) {
        Point x1 = segment.getLeft();
        Point x2 = segment.getRight();
        /** y = ax + b */
        double a = ( x2.getY() - x1.getY() ) / ( x2.getX() - x1.getX() );
        double b = x1.getY() - a * x1.getX();

        return a * x + b;
    }
}


/** Хранилище для точек событий */
class EventPoint {
    private Point point;
    private Segment segment;
    private boolean left;
    public EventPoint(Point point, Segment segment, boolean left) {
        this.point = point;
        this.segment = segment;
        this.left = left;
    }

    public Point getPoint() {
        return  point;
    }
    public Segment getSegment() {
        return segment;
    }
    public boolean isLeft() {
        return left;
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