import sun.awt.TracedEventQueue;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Lev on 25.02.14.
 */
public class LegendPanel extends JPanel{


    LegendPanel() {
        setBackground(Color.LIGHT_GRAY);
    }

    /** Отрезки */
    private TreeSet<Segment> segments;


    public void setSegments(TreeSet<Segment> segments) {
        this.segments = segments;
    }

    /** Рисует легенду */
    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        /** Отрисовка легенды */
         if (segments != null) {
             /** Список нужно перевернуть */
            int index = 0;
            for (Segment segment : segments) {
                g.setColor(segment.getColor());
                g.fillRect(getWidth() / 4, index += 20, getWidth() / 2, 10);
            }
        }
    }

    /** Возращает 10 цветов */
    public static  ArrayList<Color> getColors() {
        ArrayList<Color> colors = new ArrayList<Color>();
        colors.add(new Color(139, 0, 0));
        colors.add(new Color(148, 0, 211));
        colors.add(new Color(255, 0, 153));
        colors.add(new Color(255, 255, 0));
        colors.add(new Color(0, 255, 0));
        colors.add(new Color(153, 153, 102));
        colors.add(new Color(47, 79, 79));
        colors.add(new Color(0, 0, 255));
        colors.add(new Color(169, 69, 19));
        colors.add(new Color(47, 255, 255));

        Collections.shuffle(colors);

        return colors;
    }


    /** Возвращает 10 случайных цветов */
    public static ArrayList<Color> getRandomColors() {
        ArrayList<Color> colors = new ArrayList<Color>();
        Random random = new Random();
        for (int i = 0; i < 10; ++ i) {
            colors.add(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        }
        return colors;
    }
}