/**
 * Created by Lev on 08.03.14.
 */
public class Vertex extends Point  implements Comparable<Vertex>{

    /** Тип вертекса */
    private int type;

    /** Инцидентное ребро */
    private HalfEdge next;

    /** Предыдущее ребро */
    private HalfEdge prev;

    /** Какая цепочка */
    private boolean left;

    /** Константы */
    final public static int VERTEX_START = 0;
    final public static int VERTEX_MERGE = 1;
    final public static int VERTEX_SPLIT = 2;
    final public static int VERTEX_END = 3;
    final public static int VERTEX_REGULAR = 4;


    public Vertex(double x, double y) {
        super(x, y);
        this.type = -1;
    }

    public Vertex(double x, double y, int type) {
        super(x, y);
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    public void setPrev(HalfEdge prev) {
        this.prev = prev;
    }

    public int getType() {
        return type;
    }

    public HalfEdge getNext() {
        return next;
    }

    public HalfEdge getPrev() {
        return prev;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    /** Сравнение */
    @Override
    public int compareTo(Vertex op) {

        if (this == op) {
            return 0;
        }

        if (getY() < op.getY()) {
            return 1;
        } else if (getY() > op.getY()) {
            return -1;
        /** Если когоризонтальны */
        } else if (getX() > op.getX()) {
            return 1;
        } else if (getX() < op.getX()) {
            return -1;
        } else {
            return 0;
        }
    }

    public Vertex copy() {
        return new Vertex(getX(), getY(), getType());
    }

}
