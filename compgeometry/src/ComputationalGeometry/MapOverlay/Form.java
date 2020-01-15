package ComputationalGeometry.MapOverlay;

import sun.awt.TracedEventQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Form extends JFrame {
    private boolean isPressed = false;
    private Vertex prev = null;
    private Vertex first = null;
    private HalfEdge prevSeg = null;
    private HalfEdge firstSeg = null;

    private JMenuBar Bar = new JMenuBar();
    private JMenu Menu = new JMenu();
    private JMenuItem FindItem = new JMenuItem();
    private JMenuItem ClearItem = new JMenuItem();
    private ArrayList<Vertex> points = new ArrayList<Vertex>();
    private ArrayList<Vertex> result = new ArrayList<Vertex>();
    private ArrayList<Face> faces = new ArrayList<Face>();
    private ArrayList<HalfEdge> loops = new ArrayList<HalfEdge>();
    private JPanel Panel = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < points.size(); i ++) {
                g2.fill(new Ellipse2D.Double(points.get(i).getX() - 3, points.get(i).getY() - 3, 6, 6));
                try {
                    Vertex p1 = points.get(i).incidentEdge.get(0);
                    Vertex p2 = points.get(i).getIncidentEdge().get(1);
                    g2.draw(new Line2D.Double(p1, p2));
                } catch (Exception e) {}
            }
            if (!faces.isEmpty()) {
                ArrayList<Face> tempFace = new ArrayList<Face>(faces);
                ArrayList<Face> interSeg = new ArrayList<Face>();
                for (int i = 0; i < tempFace.size(); i ++) {
                    g2.setColor(new Color((i+1)*40, (i+1)*40, (i+1)*40));
                    Face f = tempFace.get(i);
                    if (f.parents.size() > 0) {
                        i --;
                        tempFace.remove(f);
                        interSeg.add(f);
                        continue;
                    }
                    Polygon p = new Polygon();
                    ArrayList<Vertex> polygPoints = new ArrayList<Vertex>();
                    HalfEdge temp = f.getOuterComponent();
                    do {
                        polygPoints.add(temp.origin);
                        temp = temp.getNext();
                    } while (temp != f.getOuterComponent());
                    for (Vertex point : polygPoints) {
                        p.addPoint(point.x, point.y);
                    }
                    g2.fill(p);
                }
                for (int i = 0; i < interSeg.size(); i ++) {
                    g2.setColor(new Color((i+1)*40, (i+1)*40, (i+1)*40));
                    Face f = interSeg.get(i);
                    Polygon p = new Polygon();
                    ArrayList<Vertex> polygPoints = new ArrayList<Vertex>();
                    HalfEdge temp = f.getOuterComponent();
                    do {
                        polygPoints.add(temp.origin);
                        temp = temp.getNext();
                    } while (temp != f.getOuterComponent());
                    for (Vertex point : polygPoints) {
                        p.addPoint(point.x, point.y);
                    }
                    g2.fill(p);
//                    g2.setColor(Color.RED);
//                    g2.draw(new Line2D.Double(loop.get(0), loop.get(1)));
//                    g2.setColor(Color.GREEN);
//                    g2.fill(new Ellipse2D.Double(loop.getOrigin().x - 3, loop.getOrigin().y - 3, 6, 6));
                }
            }
//            for (HalfEdge loop : loops) {
//                g2.setColor(Color.RED);
//                g2.draw(new Line2D.Double(loop.get(0), loop.get(1)));
//                g2.setColor(Color.GREEN);
//                g2.fill(new Ellipse2D.Double(loop.getOrigin().x - 3, loop.getOrigin().y - 3, 6, 6));
//            }
        }
    };

    public Form() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Exercise 6");
        setSize(500, 500);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth - getWidth()) / 2, (screenHeight - getHeight()) / 2);
        Menu.setText("Menu");
        FindItem.setText("Find");
        FindItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Methods.mapOverlay(points, faces, loops);
                repaint();
                String res = "There are " + faces.size() + " faces\n";
                if (!faces.isEmpty()) {
                    for (int i = 0; i < faces.size(); i ++) {
                        res += "Face " + (i + 1) + ": incident edge is {("
                                + faces.get(i).getOuterComponent().getHigher().x + ", "
                                + faces.get(i).getOuterComponent().getHigher().y + "), ("
                                + faces.get(i).getOuterComponent().getLower().x + ", "
                                + faces.get(i).getOuterComponent().getLower().y + ")}\n";
                        if (!faces.get(i).parents.isEmpty()) {
                            res += "It's is intersections of " + faces.get(i).parents.size() + " faces\n";
                        }
                    }
                    JOptionPane.showMessageDialog(Bar, res, "Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Vertex curr = new Vertex(e.getPoint());
                if (!isPressed) { //нажимаем первый раз
                    isPressed = true;
                    first = curr;
                } else { //уже нажимали прежде
                    if (curr.x > first.x - 5 && curr.x < first.x + 5 && curr.y > first.y - 5 && curr.y < first.y + 5) {
                        //закончить цикл
                        HalfEdge temp = new HalfEdge(prev, first);
                        HalfEdge twin = new HalfEdge(null, null);  //плоскости задать
                        temp.origin = prev;
                        prev.setIncidentEdge(temp);
                        temp.twin = twin;
                        twin.twin = temp;
                        twin.origin = first;
                        temp.setPrev(prevSeg);
                        temp.setNext(firstSeg);
                        twin.setPrev(firstSeg.twin);
                        twin.setNext(prevSeg.twin);
                        prevSeg.twin.setPrev(twin);
                        prevSeg.setNext(temp);
                        firstSeg.setPrev(temp);
                        firstSeg.twin.setNext(twin);
                        loops.add(temp);
                        loops.add(twin); //стоит ли?
                        isPressed = false;
                        prev = null;
                        prevSeg = null;
                        first = null;
                        firstSeg = null;
                        repaint();
                        return;
                    } else {
                        HalfEdge temp = new HalfEdge(prev, curr);
                        HalfEdge twin = new HalfEdge(null, null);
                        temp.origin = prev;
                        prev.setIncidentEdge(temp);
                        twin.origin = curr;
                        temp.twin = twin;
                        twin.twin = temp;
                        if (firstSeg == null) {
                            firstSeg = temp;
                        } else {
                            temp.setPrev(prevSeg);
                            prevSeg.setNext(temp);
                            prevSeg.twin.setPrev(twin);
                            twin.setNext(prevSeg.twin);
                        }
                        prevSeg = temp;
                    }
                }
                prev = curr;
                points.add(curr);
                repaint();
            }
        });
        Menu.add(FindItem);
        ClearItem.setText("Clear");
        ClearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.clear();
                points.clear();
                faces.clear();
                loops.clear();
                isPressed = false;
                prev = null;
                first = null;
                prevSeg = null;
                firstSeg = null;
                repaint();
            }
        });
        Menu.add(ClearItem);
        Bar.add(Menu);
        setJMenuBar(Bar);
        add(Panel);
        setVisible(true);
    }

    public static void main (String[] args) {
        TracedEventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Form();
            }
        });
    }
}


class Methods {

    public static int direction (Point i, Point j, Point k) {
        return (k.x - i.x) * (j.y - i.y) - (j.x - i.x) * (k.y - i.y);
    }

    public static boolean on_segment (Point i, Point j, Point k) {
        return (Math.min(i.x, j.x) <= k.x) && (k.x <= Math.max(i.x, j.x))
                && (Math.min(i.y, j.y) <= k.y) && (k.y <= Math.max(i.y, j.y));
    }

    public static boolean segments_intersect (Point... points) {
        int d1 = direction(points[2], points[3], points[0]);
        int d2 = direction(points[2], points[3], points[1]);
        int d3 = direction(points[0], points[1], points[2]);
        int d4 = direction(points[0], points[1], points[3]);

        if ((((d1 > 0) && (d2 < 0)) || ((d1 < 0) && d2 > 0))
                && (((d3 > 0) && (d4 < 0)) || ((d3 < 0) && (d4 > 0)))) {
            return true;
        } else {
            if ((d1 == 0) && (on_segment(points[2], points[3], points[0]))) {
                return true;
            } else {
                if ((d2 == 0) && on_segment(points[2], points[3], points[1])) {
                    return true;
                } else {
                    if ((d3 == 0) && on_segment(points[0], points[1], points[2])) {
                        return true;
                    } else {
                        if ((d4 == 0) && on_segment(points[0], points[1], points[3])) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<Vertex> find_intersections (ArrayList<Vertex> list) {  //ex 5
        ArrayList<Vertex> points = new ArrayList<Vertex>(list); //for work
        ArrayList<Vertex> result = new ArrayList<Vertex>();    //array of intersection points
        ArrayList<Vertex> stack = new ArrayList<Vertex>();     //queue
        ArrayList<HalfEdge> halfEdges = new ArrayList<HalfEdge>();  //halfEdges in the queue
        ArrayList<HalfEdge> allHalfEdges = new ArrayList<HalfEdge>(); //all halfEdges
        Collections.sort(points, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return o1.y == o2.y ? o1.x - o2.x : o1.y - o2.y;
            }
        });
        for (Vertex p : list) {
            if (p.getSegment().isEmpty()) {
                points.remove(p);
                break;
            }
            for (HalfEdge s : p.getSegment()) {
                if (!allHalfEdges.contains(s)) {
                    allHalfEdges.add(s);
                    s.clearBelong();
                    p.clearInter();
                }
            }
        }
        int i = 0;
        do {
            Vertex temp = null;
            if (i < points.size()) {    //adding new point from the list
                temp = points.get(i);
                for (Vertex p : stack) {
                    if (p.x >= temp.x - 3 && p.x <= temp.x + 3 && p.y >= temp.y - 3 && p.y <= temp.y + 3) {  //inner
                        stack.remove(p);
                        break;
                    }
                }
                stack.add(temp);
                i ++;
                Collections.sort(stack, new Comparator<Vertex>() {
                    @Override
                    public int compare(Vertex o1, Vertex o2) {
                        return o1.y == o2.y ? o1.x - o2.x : o1.y - o2.y;
                    }
                });
            }
            if (temp != null) { //if added
                int oldSize = stack.size();
                while (true) {
                    handleEventPoint(stack, result, halfEdges, allHalfEdges); //handle point
                    if (stack.get(0) == temp) { //if start/end point
                        stack.remove(0);
                        break;
                    }
                    stack.remove(0);
                    if (stack.size() != oldSize - 1) {  //if new point added in method
                        Collections.sort(stack, new Comparator<Vertex>() {
                            @Override
                            public int compare(Vertex o1, Vertex o2) {
                                return o1.y == o2.y ? o1.x - o2.x : o1.y - o2.y;
                            }
                        });
                    }
                    if (result.size() > 100) { //for exceptions
                        System.out.print("Sorry...");
                        return result;
                    }
                }
            }
        } while (!stack.isEmpty() || i < points.size());
        for (Vertex p : points) {
            for (int j = 0; j < result.size(); j ++) {
                Vertex r = result.get(j);
                if (r.x >= p.x - 3 && r.x <= p.x + 3 && r.y >= p.y - 3 && r.y <= p.y + 3) {
                    result.remove(r);
                    j --;
                }
            }
        }
        return result;
    }

    public static void handleEventPoint (ArrayList<Vertex> stack, ArrayList<Vertex> result,
                                         ArrayList<HalfEdge> halfEdges, ArrayList<HalfEdge> allHalfEdges) {
        Vertex temp = stack.get(0); //current point
        ArrayList<HalfEdge> U = new ArrayList<HalfEdge>(); //started at this point
        for (HalfEdge s : allHalfEdges) {
            if (s.getHigher() == temp) {
                U.add(s);
            }
        }
        ArrayList<HalfEdge> L = new ArrayList<HalfEdge>();   //ended at this point
        ArrayList<HalfEdge> C = new ArrayList<HalfEdge>();   //halfEdges contains this point
        for (HalfEdge s : halfEdges) {
            if (s.getLower() == temp) {
                L.add(s);
                continue;
            }
            if (s.isBelongs(temp)) {
                C.add(s);
            }
        }

        ArrayList<HalfEdge> LUC = merger(L, U, C);
        if (LUC.size() > 1) { //intersection is found
            if (!(LUC.size() == 2 && (temp.getSegment().contains(LUC.get(0)) ||
                    temp.getSegment().contains(LUC.get(1))))) {
                addTo(temp, result);   //add to result
                //modifySegments(temp, modSegments);  //лучше после всего обработать
            }
        }
        for (HalfEdge s : merger(L, C)) { //delete halfEdges which finished at this point and contains this point
            halfEdges.remove(s);
        }
        //add halfEdges which started at this point and contains this point
        ArrayList<HalfEdge> UC = merger(U, C);
        ArrayList<HalfEdge> sorted = new ArrayList<HalfEdge>();
        if (!UC.isEmpty()) {
            ArrayList<Double> d1 = new ArrayList<Double>(), d2 = new ArrayList<Double>();
            //sorting by angle
            for (HalfEdge s : UC) {
                double angle = Math.atan2(s.getLower().y - temp.y, s.getLower().x - temp.x);
                d1.add(angle);
                d2.add(angle);
            }
            Collections.sort(d2);
            for (int i = d2.size() - 1; i >=0; i --) {
                sorted.add(UC.get(d1.indexOf(d2.get(i))));
            }
            int index = findIndex(halfEdges, sorted.get(0), temp); //find index to add
            for (int i = sorted.size() - 1; i >=0; i --) {
                halfEdges.add(index, sorted.get(i));
            }
        }
        if (merger(U, C).size() == 0) {  //if segment ended compare right and lift neighbours
            if (halfEdges.size() > 1) {
                int i = halfEdges.size() / 2;
                int j = halfEdges.size();
                while (true) {
                    if (i == halfEdges.size() - 1 || i == 0) { //if end of start
                        break;
                    }
                    if (halfEdges.get(i).getX(temp) < temp.x) { //check this segment
                        if (halfEdges.get(i + 1).getX(temp) >= temp.x) { //check neighbour
                            if (segments_intersect(halfEdges.get(i).get(0), halfEdges.get(i).get(1),
                                    halfEdges.get(i + 1).get(0), halfEdges.get(i + 1).get(1)) &&
                                    (!temp.getSegment().contains(halfEdges.get(i)) ||
                                            !temp.getSegment().contains(halfEdges.get(i + 1)))) {
                                findNewEvent(halfEdges.get(i), halfEdges.get(i + 1), result);
                            }
                            break;

                        } else { //continue search
                            i += (j - i)/2;
                        }
                    } else {
                        if (halfEdges.get(i - 1).getX(temp) <= temp.x) { //check neighbour
                            if (segments_intersect(halfEdges.get(i-1).get(0), halfEdges.get(i-1).get(1),
                                    halfEdges.get(i).get(0), halfEdges.get(i).get(1)) &&
                                    (!temp.getSegment().contains(halfEdges.get(i - 1)) ||
                                            !temp.getSegment().contains(halfEdges.get(i)))) {
                                findNewEvent(halfEdges.get(i-1), halfEdges.get(i), stack);
                            }
                            break;
                        } else { //continue search
                            j = i;
                            i /= 2;
                        }
                    }
                }
            }
        } else { //compare left element with its left neighbour and right with ist right neighbour
            HalfEdge sl = sorted.get(0);
            HalfEdge sr = sorted.get(sorted.size() - 1);
            if (halfEdges.indexOf(sl) - 1 >= 0) {
                HalfEdge sll = halfEdges.get(halfEdges.indexOf(sl) - 1);
                if (segments_intersect(sl.get(0), sl.get(1), sll.get(0), sll.get(1)) && !sl.isIntersectedBefore(sll) &&
                        (!temp.getSegment().contains(sl) ||
                                !temp.getSegment().contains(sll))) {
                    findNewEvent(sl, sll, stack);
                }
            }
            if (halfEdges.indexOf(sr) + 1 < halfEdges.size()) {
                HalfEdge srr = halfEdges.get(halfEdges.indexOf(sr) + 1);
                if (segments_intersect(sr.get(0), sr.get(1), srr.get(0), srr.get(1)) && !sr.isIntersectedBefore(srr) &&
                        (!temp.getSegment().contains(sr) ||
                                !temp.getSegment().contains(srr))) {
                    findNewEvent(sr, srr, stack);
                }
            }
        }
    }

    public static ArrayList<HalfEdge> merger (ArrayList<HalfEdge>... st) { //merge arrays
        ArrayList<HalfEdge> result = new ArrayList<HalfEdge>(st[0]);
        for (int i = 1; i < st.length; i ++) {
            for (HalfEdge s : st[i]) {
                if (!result.contains(s)) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    public static void findNewEvent(HalfEdge s1, HalfEdge s2, ArrayList<Vertex> stack) {  //find intersection point
        int x,y;
        if (s1.get(0).x == s1.get(1).x) {  //if vertical segment
            x = s1.get(0).x;
            double k2 = (double) (s2.get(0).y - s2.get(1).y) / (double) (s2.get(0).x - s2.get(1).x);
            double b2 = s2.get(0).y - k2 * s2.get(0).x;
            y = (int) (k2 * x + b2);
        } else {
            if (s2.get(0).x == s2.get(1).x) {  //if vertical segment
                x = s2.get(0).x;
                double k1 = (double) (s1.get(0).y - s1.get(1).y) / (double) (s1.get(0).x - s1.get(1).x);
                double b1 = s1.get(0).y - k1 * s1.get(0).x;
                y = (int) (k1 * x + b1);
            } else {   //if halfEdges are "normal"
                double k1 = (double) (s1.get(0).y - s1.get(1).y) / (double) (s1.get(0).x - s1.get(1).x);
                double b1 = s1.get(0).y - k1 * s1.get(0).x;
                double k2 = (double) (s2.get(0).y - s2.get(1).y) / (double) (s2.get(0).x - s2.get(1).x);
                double b2 = s2.get(0).y - k2 * s2.get(0).x;
                x = (int) ((b2 - b1) / (k1 - k2));
                y = (int) (k1 * x + b1);
            }
        }
        Vertex temp = new Vertex(x, y);
        s1.addBelong(temp);  //add point to segment
        s2.addBelong(temp);
        if (!stack.contains(temp)) { //if stack haven't this point
            temp.addInter(s1);
            temp.addInter(s2);
            stack.add(temp);
        } else { //if point was found earlier
            Vertex p = stack.get(stack.indexOf(temp));
            p.addInter(s1);
            p.addInter(s2);
        }
    }

    public static int findIndex (ArrayList<HalfEdge> halfEdges, HalfEdge halfEdge, Vertex temp) {  //find index to put
        int i = halfEdges.size() / 2;
        int j = halfEdges.size();
        if (halfEdges.size() == 0) { //empty list
            return 0;
        }
        if (halfEdges.size() == 1) { //compare with element
            return halfEdge.getX(temp) < halfEdges.get(0).getX(temp) ? 0 : 1;
        }
        if (halfEdges.size() == 2) { //compare with each element
            if (halfEdges.get(0).getX(temp) > halfEdge.getX(temp)) {
                return 0;
            } else {
                if (halfEdges.get(1).getX(temp) < halfEdge.getX(temp)) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
        while (true) { //if size > 2
            if (i == halfEdges.size() - 1) { //end of list
                if (halfEdges.get(i).getX(temp) < halfEdge.getX(temp)) {
                    return halfEdges.size();
                } else {
                    return halfEdges.size() - 1;
                }
            }
            if (i == 0) { //start of list
                if (halfEdges.get(0).getX(temp) < halfEdge.getX(temp)) {
                    return 1;
                } else {
                    return 0;
                }
            }
            if (halfEdges.get(i).getX(temp) < halfEdge.getX(temp)) { //check this halfEdge
                if (halfEdges.get(i + 1).getX(temp) >= halfEdge.getX(temp)) {  //check neighbour*
                    return i + 1;
                } else {
                    i += (j - i)/2; //continue search
                }
            } else {
                if (halfEdges.get(i - 1).getX(temp) <= halfEdge.getX(temp)) {  //check neighbour
                    return i;
                } else { //continue search
                    j = i;
                    i /= 2;
                }
            }
        }
    }

    public static void addTo (Vertex temp, ArrayList<Vertex> list) { //add to result list
        for (Vertex p : list) {
            if (temp.equal(p)) { //if point was added earlier
                for (HalfEdge s : temp.getInter()) {
                    p.addInter(s); //add all halfEdges from temp
                }
                return;
            }
        }
        list.add(temp);  //else add point to list
    }

    public static void mapOverlay(ArrayList<Vertex> points, ArrayList<Face> faces, ArrayList<HalfEdge> loops) {
        travers(faces, loops, false);
        ArrayList<Vertex> interPoints = find_intersections(points);
        modifySegments(interPoints, loops);
        travers(faces, loops, true);
        System.out.print(loops.size());
    }

    public static void modifySegments (ArrayList<Vertex> points, ArrayList<HalfEdge> loops) {
        ArrayList<HalfEdge> modifSeg = new ArrayList<HalfEdge>();
        for (Vertex p : points) {
            ArrayList<HalfEdge> inner  = p.getInter();
            for (int i = 0; i < inner.size(); i ++) {
                HalfEdge s = inner.get(i);
                if (!modifSeg.contains(s)) {
                    modifSeg.add(s);
                } else {
                    inner.remove(s);
                    HalfEdge temp1 = s.getPrev().getNext();
                    HalfEdge temp2 = s.getNext().getPrev();
                    if ((p.x < temp1.get(0).x && p.x > temp1.get(1).x) || (p.x < temp1.get(1).x && p.x > temp1.get(0).x)) {
                        s = temp1;
                    } else {
                        s = temp2;
                    }
                    inner.add(i, s);
                }

            }
            ArrayList<HalfEdge> currSeg = new ArrayList<HalfEdge>();
            for (HalfEdge s : inner) {
                if (loops.contains(s)) {
                    loops.remove(s); //точно?
                }
                if (loops.contains(s.twin)) {
                    loops.remove(s.twin); //точно?
                }
                //modSegments.remove(s);
                HalfEdge temp1 = new HalfEdge(s.origin, p);
                HalfEdge temp2 = new HalfEdge(p, s.twin.origin);
                HalfEdge twin1 = new HalfEdge(null, null);
                HalfEdge twin2 = new HalfEdge(null, null);
                HalfEdge twin = s.twin;

                //задаем новые параметры исходя из старых данных
                temp1.origin = s.origin;
                temp1.setPrev(s.getPrev());
                s.getPrev().setNext(temp1);
                temp1.twin = twin1;
                temp2.origin = p;
                temp2.setNext(s.getNext());
                s.getNext().setPrev(temp2);
                temp2.twin = twin2;
                twin2.origin = twin.origin;
                twin2.twin = temp2;
                twin2.setPrev(twin.getPrev());
                twin.getPrev().setNext(twin2);
                twin1.origin = p;
                twin1.setNext(twin.getNext());
                twin.getNext().setPrev(twin1);
                twin1.twin = temp1;
                temp1.setIncidentFace(s.getIncidentFace());
                temp2.setIncidentFace(s.getIncidentFace());
                twin1.setIncidentFace(twin.getIncidentFace());
                twin2.setIncidentFace(twin.getIncidentFace());

                temp1.setNext(temp2);
                twin2.setNext(twin1);

                currSeg.add(temp1);
                currSeg.add(temp2);
                currSeg.add(twin1);
                currSeg.add(twin2);
            }
            //считаем новых соседей
            while (currSeg.size() != 0) {
                HalfEdge temp1 = currSeg.get(0);
                if (temp1.origin == p) {
                    currSeg.remove(temp1);
                    currSeg.add(temp1);
                    continue;
                }
                for (int i = 1; i < currSeg.size(); i ++) {
                    HalfEdge temp2 = currSeg.get(i);
                    if (temp2.origin != p || temp2 == temp1.getNext()) {
                        continue;
                    }
                    if (direction(temp1.origin, p, temp2.twin.origin) < 0) { //левый поворот
                        temp1.setNext(temp2);
                        temp2.setPrev(temp1);
                        currSeg.remove(temp2);
                        loops.add(temp1); //добаляем новый цикл на случай возникновения новой плоскости
                        break;
                    }
                }
                currSeg.remove(temp1);
            }
        }
    }

    public static void travers (ArrayList<Face> faces, ArrayList<HalfEdge> loops, boolean full) {
        faces.clear();
        for (int i = 0; i < loops.size(); i ++) {
            HalfEdge loop = loops.get(i);
            for (int j = i + 1; j < loops.size(); j ++) {
                if (loop.isHasEqualLoop(loops.get(j))) {
                    loops.remove(loops.get(j));
                    j --;
                }
            }
            HalfEdge next, prev;
            Vertex left = new Vertex(-1, -1);
            next = getLeftPoint(loop, left);
            prev = next.getPrev();
            if (direction(prev.origin, left, next.getNext().origin) < 0) { //контур
                Face face;
                if (loop.getIncidentFace() != null) {
                    face = loop.getIncidentFace();
                    Face newFace = new Face();
                    HalfEdge z = loop;
                    do {
                        if (z.getIncidentFace() != null && z.getIncidentFace() != face) { //если в контуре разные плоскости
                            newFace.addParents(z.getIncidentFace());
                            newFace.addParents(loop.getIncidentFace());
                        }
                        z = z.getNext();
                    } while (z != loop);
                    if (!newFace.parents.isEmpty()) {
                        face = newFace;
                    }
                } else {
                    face = new Face();
                }

                HalfEdge z = loop;
                do {
                    z.setIncidentFace(face);
                    z = z.getNext();
                } while (z != loop);
                face.outerComponent = loop;

                if (!faces.contains(face)) {
                    faces.add(face);
                }
            } else { //дыра
                if (full) { //в первый раз не рассматриваем дыры
                    for (HalfEdge s : loops) {
                        if (getLeftSegment(left, s)) {
                            Face face = s.getIncidentFace() == null ? new Face() : s.getIncidentFace();
                            loop.setIncidentFace(face);
                            s.setIncidentFace(face);
                            face.outerComponent = s;
                            face.innerComponents.add(loop);
                            HalfEdge z = loop;
                            do {
                                z.setIncidentFace(face);
                                z = z.getNext();
                            } while (z != loop);
                            z = s;
                            do {
                                z.setIncidentFace(face);
                                z = z.getNext();
                            } while (z != s);
                            if (!faces.contains(face)) {
                                faces.add(face);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean getLeftSegment(Vertex p, HalfEdge start) { //left by y
        HalfEdge st = start;
        if (st.get(0) == null) {
            st = st.twin;
        }
        HalfEdge temp = st;
        do {
            double xx = temp.getX(p);
            if ((xx > temp.get(0).x && xx < temp.get(1).x) || (xx < temp.get(0).x && xx > temp.get(1).x)) { //проверь
                return true;
            }
            temp = temp.getNext();
        } while (temp != st);
        return false;
    }

    public static HalfEdge getLeftPoint (HalfEdge start, Vertex left) {
        int xx = Integer.MAX_VALUE;
        HalfEdge result = null;
        HalfEdge temp = start;
        do {
            if (temp.origin.x < xx) {
                result = temp;
                left.x = temp.origin.x;
                left.y = temp.origin.y;
                xx = temp.origin.x;
            }
            temp = temp.getNext();
        } while (temp != start);
        return result;
    }
}


