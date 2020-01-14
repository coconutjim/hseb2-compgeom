import java.util.*;

/**
 * Created by Lev on 02.02.14.
 */
public class GrahamAlgorithm {

    /** Самая нижняя (и левая, если их несколько) */
    private Point p0;

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
    public StackG<Point> scanGraham(List<Point> Q, DrawPanel panel, int delay) {
        find_point(Q);
        polar_sort(Q);
        check_for_same_angles(Q);
        StackG<Point> stack = new StackG<Point>();
        stack.push(Q.get(0));
        stack.push(Q.get(1));
        stack.push(Q.get(2));
        panel.setPoints(stack);
        for (int i = 3; i < Q.size(); ++ i) {
            while (new Vector(stack.next_to_top(), stack.top()).composite(new Vector(stack.top(), Q.get(i))) < 0) {
                stack.pop();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    return null;
                }
                panel.validate();
                panel.repaint();
            }
            stack.push(Q.get(i));
            panel.validate();
            panel.repaint();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return null;
            }
        }
        panel.setEnd(true);
        panel.validate();
        panel.repaint();
        return stack;
    }

    /**
     * Сортировка стека в порядке возрастания полярного угла,
     * измеряемого против часовой стрелки относительно первой точки.
     * Первая точка должна быть уже определена, она вытаскивается на время сортировки
     * @param src список точек
     */
    public void polar_sort(List<Point> src) {

        src.remove(0);
        Collections.sort(src, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                Vector v1 = new Vector(p0, o1);
                Vector v2 = new Vector(p0, o2);
                if (v1.composite(v2) > 0) {
                    return -1;
                }
                else {
                    if (v1.composite(v2) < 0) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                }
            }
        });
        src.add(0, p0);
    }


    /**
     * При совпадениях углов точки ближе к p0 удаляются.
     * Первая точка должна быть уже определена.
     * @param src список точек
     */
    public void check_for_same_angles(List<Point> src) {
            for (int i = 1; i < src.size() - 1;) {
            Vector v1 = new Vector(p0, src.get(i)); // вектор p0pi-1
            Vector v2 = new Vector(p0, src.get(i  + 1)); // вектор p0pi
            if (v1.composite(v2) == 0) {
                if (v1.length() < v2.length()) {
                    src.remove(i);
                }
                else {
                    src.remove(i + 1);
                }
                continue;
            }
            i++;
        }
    }

    /**
     * Поиск самой нижней точки (если таких несколько, то самой левой)
     * и ее становление на первое место (на дно стека)
     * @param src список точек
     */
    public void find_point(List<Point> src) {
        double minX = src.get(0).getX(), minY = src.get(0).getY();
        int ind = 0;
        for (int i = 0; i < src.size(); ++ i) {
            if (src.get(i).getY() < minY || (src.get(i).getY() == minY && src.get(i).getX() < minX)) { // ниже, либо так же, но левее
                minX = src.get(i).getX();
                minY = src.get(i).getY();
                ind = i;
            }
        }
        Point temp = src.get(ind);
        src.set(ind, src.get(0));
        src.set(0, temp);
        p0 = src.get(0);

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
