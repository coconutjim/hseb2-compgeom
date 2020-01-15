package ComputationalGeometry.MonotonePolygon;

import ComputationalGeometry.misc.AATree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/31/13
 * Time: 1:47 AM
 */
public class MonotonePolygon {
    private static List<Segment> result;
    private static List<Segment> newSegments;
    private static AATree Q;
    private static AATree T;
    private static Helper helper;

    public static List<Segment> makeMonotone(List<Vertex> vertexes, List<Segment> segments) {
        Q = new AATree();
        T = new AATree();
        newSegments = new ArrayList<Segment>();
        helper = new Helper();
        result = new ArrayList<Segment>();

        for (Vertex v: vertexes) {
            Q.insert(v);
        }

        while (!Q.isEmpty()) {
            handleVertex((Vertex) Q.getMinAndRemove());
        }

        result.addAll(newSegments);
        result.addAll(segments);
        //processAnswer(segments, newSegments);

        return result;
    }

    private static List<List<Segment>> processAnswer(List<Segment> input, List<Segment> diag) {
        List<List<Segment>> answer = new ArrayList<List<Segment>>();

        while (input.size() != 0 && diag.size() != 0) {
            List<Segment> loop = new ArrayList<Segment>();
            Segment first = input.get(0);
            Segment tempSegment = first;
            input.remove(first);
            do {
                loop.add(tempSegment);
                List<Segment> nextSeg = new ArrayList<Segment>();
                List<Segment> nextDiag = new ArrayList<Segment>();
                for (Segment segment: input) {
                    if (tempSegment.getSecondVertex().compareTo(segment.getFirstVertex()) == 0) {
                        nextSeg.add(segment);
                    }
                }
                for (Segment segment: diag) {
                    if (tempSegment.getSecondVertex().compareTo(segment.getFirstVertex()) == 0) {
                        nextDiag.add(segment);
                    } else if (first.getSecondVertex().compareTo(segment.getSecondVertex()) == 0) {
                        Vertex temp = segment.getSecondVertex();
                        segment.vertexes.set(1, segment.getFirstVertex());
                        segment.vertexes.set(0, temp);
                        nextDiag.add(segment);
                    }
                }
                if (nextDiag.size() == 0) {
                    tempSegment = nextSeg.get(0);
                    input.remove(nextSeg.get(0));
                    diag.remove(nextSeg.get(0));
                } else {
                    tempSegment = nextDiag.get(0);
                }
            } while (tempSegment.compareTo(first) != 0);
            answer.add(loop);
        }
        return answer;
    }

    private static void handleVertex(Vertex vertex) {
        Vertex leftVertex = vertex.firstVertexOf().getSecondVertex();
        Vertex rightVertex = vertex.secondVertexOf().getFirstVertex();

        if (leftVertex.y > vertex.y && rightVertex.y >= vertex.y ||
                leftVertex.y >= vertex.y && rightVertex.y > vertex.y) {
            if (Vertex.direction(rightVertex, vertex, leftVertex) > 0) {
                handleStartVertex(vertex);
            } else {
                handleSplitVertex(vertex);
            }
        } else if (leftVertex.y < vertex.y && rightVertex.y <= vertex.y &&
                leftVertex.y <= vertex.y && rightVertex.y < vertex.y &&
                Vertex.direction(rightVertex, vertex, leftVertex) < 0) {
            handleMergeVertex(vertex);
        } else {
            handleRegularVertex(vertex);
        }
    }

    private static void handleStartVertex(Vertex vertex) {
        System.out.println(vertex.toString() + " start");
        helper.addHelper(vertex.firstVertexOf(), vertex, false);
        T.insert(vertex.firstVertexOf());
    }

    private static void handleSplitVertex(Vertex vertex) {
        System.out.println(vertex.toString() + " split");
        Segment vertexSegment = vertex.firstVertexOf();
        Segment leftSegment = getLeftSegment(vertex);
        Segment newSegment = new Segment(helper.getHelper(leftSegment), vertex);
        System.out.println("add new segment " + newSegment.toString());
        newSegments.add(newSegment);
        helper.changeHelper(leftSegment, vertex, false);
        T.insert(vertexSegment);
        helper.addHelper(vertexSegment, vertex, false);
    }

    private static void handleMergeVertex(Vertex vertex) {
        System.out.println(vertex.toString() + " merge");
        if (helper.isMerge(vertex.secondVertexOf())) {
            Vertex vertex1 = helper.getHelper(vertex.secondVertexOf());
            Segment newSegment = new Segment(vertex, vertex1);
            System.out.println("add new segment " + newSegment.toString());
            newSegments.add(newSegment);
        }
        T.remove(vertex.secondVertexOf());
        Segment leftSegment = getLeftSegment(vertex);
        if (helper.isMerge(leftSegment)) {
            Segment newSegment = new Segment(helper.getHelper(leftSegment), vertex);
            System.out.println("add new segment " + newSegment.toString());
            newSegments.add(newSegment);
        }
        helper.changeHelper(leftSegment, vertex, true);
    }

    private static void handleRegularVertex(Vertex vertex) {
        System.out.println(vertex.toString() + " regular");
        if (numberOfSegmentsOnTheLeft(vertex) % 2 == 0) {
            if (helper.isMerge(vertex.secondVertexOf())) {
                Segment newSegment = new Segment(vertex, helper.getHelper(vertex.secondVertexOf()));
                System.out.println("add new segment " + newSegment.toString());
                newSegments.add(newSegment);
            }
            T.remove(vertex.secondVertexOf());
            T.insert(vertex.firstVertexOf());
            helper.addHelper(vertex.firstVertexOf(), vertex, false);
        } else {
            Segment leftSegment = getLeftSegment(vertex);
            if (helper.isMerge(leftSegment)) {
                Segment newSegment = new Segment(vertex, helper.getHelper(leftSegment));
                System.out.println("add new segment " + newSegment.toString());
                newSegments.add(newSegment);
            }
            helper.changeHelper(leftSegment, vertex, false);
        }
    }

    private static Segment getLeftSegment(Vertex vertex) {
        List<Comparable> segments = T.getElements();
        List<Segment> seg = new ArrayList<Segment>();
        for (Comparable segment: segments) {
            seg.add((Segment)segment);
        }
        if (seg.size() > 1) {
            Helper.qSort(seg, 0, segments.size() - 1);
        }

        for (int i = 0; i < seg.size(); ++i) {
            if (seg.get(i).getUpperVertex().x >= vertex.x) {
                if (i == 0) {
                    return null;
                } else {
                    return seg.get(i - 1);
                }
            }
        }

        return seg.get(seg.size() - 1);
    }

    private static int numberOfSegmentsOnTheLeft(Vertex vertex) {
        List<Comparable> segments = T.getElements();
        int i = 0;

        for (; i < segments.size(); ++i) {
            if (Vertex.direction(
                    ((Segment) segments.get(i)).getUpperVertex(), ((Segment) segments.get(i)).getLowerVertex(), vertex
                    ) <= 0) {
                break;
            }
        }

        return i;
    }

    private static int numberOfSegmentsOnTheRight(Vertex vertex) {
        List<Comparable> segments = T.getElements();
        int i = 0;

        for (int j = segments.size() - 1; j >= 0; --j, ++i) {
            if (Vertex.direction(
                    ((Segment) segments.get(i)).getUpperVertex(), ((Segment) segments.get(i)).getLowerVertex(), vertex
            ) >= 0) {
                break;
            }
        }

        return i;
    }
}

class Helper {
    class Pair {
        public Segment segment;
        public Vertex vertex;
        public boolean mergeVertex;

        public Pair(Segment segment, Vertex vertex, boolean mergeVertex) {
            this.segment = segment;
            this.vertex = vertex;
            this.mergeVertex = mergeVertex;
        }
    }

    public List<Pair> helpers;

    public Helper() {
        helpers = new ArrayList<Pair>();
    }

    public void addHelper(Segment segment, Vertex vertex, boolean mergeVertex) {
        System.out.println("add helper for " + segment.toString() + " with " + vertex.toString() + " " + mergeVertex);
        helpers.add(new Pair(segment, vertex, mergeVertex));
    }

    public void changeHelper(Segment segment, Vertex vertex, boolean mergeVertex) {
        for (Pair pair: helpers) {
            if (pair.segment == segment) {
                System.out.println("change helper for " + segment.toString() + " from " + pair.vertex.toString() + " "
                        + pair.mergeVertex + " to " + vertex.toString() + " " + mergeVertex);
                pair.vertex = vertex;
                pair.mergeVertex = mergeVertex;
                break;
            }
        }
    }

    public Vertex getHelper(Segment segment) {
        for (Pair pair: helpers) {
            if (pair.segment == segment) {
                return pair.vertex;
            }
        }
        return null;
    }

    public boolean isMerge(Segment segment) {
        for (Pair pair: helpers) {
            if (pair.segment == segment) {
                return pair.mergeVertex;
            }
        }
        return false;
    }

    public static void qSort(List<Segment> A, int low, int high) {
        int i = low;
        int j = high;
        Segment x = A.get((low+high)/2);  // x - опорный элемент посредине между low и high
        do {
            while (A.get(i).getUpperVertex().compareTo(x.getUpperVertex()) > 0)
                ++i;
            // поиск элемента для переноса в старшую часть
            while (A.get(j).getUpperVertex().compareTo(x.getUpperVertex()) < 0)
                --j;
            // поиск элемента для переноса в младшую часть
            if(i <= j){
                // обмен элементов местами:
                Segment temp = A.get(i);
                A.set(i, A.get(j));
                A.set(j, temp);
                // переход к следующим элементам:
                i++; j--;
            }
        } while(i < j);
        if(low < j) qSort(A, low, j);
        if(i < high) qSort(A, i, high);
    }

}
