
import java.util.ArrayList;

public class C {

    private ArrayList<HalfPlane> left;
    private ArrayList<HalfPlane> right;

    public C() {
        left = new ArrayList<HalfPlane>();
        right = new ArrayList<HalfPlane>();
    }
    
    public C(HalfPlane h) {
        this();

        if (h.leftSurface()) left.add(h);
        else right.add(h);
    }
    
    public double topmostVertexY() {
        if (right != null && ! right.isEmpty() && left != null && ! left.isEmpty() &&
                left.get(0).getLine().isAscending() && ! right.get(0).getLine().isAscending()) {
            return Line.getIntersection(right.get(0).getLine(), left.get(0).getLine()).getY();
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    
    private static double max(double... values) {
        double result = Double.NEGATIVE_INFINITY;
        for (double d : values) {
            if (d > result) result = d;
        }
        return result;
    }
    
    public ArrayList<HalfPlane> getLeft() {
        return left;
    }
    
    public ArrayList<HalfPlane> getRight() {
        return right;
    }

    public static C intersection(C c1, C c2) {
        
        C C = new C();
        
        boolean is_left_c1 = false,
                is_right_c1 = false,
                is_left_c2 = false,
                is_right_c2 = false;
        
        double y1 = c1.topmostVertexY();
        double y2 = c2.topmostVertexY();
        double sweepingLineY = Math.min(y1, y2);
        
        // Iterators initializing
        EdgeIterator leftC1 = new EdgeIterator(c1.left);
        EdgeIterator rightC1 = new EdgeIterator(c1.right);
        EdgeIterator leftC2 = new EdgeIterator(c2.left);
        EdgeIterator rightC2 = new EdgeIterator(c2.right);
        
        if (Double.isInfinite(sweepingLineY)) {
            if (c1.getLeft().size() > 0) {
                is_left_c1 = true;
            }
            if (c1.getRight().size() > 0) {
                is_right_c1 = true;
            }
            if (c2.getLeft().size() > 0) {
                is_left_c2 = true;
            }
            if (c2.getRight().size() > 0) {
                is_right_c2 = true;
            }
        } else {
            if (sweepingLineY == y1) {
                is_left_c1 = is_right_c1 = true;
            }
            if (sweepingLineY == y2) {
                is_left_c2 = is_right_c2 = true;
            }
        }
        
        while (sweepingLineY > Double.NEGATIVE_INFINITY) {
            
            // processing queue event points

            if (is_left_c1) { // left c1
                System.out.println("left c1");
                if (Double.isInfinite(leftC1.upperVertexX()) ||
                    (leftC2.currentEdge() == null ||
                     leftC1.upperVertexX() >= leftC2.currentEdge().getLine().getXbyY(sweepingLineY)) &&
                    (rightC2.currentEdge() == null ||
                     leftC1.upperVertexX() <= rightC2.currentEdge().getLine().getXbyY(sweepingLineY))) {
                    C.left.add(leftC1.currentEdge());System.out.println("added left c1");
                }
                if (leftC1.intersects(rightC2)) {
                    if (rightC2.currentEdge() == null ||
                        leftC1.upperVertexX() >= rightC2.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.left.add(leftC1.currentEdge());System.out.println("added left c1");
                        C.right.add(rightC2.currentEdge());System.out.println("added right c2");
                    }
                }
                if (leftC1.intersects(leftC2)) {
                    if (leftC2.currentEdge() == null ||
                        leftC1.upperVertexX() <= leftC2.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.left.add(leftC1.currentEdge());System.out.println("added left c1");
                    } else {
                        C.left.add(leftC2.currentEdge());System.out.println("added left c2");
                    }
                }
            }
            
            if (is_right_c1) { // right c1
                System.out.println("right c1");
                if (Double.isInfinite(rightC1.upperVertexX()) ||
                    (leftC2.currentEdge() == null ||
                     rightC1.upperVertexX() >= leftC2.currentEdge().getLine().getXbyY(sweepingLineY)) &&
                    (rightC2.currentEdge() == null ||
                     rightC1.upperVertexX() <= rightC2.currentEdge().getLine().getXbyY(sweepingLineY))) {
                    C.right.add(rightC1.currentEdge());System.out.println("added right c1");
                }
                if (rightC1.intersects(leftC2)) {
                    if (leftC2.currentEdge() == null || 
                        rightC1.upperVertexX() <= leftC2.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.right.add(rightC1.currentEdge());System.out.println("added right c1");
                        C.left.add(leftC2.currentEdge());System.out.println("added left c2");
                    }
                }
                if (rightC1.intersects(rightC2)) {
                    if (rightC2.currentEdge() == null ||
                        rightC1.upperVertexX() >= rightC2.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.right.add(rightC1.currentEdge());System.out.println("added right c1");
                    } else {
                        C.right.add(rightC2.currentEdge());System.out.println("added right c2");
                    }
                }                
            }
            
            if (is_left_c2) { // left c2
                System.out.println("left c2");
                if (Double.isInfinite(leftC2.upperVertexX()) ||
                    (leftC1.currentEdge() == null ||
                     leftC2.upperVertexX() >= leftC1.currentEdge().getLine().getXbyY(sweepingLineY)) &&
                    (rightC1.currentEdge() == null ||
                     leftC2.upperVertexX() <= rightC1.currentEdge().getLine().getXbyY(sweepingLineY))) {
                    C.left.add(leftC2.currentEdge());System.out.println("added left c2");
                }
                if (leftC2.intersects(rightC1)) {
                    if (rightC1.currentEdge() == null ||
                        leftC2.upperVertexX() >= rightC1.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.left.add(leftC2.currentEdge());System.out.println("added left c2");
                        C.right.add(rightC1.currentEdge());System.out.println("added right c1");
                    }
                }
                if (leftC2.intersects(leftC1)) {
                    if (leftC1.currentEdge() == null ||
                        leftC2.upperVertexX() <= leftC1.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.left.add(leftC2.currentEdge());System.out.println("added left c2");
                    } else {
                        C.left.add(leftC1.currentEdge());System.out.println("added left c2");
                    }
                }
            }
            
            if (is_right_c2) { // right c2
                System.out.println("right c2");
                if (Double.isInfinite(rightC2.upperVertexX()) ||
                    (leftC1.currentEdge() == null ||
                     rightC2.upperVertexX() >= leftC1.currentEdge().getLine().getXbyY(sweepingLineY)) &&
                    (rightC1.currentEdge() == null ||
                     rightC2.upperVertexX() <= rightC1.currentEdge().getLine().getXbyY(sweepingLineY))) {
                    C.right.add(rightC2.currentEdge());System.out.println("added right c2");
                }
                if (rightC2.intersects(leftC2)) {
                    if (leftC1.currentEdge() == null ||
                        rightC2.upperVertexX() <= leftC1.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.right.add(rightC2.currentEdge());System.out.println("added right c2");
                        C.left.add(leftC1.currentEdge());System.out.println("added left c1");
                    }
                }
                if (rightC2.intersects(rightC1)) {
                    if (rightC1.currentEdge() == null ||
                        rightC2.upperVertexX() >= rightC1.currentEdge().getLine().getXbyY(sweepingLineY)) {
                        C.right.add(rightC2.currentEdge());System.out.println("added right c2");
                    } else {
                        C.right.add(rightC1.currentEdge());System.out.println("added right c1");
                    }
                }                
            }

            // left to right computing of event points

            sweepingLineY = max(leftC1.lowerVertexY(),
                                rightC1.lowerVertexY(),
                                leftC2.lowerVertexY(),
                                rightC2.lowerVertexY());
            
            // Going down to the sweeping line Y coordinate
            is_left_c1 = leftC1.toY(sweepingLineY);
            is_right_c1 = rightC1.toY(sweepingLineY);
            is_left_c2 = leftC2.toY(sweepingLineY);
            is_right_c2 = rightC2.toY(sweepingLineY);
        }
        
        return C;
    }
    
    static class EdgeIterator {

        private int i;
        private Point2D lowerVertex, upperVertex;
        private ArrayList<HalfPlane> edges;

        public EdgeIterator(ArrayList<HalfPlane> edges) {
            this.edges = edges;
            lowerVertex = null;
            i = -1;
            
            next();
        }
        
        public void next() {
            i ++;
            upperVertex = lowerVertex;
            if (i >= edges.size() - 1) {
                lowerVertex = null;
            } else {
                lowerVertex = Line.getIntersection(edges.get(i).getLine(), edges.get(i + 1).getLine());
            }
        }
        
        public boolean intersects(EdgeIterator edge) {
            
            if (edge.currentEdge() == null ||
                this.currentEdge() == null) {
                return false;
            }
            
            Point2D intersection = Line.getIntersection(this.currentEdge().getLine(),
                    edge.currentEdge().getLine());
            
            if (intersection.isNanDot()) {
                return false;
            }
            
            return intersection.getY() <= edge.upperVertexY() &&
                   intersection.getY() >= edge.lowerVertexY() &&
                   intersection.getY() <= this.upperVertexY() &&
                   intersection.getY() >= this.lowerVertexY();
        }

        public boolean toY(double Y) {
            boolean change = (lowerVertex != null) && (lowerVertex.getY() == Y);
            
            if(change) {
                next();
            }
            
            return change;
        }
        
        public double upperVertexX() {
            return upperVertex == null ?
                    Double.NEGATIVE_INFINITY : upperVertex.getX();
        }
        
        public double upperVertexY() {
            return upperVertex == null ?
                    Double.NEGATIVE_INFINITY : upperVertex.getY();
        }
        
        public double lowerVertexY() {
            return lowerVertex == null ?
                    Double.NEGATIVE_INFINITY : lowerVertex.getY();
        }
        
        public HalfPlane currentEdge() {
            if (i >= edges.size()) {
                return null;
            } else {
                return edges.get(i);
            }
        }

    }
    
}