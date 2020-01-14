/**
 * Created by Lev on 02.02.14.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Форма и кнопки
 */
public class CFrame extends JFrame {
    /** Список точек */
    private ArrayList<Point> list;

    /** Панель отрисовки */
    DrawPanel p = new DrawPanel();

    /** Поток выполнения и отрисовки алгоритма */
    Thread graham;

    final JTextField textCount = new JTextField();
    final JButton buttonGen = new JButton("Сгенерировать новые точки");
    final JTextField textDelay = new JTextField();
    final JButton buttonStart = new JButton("Начать построение");
    final JButton buttonEnd = new JButton("Отменить");

    public CFrame() {
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

        p = new DrawPanel();
        p.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
        add(p);


        JPanel panel = new JPanel();
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

                list = GrahamAlgorithm.generatePoints(count);


                p.setAllPoints(list);
                p.validate();
                p.repaint();

            }
        });
        panel.add(buttonGen);


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
                graham = new Thread() {
                    @Override
                    public void run() {
                        buttonStart.setEnabled(false);
                        textCount.setEnabled(false);
                        textDelay.setEnabled(false);
                        buttonGen.setEnabled(false);
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
                            return;
                        }

                        GrahamAlgorithm alg = new GrahamAlgorithm();
                        ArrayList<Point> tempList = new ArrayList<Point>(list);
                        StackG<Point> sol = alg.scanGraham(tempList, p, delay);



                        textCount.setEnabled(true);
                        textDelay.setEnabled(true);
                        buttonGen.setEnabled(true);
                        buttonStart.setEnabled(true);
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
                buttonStart.setEnabled(true);
            }
        });
        panel.add(buttonEnd);


        /** Изначальные действия */
        buttonEnd.setEnabled(false);
        textCount.setText("100");
        textDelay.setText("30");
        p.setPoints(null);
        p.setEnd(false);
        list = GrahamAlgorithm.generatePoints(Integer.parseInt(textCount.getText()));
        p.setAllPoints(list);
        p.validate();
        p.repaint();



    }
}
