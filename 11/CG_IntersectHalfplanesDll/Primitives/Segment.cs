using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CG_IntersectHalfplanesDll.Primitives {
    public class Segment {
         private readonly Point left;
     private readonly Point right;
 
        public Segment(Point left, Point right) {

        if(left.getX() > right.getX()) {
            Point temp = right;
            right = left;
            left = temp;
        }

        left.setSegment(this);
        right.setSegment(this);

        this.left = left;
        this.right = right;
    }

    public Point getLeft() {
        return left;
    }

    public Point getRight() {
        return right;
    }

    public Point getBisectionalPoint() {
        return new Point((left.getX() + right.getX()) / 2,
                (left.getY() + right.getY()) / 2);
    }

    public Line getLine() {
        return new Line(left, right);
    }


        public override bool Equals(object o2) {
              if (! (o2 is Segment)) {
            return false;
        }
             Segment s2 = (Segment) o2;
             return ((this.getLeft().Equals(s2.getLeft()) &&
                this.getRight().Equals(s2.getRight())) ||
                (this.getLeft().Equals(s2.getRight()) &&
                        this.getRight().Equals(s2.getLeft())));
        }

      

       

       
    }

    
}
