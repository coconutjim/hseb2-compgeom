import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by Lev on 08.04.14.
 */
public class Triangulation {

    /** Общая панель отрисовки */
    final private DrawPanel drawPanel;

    /** Строка состояния */
    final private JLabel labelProcess;

    /** Задержка отображения */
    final private int delay;

    public Triangulation(DrawPanel drawPanel, JLabel labelProcess, int delay) {
        this.drawPanel = drawPanel;
        this.labelProcess = labelProcess;
        this.delay = delay;
    }

    public void triangulateMonotonePolygon(HalfEdge edge) throws InterruptedException {

        labelProcess.setText("Триангуляция");
        Thread.sleep(delay);

        /** Определение вертексов */
        ArrayList<Vertex> eps = new ArrayList<Vertex>();

        eps.add(edge.getOrigin());
        HalfEdge next = edge.getNext();
        while (next != edge) {
            eps.add(next.getOrigin());

            next = next.getNext();
        }
        Collections.sort(eps);

        /** Присваивание цепочек */
        for (int i = 1; i < eps.size() - 1; ++ i) {
            Vertex vertex = eps.get(i);
            vertex.setLeft(vertex.getY() > vertex.getNext().getEnd().getY());
        }

        /** Диагонали */
        ArrayList<HalfEdge> diagonals = new ArrayList<HalfEdge>();

        /** Стек */
        Stack<Vertex> stack = new Stack<Vertex>();

        /** Втолкнуть два первых */
        stack.push(eps.get(0));
        stack.push(eps.get(1));

        for (int i = 2; i < eps.size() - 1; ++ i) {
            Vertex vertex = eps.get(i);
            Vertex top = stack.peek();

            /** Если разные цепочки */
            if (vertex.isLeft() != top.isLeft()) {

                labelProcess.setText("На разных цепочках");
                drawPanel.setHelper(vertex);
                drawPanel.setPrevHelper(top);
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setPrevHelper(null);


                /** Вытолкнуть и добавить диагонали ко всем, кроме последнего */
                while (stack.size() > 1) {
                    Vertex vertex1 = stack.pop();
                    diagonals.add(new HalfEdge(vertex, vertex1, null, null));
                    drawPanel.addDiagonal(new HalfEdge(vertex, vertex1, null, null));

                    labelProcess.setText("Добавление диагонали");
                    repaint(drawPanel);
                    Thread.sleep(delay);

                }
                stack.pop();

                /** Вставить предыдущий и этот */
                stack.push(eps.get(i - 1));
                stack.push(vertex);
            }
            /** Если нет */
            else {

                labelProcess.setText("На одной цепочке");
                drawPanel.setHelper(vertex);
                drawPanel.setPrevHelper(top);
                repaint(drawPanel);
                Thread.sleep(delay);
                drawPanel.setPrevHelper(null);

                /** Вытолкннуть один */
                Vertex last = stack.pop();

                top = stack.peek();
                /** Пока можем проводить диагонали (определяется с помощью в.п.), учитывается цепочка */
                while ( ( vertex.isLeft() ^ new Vector(vertex, last).composite(new Vector(vertex, top)) > 0) &&
                        new Vector(vertex, last).composite(new Vector(vertex, top)) != 0) {
                    diagonals.add(new HalfEdge(vertex, top, null, null));
                    drawPanel.addDiagonal(new HalfEdge(vertex, top, null, null));

                    labelProcess.setText("Добавление диагонали");
                    repaint(drawPanel);
                    Thread.sleep(delay);

                    last = stack.pop();
                    if (! stack.isEmpty()) {
                        top = stack.peek();
                    }
                }
                /** Втолкнуть последний */
                stack.push(last);

                /** Втолкнуть текущий */
                stack.push(vertex);
            }
        }

        /** Добавить диагонали от последнего к вертексам стека*/
        Vertex end = eps.get(eps.size() - 1);

        labelProcess.setText("Последний вертекс");
        drawPanel.setHelper(end);
        repaint(drawPanel);
        Thread.sleep(delay);

        stack.pop();
        while (stack.size() > 1) {
            Vertex pop = stack.pop();
            diagonals.add(new HalfEdge(end, pop, null, null));
            drawPanel.addDiagonal(new HalfEdge(end, pop, null, null));

            labelProcess.setText("Добавление диагонали");
            repaint(drawPanel);
            Thread.sleep(delay);

        }

        stack.clear();

        //return diagonals;//HalfEdge.split(edge, diagonals);
    }

    public void repaint(DrawPanel panel) {
        panel.setPainted(false);
        panel.repaint();
        while (! panel.isPainted()) { }
    }
}
