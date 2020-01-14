/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Форма и кнопки
 */
public class Task8 extends JFrame {

    /** Первый отрезок */
    private HalfEdge firstEdge;

    /** Временные отрезки */
    private HalfEdge tempHalfEdge;

    /** Готовый полигон */
    private HalfEdge edge;

    /** Панель отрисовки */
    final private DrawPanel drawPanel = new DrawPanel();

    /** Поток выполнения и отрисовки алгоритма */
    private Thread graham;

    /** Первая точка */
    private Vertex first;

    /** Сетка */
    private boolean grid;

    final private JTextField textDelay = new JTextField();
    final private JButton buttonStart = new JButton("Начать поиск");
    final private JButton buttonClear = new JButton("Очистить");
    final private JButton buttonEnd = new JButton("Отменить");
    final private JRadioButton radioButtonGrid = new JRadioButton("Сетка");
    final private JRadioButton radioButtonTriangle = new JRadioButton("Триангуляция");
    final private JLabel labelProcess = new JLabel();


    public Task8() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int w = d.width;
        int h = d.height;
        setSize(3 * w / 4, 3 * h / 4);
        setLocation(w / 8, h / 8);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createGUI();
    }
    private void createGUI() {
        setLayout(new GridLayout(0, 2));

        drawPanel.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
        add(drawPanel);


        final JPanel panelGUI = new JPanel();
        panelGUI.setBackground(Color.LIGHT_GRAY);
        panelGUI.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight()));
        add(panelGUI);

        panelGUI.setLayout(new BoxLayout(panelGUI, BoxLayout.Y_AXIS));


        /** Кнопка очистки */
        buttonClear.setAlignmentX(CENTER_ALIGNMENT);
        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        panelGUI.add(buttonClear);

        JLabel labelDelay = new JLabel("Задайте задержку в мс (от 0 до 3000): ");
        labelDelay.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(labelDelay);

        textDelay.setMaximumSize(new Dimension(100, 20));
        textDelay.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(textDelay);

        /** Кнопка запуска алгоритма */
        buttonStart.setAlignmentX(CENTER_ALIGNMENT);
        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (! (firstEdge == null && first == null && tempHalfEdge == null && edge != null)) {
                    JOptionPane.showMessageDialog(null, "Нужен один целый полигон!");
                    return;
                }

                graham = new Thread() {
                    @Override
                    public void run() {
                        enableComponent(false);
                        while (buttonStart.isEnabled()) { }

                        int delay;

                        if (textDelay.getBackground() != Color.WHITE) {
                            textDelay.setBackground(Color.WHITE);
                        }
                        try {
                            delay = Integer.parseInt(textDelay.getText());
                            if (delay < 0 || delay > 3000) {
                                throw new NumberFormatException();
                            }
                        }
                        catch (NumberFormatException ex) {
                            textDelay.setBackground(Color.PINK);
                            enableComponent(true);
                            return;
                        }

                        drawPanel.setNULL();
                        drawPanel.setEdges(null);
                        drawPanel.setDiagonals(null);
                        drawPanel.addEdge(edge);
                        drawPanel.repaint();

                        /** Алгоритм */
                        try {
                            /** Монотонность */
                            PolygonMonotone alg1 = new PolygonMonotone(drawPanel,
                                    labelProcess, delay, ! radioButtonTriangle.isSelected());
                            ArrayList<HalfEdge> result = alg1.makeMonotone(HalfEdge.copyData(edge));
                            drawPanel.setNULL();
                            drawPanel.setEdges(result);
                            drawPanel.setDiagonals(null);
                            drawPanel.repaint();
                            if (radioButtonTriangle.isSelected()) {
                                ArrayList<HalfEdge> triangles = new ArrayList<HalfEdge>();
                                Triangulation alg2 = new Triangulation(drawPanel, labelProcess, delay);
                                for (HalfEdge halfEdge : result) {
                                    alg2.triangulateMonotonePolygon(HalfEdge.copyData(halfEdge));
                                    drawPanel.setNULL();
                                    drawPanel.repaint();
                                }

                            }

                        }
                        catch (InterruptedException e1) {
                            // Interrupted
                        }
                        catch (Throwable e2) {
                            e2.printStackTrace();
                        }


                        enableComponent(true);
                    }
                };
                graham.start();
            }
        });
        panelGUI.add(buttonStart);

        /** Кнопка отмены выполнения */
        buttonEnd.setAlignmentX(CENTER_ALIGNMENT);
        buttonEnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graham.interrupt();
                try {
                    graham.join();
                }
                catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panelGUI.add(buttonEnd);


        /** Добавление точек по клику */
        drawPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                /** Если не идет процесс выполнения и в пределах поля отрисовки */
                if (drawPanel.isEnabled() && e.getX() <= DrawPanel.getSIZE() && e.getY() <= DrawPanel.getSIZE() &&
                        e.getX() >= 0 && e.getY() >= 0) {


                    int x = e.getX();
                    int y = e.getY();

                    /** Если сетка, нужно сдвинуть координаты */
                    if (grid) {
                        int distance = DrawPanel.getSIZE() / DrawPanel.getGridRowCount();
                        x = x % distance < distance / 2 ? x / distance * distance : (x / distance + 1) * distance;
                        y = y % distance < distance / 2 ? y / distance * distance : (y / distance + 1) * distance;
                    }


                    /** Если первая точка*/
                    if (first == null && tempHalfEdge == null) {
                        drawPanel.setNULL();
                        drawPanel.setEdges(null);
                        drawPanel.setDiagonals(null);
                        edge = null;
                        first = new Vertex(x, DrawPanel.getSIZE() - y);
                        drawPanel.setFirst(first);
                        drawPanel.repaint();

                        drawPanel.setFirst(first);
                        drawPanel.repaint();
                    }
                    /** Если вторая точка */
                    else {
                        Vertex newVertex = new Vertex(x, DrawPanel.getSIZE() - y);
                        /** Проверка на ту же точку */
                        if (newVertex.equals(first)) {
                            return;
                        }
                        /** Если еще нет полуребер */
                        if (tempHalfEdge == null) {
                            tempHalfEdge = new HalfEdge(first, newVertex, null, null);
                            firstEdge = tempHalfEdge;
                            firstEdge.getOrigin().setNext(firstEdge);

                            clearFirstPoint();
                        }
                        /** Если уже есть полуребра  */
                        else {
                            /** Проверка на законченность полигона */
                            if ( new Segment(firstEdge.getOrigin(), newVertex).length() < 10) {


                                newVertex = firstEdge.getOrigin();

                                HalfEdge temp = tempHalfEdge;
                                tempHalfEdge = new HalfEdge(first, newVertex, temp, firstEdge);

                                temp.setNext(tempHalfEdge);
                                firstEdge.setPrev(tempHalfEdge);

                                tempHalfEdge.getOrigin().setNext(tempHalfEdge);
                                tempHalfEdge.getOrigin().setPrev(temp);
                                firstEdge.getOrigin().setPrev(tempHalfEdge);

                                edge = tempHalfEdge;
                                drawPanel.addEdge(edge);
                                tempHalfEdge = null;
                                firstEdge = null;
                                first = null;

                                drawPanel.setNULL();
                                drawPanel.repaint();

                                return;


                            }
                            /** Продолжение рисования */
                            else {
                                HalfEdge temp = tempHalfEdge;
                                tempHalfEdge = new HalfEdge(first, newVertex, temp, null);
                                temp.setNext(tempHalfEdge);
                                tempHalfEdge.getOrigin().setNext(tempHalfEdge);
                                tempHalfEdge.getOrigin().setPrev(temp);

                            }

                        }

                        first = newVertex;
                        drawPanel.setTempEdge(tempHalfEdge);
                        drawPanel.repaint();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        radioButtonGrid.setAlignmentX(CENTER_ALIGNMENT);
        radioButtonGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGrid(radioButtonGrid.isSelected());
            }
        });
        panelGUI.add(radioButtonGrid);


        radioButtonTriangle.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(radioButtonTriangle);

        /** Строка состояния алгоритма */
        labelProcess.setFont(new Font("Times New Roman", Font.ITALIC, 25));
        labelProcess.setAlignmentX(CENTER_ALIGNMENT);
        labelProcess.setBorder(BorderFactory.createLineBorder(Color.black));
        panelGUI.add(labelProcess);
        labelProcess.setVisible(false);





        /** Изначальные действия */
        buttonEnd.setEnabled(false);
        radioButtonGrid.setVisible(false);
        textDelay.setText("000");
        drawPanel.setNULL();
        drawPanel.repaint();



    }

    /** Доступность гуи */
    private void enableComponent(boolean flag) {
        textDelay.setEnabled(flag);
        drawPanel.setEnabled(flag);
        buttonStart.setEnabled(flag);
        buttonClear.setEnabled(flag);
        radioButtonGrid.setEnabled(flag);
        radioButtonTriangle.setEnabled(flag);
        buttonEnd.setEnabled(! flag);
        labelProcess.setVisible(! flag);
        labelProcess.setText("");
    }

    /** Очистка первой точки */
    private void clearFirstPoint() {
        first = null;
        drawPanel.setFirst(null);
        drawPanel.repaint();
    }

    /** Очистка панели */
    private void clear() {
        drawPanel.setNULL();
        drawPanel.setEdges(null);
        drawPanel.setDiagonals(null);
        first = null;
        tempHalfEdge = null;
        drawPanel.repaint();
    }

    /** Сетка */
    private void setGrid(boolean flag) {
        grid = flag;
        drawPanel.setGrid(flag);
        clear();

    }

    public static void main(String[] args) {
        /** Запуск */
        Task8 f = new Task8();
        f.setVisible(true);
        f.setResizable(false);
        f.setTitle("Монотонизация и триангуляция полигонов. Осипов Лев 271ПИ НИУ ВШЭ");
    }
}

