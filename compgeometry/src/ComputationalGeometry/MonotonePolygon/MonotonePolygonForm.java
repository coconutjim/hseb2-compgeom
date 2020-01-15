package ComputationalGeometry.MonotonePolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/31/13
 * Time: 1:12 AM
 */
public class MonotonePolygonForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("MonotonePolygon");

        frame.add(new JComponent() {
            List<Vertex> vertexes = new ArrayList<Vertex>();
            List<Segment> segments = new ArrayList<Segment>();
            List<Segment> result = new ArrayList<Segment>();

            Polygon polygon = null;
            Vertex tempVertex;
            boolean isFinished = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Vertex newVertex = new Vertex(e.getX(), e.getY());
                        result = new ArrayList<Segment>();
                        /*/
                        List<Vertex> vert = new ArrayList<Vertex>();
                        List<Segment> segments1 = new ArrayList<Segment>();
                        Vertex start = new Vertex(140, 70);
                        Vertex vertex1 = new Vertex(110, 80);
                        Vertex vertex2 = new Vertex(100, 10);
                        Vertex vertex3 = new Vertex(70, 30);
                        Vertex vertex4 = new Vertex(60, 10);
                        Vertex vertex5 = new Vertex(30, 40);
                        Vertex vertex6 = new Vertex(50, 70);
                        Vertex vertex7 = new Vertex(40, 90);
                        Vertex vertex8 = new Vertex(20, 80);
                        Vertex vertex9 = new Vertex(10, 120);
                        Vertex vertex10 = new Vertex(30, 150);
                        Vertex vertex11 = new Vertex(65, 135);
                        Vertex vertex12 = new Vertex(100, 160);
                        Vertex vertex13 = new Vertex(80, 110);
                        Vertex vertex14 = new Vertex(120, 120);
                        vert.add(start);
                        vert.add(vertex1);
                        vert.add(vertex2);
                        vert.add(vertex3);
                        vert.add(vertex4);
                        vert.add(vertex5);
                        vert.add(vertex6);
                        vert.add(vertex7);
                        vert.add(vertex8);
                        vert.add(vertex9);
                        vert.add(vertex10);
                        vert.add(vertex11);
                        vert.add(vertex12);
                        vert.add(vertex13);
                        vert.add(vertex14);
                        segments1.add(new Segment(start, vertex1));
                        segments1.add(new Segment(vertex1, vertex2));
                        segments1.add(new Segment(vertex2, vertex3));
                        segments1.add(new Segment(vertex3, vertex4));
                        segments1.add(new Segment(vertex4, vertex5));
                        segments1.add(new Segment(vertex5, vertex6));
                        segments1.add(new Segment(vertex6, vertex7));
                        segments1.add(new Segment(vertex7, vertex8));
                        segments1.add(new Segment(vertex8, vertex9));
                        segments1.add(new Segment(vertex9, vertex10));
                        segments1.add(new Segment(vertex10, vertex11));
                        segments1.add(new Segment(vertex11, vertex12));
                        segments1.add(new Segment(vertex12, vertex13));
                        segments1.add(new Segment(vertex13, vertex14));
                        segments1.add(new Segment(vertex14, start));
                        for (int i = 0; i < segments1.size(); ++i) {
                            segments1.get(i).next = segments1.get((i + 1) % segments1.size());
                            segments1.get(i).previous = segments1.get((i - 1 + segments1.size()) % segments1.size());
                        }
                        result = MonotonePolygon.makeMonotone(vert, segments1);
                        //*/
                        //*/
                        if (isFinished) {
                            isFinished = false;
                            vertexes.clear();
                            segments.clear();
                            polygon = null;
                            tempVertex = null;
                        } else {
                            if (tempVertex == null) {
                                tempVertex = newVertex;
                                vertexes.add(newVertex);
                            } else {
                                if (polygon == null) {
                                    Segment newSegment = new Segment(tempVertex, newVertex);
                                    segments.add(newSegment);
                                    vertexes.add(newVertex);
                                    polygon = new Polygon(newSegment);
                                } else {
                                    for (Vertex vertex: vertexes) {
                                        if (Math.abs(vertex.x - newVertex.x) <= 5 &&
                                                Math.abs(vertex.y - newVertex.y) <= 5) {
                                            isFinished = true;
                                            if (segments.size() >= 2) {
                                                try {
                                                    Segment lastSegment = polygon.finishPolygon();
                                                    segments.add(lastSegment);
                                                    result = MonotonePolygon.makeMonotone(vertexes, segments);
                                                } catch (Exception e1) {
                                                    System.out.println(e1.getMessage());
                                                }
                                            }
                                            return;
                                        }
                                    }

                                    try {
                                        Segment lastSegment = polygon.addSegment(newVertex);
                                        segments.add(lastSegment);
                                        vertexes.add(newVertex);
                                    } catch (Exception e1) {
                                        System.out.println(e1.getMessage());
                                    }
                                }
                            }
                        }
                        //*/
                    }
                    public void mouseReleased(MouseEvent e) {
                        repaint();
                    }
                });
                setPreferredSize(new Dimension(400, 300));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(Color.WHITE);
                for(Vertex vertex : vertexes) {
                    g.fillOval(vertex.getIntX() - 4, vertex.getIntY() - 4, 8, 8);
                }

                for (Segment segment: segments) {
                    g.drawLine(segment.vertexes.get(0).getIntX(), segment.vertexes.get(0).getIntY(),
                            segment.vertexes.get(1).getIntX(), segment.vertexes.get(1).getIntY());
                }

                for (Segment segment: result) {
                    g.setColor(Color.BLUE);
                    g.drawLine(segment.vertexes.get(0).getIntX(), segment.vertexes.get(0).getIntY(),
                            segment.vertexes.get(1).getIntX(), segment.vertexes.get(1).getIntY());
                }
            }

            @Override
            public void setInheritsPopupMenu(boolean value) {
                super.setInheritsPopupMenu(value);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
