/**
 * Created by Lev on 09.04.14.
 */
/**
 * Моделирует вектор.
 */
public class Vector extends Segment {
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
        super(start, end);
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

