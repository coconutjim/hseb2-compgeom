import java.util.*;

/**
 * Created by Lev on 02.02.14.
 */
public class JarvisAlgorithm {

    /** Самая нижняя (и левая, если их несколько) */
    private Point pBot;


    /**
     * Генерирует список точек по их заданному количеству
     * @param count количество
     * @return список точек
     */
    public static ArrayList<Point> generatePoints(int count) {
        ArrayList<Point> list = new ArrayList<Point>();
        Random gen = new Random();
        for (int i = 0; i < count; ++ i) {
            list.add(new Point(gen.nextInt(DrawPanel.getSIZE() + 1), gen.nextInt(DrawPanel.getSIZE() + 1)));
        }
        return list;
    }

    /**
     * Ищет выпуклую оболочку сканированием Грэхема
     * @param Q список точек
     * @return CH(Q)
     */
    public Stack<Point> scanJarvis(List<Point> Q, DrawPanel panel, int delay) {
        Stack<Point> ch = new Stack<Point>(); // стек точек оболочки
        List<Point> points = new ArrayList<Point>(Q); // будут удаляться точки, уже состоящие в оболочке, кроме  p0
        /** Нахождение нижней */
        pBot = findBotPoint(Q);

        ch.push(pBot);
        panel.setPoints(ch);
        panel.repaint();

        /** Алгоритм */
        Point current;
        do {
            try {
                Thread.sleep(delay);
                current = findNext(points, ch, panel, delay);
            }
            catch(InterruptedException e) {return null;}
            panel.repaint();
        } while (!current.equal(pBot));

        panel.setEnd(true);
        panel.repaint();

        return ch;
    }


    public Point findNext(List<Point> src, Stack<Point> ch, DrawPanel p, int delay)
            throws  InterruptedException {
        int ind = 0;
        Point current = ch.peek(); // очередная точка

        double comp;
        for (int i = 1; i < src.size(); ) {

            /** Отрисовка очередных точек */
            p.setTempMinPoint(src.get(ind));
            p.setTempPoint(src.get(i));
            p.repaint();
            Thread.sleep(delay);

            /** Нахождение и сравнение векторных произведений */
            Vector v1 = new Vector(current, src.get(ind));
            Vector v2 = new Vector(current, src.get(i));
            comp = v1.composite(v2);
            if (comp < 0) {
                ind = i;
            }
            if (comp == 0 && !current.equal(src.get(i))) {
                if (v1.length() > v2.length()) {
                    src.remove(i);
                }
                else {
                    src.remove(ind);
                    ind = i - 1;
                }
                continue;
            }

            ++ i;
        }

        /** Занесение точки в стек*/
        ch.push(src.get(ind));

        return src.remove(ind); // удаление точки
    }


    /**
     * Поиск самой нижней точки (если таких несколько, то самой левой)
     * @param src список точек
     */
    public Point findBotPoint(List<Point> src) {
        double minX = src.get(0).getX(), minY = src.get(0).getY();
        int ind = 0;
        for (int i = 0; i < src.size(); ++ i) {
            if (src.get(i).getY() < minY || (src.get(i).getY() == minY && src.get(i).getX() < minX)) { // ниже, либо так же, но левее
                minX = src.get(i).getX();
                minY = src.get(i).getY();
                ind = i;
            }
        }
        return src.get(ind);

    }
}


/**
 * Моделирует вектор.
 */
class Vector {
    private Point point; // координаты вектора
    public Vector(Point start, Point end) {
        point = start.minus(end);
    }
    public double composite(Vector op) {
        return point.getX() * op.point.getY() - point.getY() * op.point.getX();
    }
    public double length() {
        return Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY());
    }
}
