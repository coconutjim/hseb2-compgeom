using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CG_IntersectHalfplanesDll.Primitives;

namespace CG_IntersectHalfplanesDll.Misc {
    public class Line {
        public Point left;
        public Point right;
        public bool reversed;

        public Line(Point l, Point r) {
            left = l;
            right = r;
        }
    }
}
