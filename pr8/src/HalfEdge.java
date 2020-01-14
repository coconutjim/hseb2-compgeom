import java.util.ArrayList;

/**
 * Created by Lev on 08.03.14.
 */
public class HalfEdge extends Segment {

    /** Начало */
    private Vertex origin;

    /** Конец */
    private Vertex end;

    /** Предыдущее полуребро */
    private HalfEdge prev;

    /** Следующее полуребро */
    private HalfEdge next;

    /** Помощник */
    private Vertex helper;


    public HalfEdge() {

    }

    public HalfEdge(Vertex origin, Vertex end, HalfEdge prev, HalfEdge next) {
        super(origin, end);
        this.origin = origin;
        this.end = end;
        this.prev = prev;
        this.next = next;

    }

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    public HalfEdge getNext() {
        return next;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    public void setHelper(Vertex helper) {
        this.helper = helper;
    }

    public HalfEdge getPrev() {
        return prev;

    }

    public void setPrev(HalfEdge prev) {
        this.prev = prev;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public Vertex getHelper() {
        return helper;
    }


    public static HalfEdge copyData(HalfEdge halfEdge) {


        HalfEdge edge = new HalfEdge(halfEdge.getOrigin().copy(), halfEdge.getEnd().copy(), null, null);
        edge.getOrigin().setNext(edge);
        // Разворачиваем
        HalfEdge current = edge;
        HalfEdge next = halfEdge.getNext();
        while (next != halfEdge) {
            HalfEdge nextEdge = new HalfEdge(next.getOrigin().copy(), next.getEnd().copy(), null, null);

            current.setNext(nextEdge);
            nextEdge.setPrev(current);

            nextEdge.getOrigin().setNext(nextEdge);
            nextEdge.getOrigin().setPrev(current);

            next = next.getNext();
            current = current.getNext();
        }

        current.setNext(edge);
        edge.setPrev(current);

        edge.getOrigin().setPrev(current);

        return edge;
    }

    /** Разбивает на полигоны */
    public static ArrayList<HalfEdge> split(HalfEdge edge, ArrayList<HalfEdge> diagonals, DrawPanel d)
            throws InterruptedException {

        /** Делаем полигоны */

        /** Все ребра */
        ArrayList<HalfEdge> all = new ArrayList<HalfEdge>();

        all.add(edge);
        HalfEdge next = edge.getNext();
        while (next != edge) {
            all.add(next);
            next = next.getNext();
        }

        ArrayList<HalfEdge> result = new ArrayList<HalfEdge>();

        /** Делаем у диагоналей копии вертексов*/
        for (int i = 0; i < diagonals.size(); ++ i) {
            for (int i1 = 0; i1 < diagonals.size(); ++ i1) {
                if (i != i1) {
                    HalfEdge d1 = diagonals.get(i);
                    HalfEdge d2 = diagonals.get(i1);


                    /*if (d1.getOrigin() == d2.getOrigin()) {
                        d1.setOrigin(copyVertex(d2.getOrigin()));
                        //d2.setOrigin(copyVertex(d2.getOrigin()));
                    }
                    if (d1.getOrigin() == d2.getEnd()) {
                        d1.setOrigin(copyVertex(d2.getEnd()));
                        //d2.setEnd(copyVertex(d2.getEnd()));
                    }
                    if (d1.getEnd() == d2.getOrigin()) {
                        d1.setEnd(copyVertex(d2.getOrigin()));
                        //d1.setEnd(copyVertex(d1.getEnd()));
                    }
                    if (d1.getEnd() == d2.getEnd()) {
                        d1.setEnd(copyVertex(d2.getEnd()));
                        //d2.setEnd(copyVertex(d2.getEnd()));
                    }*/

                }
            }
        }

        /** Перелинковка */
        for (HalfEdge diagonal : diagonals) {

            Vertex vertex = diagonal.getOrigin();
            Vertex helper = diagonal.getEnd();
            /* Копирую
            Vertex vertex1 = vertex.copy();
            Vertex helper1 = helper.copy();
            HalfEdge vp = vertex.getPrev();
            HalfEdge vn = vertex.getNext();
            HalfEdge hp = helper.getPrev();
            HalfEdge hn = helper.getNext();
            vertex1.setPrev(vp);
            vertex1.setNext(vn);
            helper1.setPrev(hp);
            helper1.setNext(hn);*/

            /*Vertex vertex1 = copyVertex(vertex);
            Vertex helper1 = copyVertex(helper);

            HalfEdge halfEdge1 = new HalfEdge(helper, vertex, helper.getPrev(), vertex.getNext());

            helper.getPrev().setNext(halfEdge1);
            vertex.getNext().setPrev(halfEdge1);

            helper.setNext(halfEdge1);
            vertex.setPrev(halfEdge1);

            d.setS1(halfEdge1.getPrev());
            d.setPrevHelper(halfEdge1.getPrev().getOrigin());
            d.setS2(halfEdge1.getNext());
            d.setHelper(halfEdge1.getNext().getOrigin());
            d.repaint();
            Thread.sleep(3000);

            HalfEdge halfEdge2 = new HalfEdge(vertex1, helper1, vertex1.getPrev(), helper1.getNext());

            vertex1.getPrev().setNext(halfEdge2);
            helper1.getNext().setPrev(halfEdge2);

            vertex1.setNext(halfEdge2);
            helper1.setPrev(halfEdge2);

            d.setS1(halfEdge2.getPrev());
            d.setS2(halfEdge2.getNext());
            d.setPrevHelper(halfEdge2.getPrev().getOrigin());
            d.setHelper(halfEdge2.getNext().getOrigin());
            d.repaint();
            Thread.sleep(3000);*/

            Vertex vertex1 = copyVertex(vertex);
            Vertex helper1 = copyVertex(helper);

            HalfEdge halfEdge1 = new HalfEdge(vertex, helper, vertex.getPrev(), helper.getNext());

            vertex.getPrev().setNext(halfEdge1);
            helper.getNext().setPrev(halfEdge1);

            vertex.setNext(halfEdge1);
            helper.setPrev(halfEdge1);

            HalfEdge halfEdge2 = new HalfEdge(helper1, vertex1, helper1.getPrev(), vertex1.getNext());

            helper1.getPrev().setNext(halfEdge2);
            vertex1.getNext().setPrev(halfEdge2);

            helper1.setNext(halfEdge2);
            vertex1.setPrev(halfEdge2);

            all.add(halfEdge1);
            all.add(halfEdge2);
        }

        /** Вычленение полигонов */
        while (! all.isEmpty()) {
            HalfEdge edge1 = all.get(0);

            all.remove(edge1);

            HalfEdge next1 = edge1.getNext();
            while (next1 != edge1) {
                all.remove(next1);
                next1 = next1.getNext();
            }

            result.add(edge1);
        }

        return result;
    }

    public static Vertex copyVertex(Vertex vertex) {
        Vertex result = vertex.copy();
        HalfEdge vp = vertex.getPrev();
        HalfEdge vn = vertex.getNext();

        result.setPrev(vp);
        result.setNext(vn);

        return result;

    }
}
