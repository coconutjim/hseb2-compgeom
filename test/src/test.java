import java.util.ArrayList;

/**
 * Created by Lev on 30.01.14.
 */
/*public class test {
    public static void main(String[] args) {
        DivideEtImpera alg = new DivideEtImpera();
        for (int i = 0; i < 10; ++ i) {
            ArrayList<Point> list = DivideEtImpera.generatePoints(10);
            Pair min1 = alg.divideAndConquer(list);
            Pair min2 = find(list);
            if ( min1.getDistance() != min2.getDistance()) {
                System.out.println("wrong");
                System.out.println(min1.getDistance());
                System.out.println(min2.getDistance());
                return;
            }
            System.out.println(min1);
            System.out.println(min2);
            System.out.println();
        }
        System.out.print("OK");
    }

    public static Pair find(ArrayList<Point> list) {
        Pair minPair = new Pair(list.get(0), list.get(1), 100000);
        double minDist = 1000000l;
        for (int i = 0; i < list.size() - 1; ++ i) {
            for (int j = i + 1; j < list.size(); ++ j) {
                if (Point.distance(list.get(i), list.get(j)) < minDist &&
                        ! list.get(i).equal(list.get(j))) {
                    minDist = Point.distance(list.get(i), list.get(j));
                    minPair = new Pair(list.get(i), list.get(j), minDist);
                }
            }
        }
        return minPair;
    }
} */

public class test {
    A aaa = new A();
    String s = "t";
    public static void main(String[] args) {
        test t = new test();
        t.aaa.setA(t.s);
        t.s = new String("fff");
        System.out.print(t.aaa.getA());

    }
}


class A {
    public String a;
    public void setA(String f) {
        a = f;
    }
    public String getA() {
         return a;
    }
}
