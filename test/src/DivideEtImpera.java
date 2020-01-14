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
    public Pair divideAndConquer(ArrayList<Point> all) {

        ArrayList<Point> x = createX(all);
        ArrayList<Point> y = createY(all);

        return divideAndConquer(x, y);
    }

    public Pair divideAndConquer(List<Point> x, List<Point> y) {

        int number = x.size();

        /** Метод грубой силы, если точек меньше 4 */
        if (number <= 3) {
            try {

                return bruteForce(x);
            }
            catch (WrongNumberArgsException e) {
                System.out.println(e.getMessage());
                return null;
            }
            catch (InterruptedException e) {
                return null;
            }
        }

        /** Разделение сортированного по х массива на два подмассива */
        int index = number / 2;

        List<Point> leftX = x.subList(0, index);
        List<Point> rightX = x.subList(index, number);
        double xLine = x.get(index).getX();




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





        /** Рекурсивный поиск минимального расстояния среди точек левого и правого подмножеств */
        Pair minLeft = divideAndConquer(leftX, leftY);
        Pair minRight = divideAndConquer(rightX, rightY);


        /** Нахождение максимума из 2 */
        Pair minPair = minLeft.getDistance() < minRight.getDistance() ? minLeft : minRight;


        /** Поиск среди остальных точек */


        List<Point> points = new ArrayList<Point>();



        for (Point p : y) {
            if (Math.abs(xLine - p.getX()) < minPair.getDistance()) {
                points.add(p);
            }
        }
        int length = points.size();
        for (int i = 0; i < length; ++ i) {
            for (int j = i + 1; j < length && j < i + 8; ++ j) {



                if ( Point.distance(points.get(j), points.get(i)) < minPair.getDistance() &&
                        ! points.get(j).equal(points.get(i))) {
                    minPair = new Pair(points.get(j), points.get(i), Point.distance(points.get(j), points.get(i)) );
                }
            }

        }




        return minPair;
    }

    /** Алгортим нахождения минимального расстояния путем грубой силы */
    Pair bruteForce(List<Point> points)
            throws WrongNumberArgsException, InterruptedException {

        /** Точек должно быть 3 или больше */
        if (points.size() < 2) {
            throw new WrongNumberArgsException("less then 2");
        }

        /** Точка - не претендент */
        double minDist = 2 * 1000000;
        Pair minPair = new Pair(new Point(0, 0), new Point(0, 0), minDist);


        /** И проходим по массиву */
        for (int i = 0; i < points.size() - 1; ++ i) {
            for (int j = i + 1; j < points.size(); ++ j) {




                /** Точки не должны быть одинаковыми */
                if (Point.distance(points.get(i), points.get(j)) < minDist &&
                        ! points.get(i).equal(points.get(j))) {
                    minDist = Point.distance(points.get(i), points.get(j));
                    minPair = new Pair(points.get(i), points.get(j), minDist);


                }
            }
        }


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
            list.add(new Point(gen.nextInt(1000), gen.nextInt(1000)));
        }
        return list;
    }
}
