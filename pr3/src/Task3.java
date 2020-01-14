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
import java.util.Stack;

/**
 * Форма и кнопки
 */
public class Task3 extends JFrame {
    /** Список точек */
    private ArrayList<Point> list;

    /** Панель отрисовки */
    final DrawPanel p = new DrawPanel();

    /** Поток выполнения и отрисовки алгоритма */
    Thread graham;

    final JTextField textCount = new JTextField();
    final JButton buttonGen = new JButton("Сгенерировать новые точки");
    final JTextField textDelay = new JTextField();
    final JButton buttonStart = new JButton("Начать построение");
    final JButton buttonClear = new JButton("Очистить");
    final JButton buttonEnd = new JButton("Отменить");

    public Task3() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int w = d.width;
        int h = d.height;
        setSize(w / 2, h / 2);
        setLocation(w / 4, h / 4);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createGUI();
    }
    void createGUI() {
        setLayout(new GridLayout(0, 2));

        p.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
        add(p);


        final JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight()));
        this.add(panel);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel labelCount = new JLabel("Введите количество точек (от 3 до 1000): ");
        labelCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelCount);


        textCount.setMaximumSize(new Dimension(100, 20));
        textCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(textCount);

        /** Кнопка генерации новых точек */
        buttonGen.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setPoints(null);
                p.setEnd(false);

                int count;

                if (textCount.getBackground() != Color.WHITE) {
                    textCount.setBackground(Color.WHITE);
                }
                try {
                    count = Integer.parseInt(textCount.getText());
                    if (count < 3 || count > 1000) {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException ex) {
                    textCount.setBackground(Color.PINK);
                    return;
                }

                list = JarvisAlgorithm.generatePoints(count);


                p.setAllPoints(list);
                p.repaint();

            }
        });
        panel.add(buttonGen);

        /** Кнопка генерации новых точек */
        buttonClear.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setPoints(null);
                p.setEnd(false);

                list = null;


                p.setAllPoints(list);
                p.repaint();

            }
        });
        panel.add(buttonClear);

        JLabel labelDelay = new JLabel("Задайте задержку в мс (от 0 до 1000): ");
        labelDelay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelDelay);

        textDelay.setMaximumSize(new Dimension(100, 20));
        textCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(textDelay);

        /** Кнопка запуска алгоритма */
        buttonStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (list == null) {
                    JOptionPane.showMessageDialog(null, "Точек нет!");
                    return;
                }
                graham = new Thread() {
                    @Override
                    public void run() {
                        buttonStart.setEnabled(false);
                        textCount.setEnabled(false);
                        textDelay.setEnabled(false);
                        buttonGen.setEnabled(false);
                        buttonClear.setEnabled(false);
                        p.setEnabled(false);
                        buttonEnd.setEnabled(true);
                        p.setEnd(false);
                        while (buttonStart.isEnabled()) { }

                        int delay;

                        if (textDelay.getBackground() != Color.WHITE) {
                            textDelay.setBackground(Color.WHITE);
                        }
                        try {
                            delay = Integer.parseInt(textDelay.getText());
                            if (delay < 0 || delay > 1000) {
                                throw new NumberFormatException();
                            }
                        }
                        catch (NumberFormatException ex) {
                            textDelay.setBackground(Color.PINK);
                            textCount.setEnabled(true);
                            textDelay.setEnabled(true);
                            buttonGen.setEnabled(true);
                            p.setEnabled(true);
                            buttonStart.setEnabled(true);
                            buttonClear.setEnabled(true);
                            buttonEnd.setEnabled(false);
                            return;
                        }

                        JarvisAlgorithm alg = new JarvisAlgorithm();
                        ArrayList<Point> tempList = new ArrayList<Point>(list);
                        Stack<Point> sol = alg.scanJarvis(tempList, p, delay);



                        textCount.setEnabled(true);
                        textDelay.setEnabled(true);
                        buttonGen.setEnabled(true);
                        p.setEnabled(true);
                        buttonStart.setEnabled(true);
                        buttonClear.setEnabled(true);
                        buttonEnd.setEnabled(false);
                    }
                };
                graham.start();
            }
        });
        panel.add(buttonStart);

        /** Кнопка отмены выполнения */
        buttonEnd.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonEnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graham.interrupt();
                try {
                    graham.join();
                }
                catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
                textCount.setEnabled(true);
                textDelay.setEnabled(true);
                buttonGen.setEnabled(true);
                buttonClear.setEnabled(true);
                buttonStart.setEnabled(true);
                p.setEnabled(true);

            }
        });
        panel.add(buttonEnd);


        /** Добавление точек по клику */
        p.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                /** Если не идет процесс выполнения и в пределах поля отрисовки */
                if (p.isEnabled() && e.getX() <= DrawPanel.getSIZE() && e.getY() <= DrawPanel.getSIZE() &&
                        e.getX() >= 0 && e.getY() >= 0) {
                    if (list == null) {
                         list = new ArrayList<Point>();
                    }
                    list.add(new Point(e.getX(), 300 - e.getY()));
                    p.setAllPoints(list);
                    p.repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        /** Изначальные действия */
        buttonEnd.setEnabled(false);
        textCount.setText("100");
        textDelay.setText("30");
        p.setPoints(null);
        p.setEnd(false);
        list = JarvisAlgorithm.generatePoints(Integer.parseInt(textCount.getText()));
        p.setAllPoints(list);
        p.validate();
        p.repaint();



    }

    public static void main(String[] args) {
        /** Запуск */
        Task3 f = new Task3();
        f.setVisible(true);
        f.setResizable(false);
        f.setTitle("Построение выпуклой оболочки по алгоритму Джарвиса. Осипов Лев 271ПИ НИУ ВШЭ");
    }
}
