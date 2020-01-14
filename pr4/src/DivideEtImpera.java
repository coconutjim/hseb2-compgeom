/**
 * Created by Lev on 13.02.14.
 */
import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class DivideEtImpera {



    /** Разделяет массив на два массива (сортированные по х и по у кооординате) и запускает алгоритм с рек. вызовами */
    public Pair divideAndConquer(ArrayList<Point> all, DrawPanel panel, int delay, JLabel label)
            throws NullPointerException, InterruptedException {

        ArrayList<Point> x = createX(all);
        ArrayList<Point> y = createY(all);

        return divideAndConquer(x, y, panel, delay, label);
    }

    public Pair divideAndConquer(List<Point> x, List<Point> y, DrawPanel panel, int delay, JLabel label)
            throws NullPointerException, InterruptedException {

        int number = x.size();

        /** Метод грубой силы, если точек меньше 4 */
        if (number <= 3) {
            label.setText("грубая сила");
            try {
                return bruteForce(x, panel, delay);
            }
            catch (WrongNumberArgsException e) {
                System.out.print(e.getMessage());
            }

        }

        /** Разделение сортированного по х массива на два подмассива */
        int index = number / 2;

        List<Point> leftX = x.subList(0, index);
        List<Point> rightX = x.subList(index, number);
        double xLine = x.get(index).getX();

        label.setText("разделение");


        /** Формирование двух отсортированных по у массивам из общего массива за линейное время */
        ArrayList<Point> leftY = new ArrayList<Point>();
        ArrayList<Point> rightY = new ArrayList<Point>();

        for (int i = 0; i < y.size(); ++ i) {
            if (y.get(i).getX() <= xLine) {
                leftY.add(y.get(i));
            }
            else {
                rightY.add(y.get(i));
            }
        }


        /** Формирование двух отсортированных по у массивам из общего массива */
        /*ArrayList<Point> leftY = createY(leftX);
        ArrayList<Point> rightY = createY(rightX);*/


        panel.setColor1(Color.BLACK);
        panel.setXLine(xLine);
        panel.repaint();
        Thread.sleep(delay);


        /** Рекурсивный поиск минимального расстояния среди точек левого и правого подмножеств */
        Pair minLeft = divideAndConquer(leftX, leftY, panel, delay, label);
        Pair minRight = divideAndConquer(rightX, rightY, panel, delay, label);


        label.setText("объединение");

        panel.setMinPair(null);
        panel.setXLine(-1);
        panel.setMinLeftPair(minLeft);
        panel.setMinRightPair(minRight);
        panel.repaint();
        Thread.sleep(delay);

        /** Нахождение максимума из 2 */
        Pair minPair = minLeft.getDistance() < minRight.getDistance() ? minLeft : minRight;


        panel.deletePair(minLeft);
        panel.deletePair(minRight);
        panel.deletePair(minLeft.getDistance() < minRight.getDistance() ? minRight : minLeft);

        panel.setMinLeftPair(null);
        panel.setMinRightPair(null);
        panel.setMinPair(minPair);
        panel.repaint();
        Thread.sleep(delay);


        /** Поиск среди остальных точек */

        panel.setColor1(Color.RED);
        panel.setColor2(Color.WHITE);
        panel.setXLine(xLine);
        panel.setXLeftLine(xLine - minPair.getDistance());
        panel.setXRightLine(xLine + minPair.getDistance());
        List<Point> points = new ArrayList<Point>();

        label.setText("комбинирование");

        for (Point p : y) {
            if (Math.abs(xLine - p.getX()) < minPair.getDistance()) {
                points.add(p);
            }
        }
        int length = points.size();
        for (int i = 0; i < length - 1; ++ i) {
            for (int j = i + 1; j < length && j < i + 8; ++ j) {

                panel.setTempPair(new Pair(points.get(i), points.get(j),
                        Point.distance(points.get(i), points.get(j))));
                panel.repaint();
                Thread.sleep(delay);


                if ( Point.distance(points.get(j), points.get(i)) < minPair.getDistance() &&
                        ! points.get(j).equal(points.get(i))) {
                    minPair = new Pair(points.get(j), points.get(i), Point.distance(points.get(j), points.get(i)) );


                    panel.setMinPair(minPair);
                    panel.repaint();
                    Thread.sleep(delay);



                }
            }

        }

        panel.setTempPair(null);
        panel.setPair(minPair);
        panel.repaint();
        Thread.sleep(delay);





        panel.setXLine(-1);
        panel.setXLeftLine(-1);
        panel.setXRightLine(-1);
        panel.setMinPair(null);
        panel.setTempPair(null);
        panel.repaint();



        return minPair;
    }

    /** Алгортим нахождения минимального расстояния путем грубой силы */
    Pair bruteForce(List<Point> points, DrawPanel p, int delay)
            throws NullPointerException, WrongNumberArgsException, InterruptedException {

        /** Точек должно быть 3 или больше */
        if (points.size() < 2) {
            throw new WrongNumberArgsException("less then 2");
        }

        /** Точка - не претендент */
        double minDist = 2 * DrawPanel.getSIZE();
        Pair minPair = new Pair(new Point(0, 0), new Point(0, 0), minDist);
        p.setColor2(Color.PINK);


        /** И проходим по массиву */
        for (int i = 0; i < points.size() - 1; ++ i) {
            for (int j = i + 1; j < points.size(); ++ j) {


                p.setTempPair(new Pair(points.get(i), points.get(j),
                        Point.distance(points.get(i), points.get(j))));
                p.repaint();
                Thread.sleep(delay);



                /** Точки не должны быть одинаковыми */
                if (Point.distance(points.get(i), points.get(j)) < minDist &&
                        ! points.get(i).equal(points.get(j))) {
                    minDist = Point.distance(points.get(i), points.get(j));
                    minPair = new Pair(points.get(i), points.get(j), minDist);


                }
            }
        }

        p.setTempPair(null);
        p.setMinPair(minPair);
        p.setPair(minPair);
        p.repaint();
        Thread.sleep(delay);

        return minPair;
    }

    /** Создает массив X - точки, сортированные по х координате*/
    ArrayList<Point> createX(List<Point> all) {
        ArrayList<Point> x = new ArrayList<Point>(all);
        Collections.sort(x, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.getX() < o2.getX()) {
                    return -1;
                }
                else {
                    if (o1.getX() > o2.getX()) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                }
            }
        });
        return  x;
    }

    /** Создает массив Y - точки, сортированные по y координате*/
    ArrayList<Point> createY(List<Point> all) {
        ArrayList<Point> y = new ArrayList<Point>(all);
        Collections.sort(y, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.getY() < o2.getY()) {
                    return -1;
                } else {
                    if (o1.getY() > o2.getY()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        return  y;
    }


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
}
