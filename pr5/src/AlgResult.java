/**
 * Created by Lev on 25.02.14.
 */
public class AlgResult {
    private boolean found;
    private Segment segment1;
    private Segment segment2;

    public AlgResult(boolean found, Segment segment1, Segment segment2) {
        this.found = found;
        this.segment1 = segment1;
        this.segment2 = segment2;
    }

    public boolean isFound() {
        return found;
    }

    public Segment getSegment1() {
        return segment1;
    }

    public Segment getSegment2() {
        return segment2;
    }
}
