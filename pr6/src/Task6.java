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
import java.util.Random;

/**
 * Форма и кнопки
 */
public class Task6 extends JFrame {

    /** Список отрезков */
    private ArrayList<Segment> list;

    /** Панель отрисовки */
    final private DrawPanel drawPanel = new DrawPanel();

    /** Панель легенды */
    final private LegendPanel legendPanel = new LegendPanel();

    /** Поток выполнения и отрисовки алгоритма */
    private Thread graham;

    /** Первая точка */
    private Point first;

    /** Сетка */
    private boolean grid;

    final private JTextField textCount = new JTextField();
    final private JButton buttonGen = new JButton("Сгенерировать новые отрезки");
    final private JTextField textDelay = new JTextField();
    final private JButton buttonStart = new JButton("Начать поиск");
    final private JButton buttonClear = new JButton("Очистить");
    final private JButton buttonEnd = new JButton("Отменить");
    final private JRadioButton radioButtonGrid = new JRadioButton("Сетка");
    final private JLabel labelProcess = new JLabel();
    final private JLabel labelInfo = new JLabel("Состояния:");

    public Task6() {
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

        JLabel labelCount = new JLabel("Введите количество отрезков (от 1 до 30, визуализация доступна при <= 10): ");
        labelCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelGUI.add(labelCount);


        textCount.setMaximumSize(new Dimension(100, 20));
        textCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelGUI.add(textCount);

        /** Кнопка генерации новых точек */
        buttonGen.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                drawPanel.setNULL();
                int count;

                if (textCount.getBackground() != Color.WHITE) {
                    textCount.setBackground(Color.WHITE);
                }
                try {
                    count = Integer.parseInt(textCount.getText());
                    if (count < 1 || count > 30) {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException ex) {
                    textCount.setBackground(Color.PINK);
                    return;
                }

                list = generateSegments(count);


                drawPanel.setAllSegMents(list);
                drawPanel.repaint();

            }
        });
        panelGUI.add(buttonGen);

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
        textCount.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(textDelay);

        /** Кнопка запуска алгоритма */
        buttonStart.setAlignmentX(CENTER_ALIGNMENT);
        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (list == null) {
                    JOptionPane.showMessageDialog(null, "Отрезков нет!");
                    return;
                }

                if (textCount.getBackground() != Color.WHITE) {
                    textCount.setBackground(Color.WHITE);
                }
                textCount.setText(Integer.toString(list.size()));

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
                        drawPanel.repaint();

                        /** Алгоритм */
                        try {
                            AllIntersections alg = new AllIntersections();
                            ArrayList<IntersectingSegments> result = alg.findAllIntersections(list,
                                    drawPanel, legendPanel, labelProcess, delay);
                            drawPanel.setNULL();
                            drawPanel.repaint();
                            if (result.size() != 0) {
                                drawPanel.setIntersectingSegments(result);
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Пересечений нет!");
                            }
                        }
                        catch (InterruptedException e1) {
                            // Interrupted
                        }
                        catch (Throwable e2) {
                            e2.printStackTrace();
                        }

                        legendPanel.setSegments(null);
                        legendPanel.repaint();

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
                    if (first == null) {
                        first = new Point(x, DrawPanel.getSIZE() - y);
                        drawPanel.setFirst(first);
                        drawPanel.repaint();
                    }
                    /** Если вторая точка */
                    else {
                        /** Проверка на точку */
                        if (first.getX() == x && first.getY() == DrawPanel.getSIZE() - y) {
                            JOptionPane.showMessageDialog(null, "Такой отрезок недопустим!");
                            clearFirstPoint();
                            return;
                        }

                        Segment segment = new Segment(first, new Point(x, DrawPanel.getSIZE() - y));
                        /** Проверка на наличие такого же отрезка*/
                        if (list != null) {
                            for (Segment s : list) {
                                if (s.equals(segment)) {
                                    JOptionPane.showMessageDialog(null, "Такой отрезок уже есть!");
                                    clearFirstPoint();
                                    return;
                                }
                            }
                        } else {
                            list = new ArrayList<Segment>();
                        }

                        /** Проверка на количество отрезков */
                        if (list.size() >= 30) {
                            JOptionPane.showMessageDialog(null, "Максимальное количество отрезков - 30!");
                            clearFirstPoint();
                            return;
                        }

                        list.add(segment);
                        clearFirstPoint();
                        drawPanel.setAllSegMents(list);
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


        /** Строка состояния алгоритма */
        labelProcess.setFont(new Font("Times New Roman", Font.ITALIC, 25));
        labelProcess.setAlignmentX(CENTER_ALIGNMENT);
        labelProcess.setBorder(BorderFactory.createLineBorder(Color.black));
        panelGUI.add(labelProcess);
        labelProcess.setVisible(false);

        labelInfo.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(labelInfo);
        labelInfo.setVisible(false);

        /** Панель легенды */
        legendPanel.setAlignmentX(CENTER_ALIGNMENT);
        panelGUI.add(legendPanel);



        /** Изначальные действия */
        buttonEnd.setEnabled(false);
        //radioButtonGrid.setVisible(false);
        textCount.setText("4");
        textDelay.setText("2000");
        drawPanel.setNULL();
        list = generateSegments(Integer.parseInt(textCount.getText()));
        drawPanel.setAllSegMents(list);
        drawPanel.repaint();



    }

    /** Генерация отрезков */
    private ArrayList<Segment> generateSegments(int count) {
        ArrayList<Segment> segments = new ArrayList<Segment>();
        Random random = new Random();
        for (int i = 0; i < count;) {
            Point x1 = new Point(random.nextInt(DrawPanel.getSIZE() + 1), random.nextInt(DrawPanel.getSIZE() + 1));
            Point x2 = new Point(random.nextInt(DrawPanel.getSIZE() + 1), random.nextInt(DrawPanel.getSIZE() + 1));

            /** Проверка на точку */
            if (x1.equals(x2)) {
                continue;
            }

            Segment newSegment = new Segment(x1, x2);
            /** Проверка на совпадение */
            boolean flag = false;
            for (Segment segment : segments) {
                if (segment.equals(newSegment)) {
                    flag = true;
                }
            }
            if (flag) {
                continue;
            }

            segments.add(newSegment);
            ++ i;
        }
        return segments;
    }

    /** Доступность гуи */
    private void enableComponent(boolean flag) {
        textCount.setEnabled(flag && ! grid);
        textDelay.setEnabled(flag);
        buttonGen.setEnabled(flag && ! grid);
        drawPanel.setEnabled(flag);
        buttonStart.setEnabled(flag);
        buttonClear.setEnabled(flag);
        radioButtonGrid.setEnabled(flag);
        buttonEnd.setEnabled(! flag);
        labelProcess.setVisible(! flag);
        labelProcess.setText("");
        labelInfo.setVisible(! flag);
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
        list = null;
        first = null;
        drawPanel.setAllSegMents(null);
        legendPanel.setSegments(null);
        legendPanel.repaint();
        drawPanel.repaint();
    }

    /** Сетка */
    private void setGrid(boolean flag) {
        grid = flag;
        drawPanel.setGrid(flag);
        clear();

        textCount.setEnabled(! flag);
        buttonGen.setEnabled(! flag);
    }

    public static void main(String[] args) {
        /** Запуск */
        Task6 f = new Task6();
        f.setVisible(true);
        f.setResizable(false);
        f.setTitle("Поиск всех пересечений отрезков. Осипов Лев 271ПИ НИУ ВШЭ");
    }
}

