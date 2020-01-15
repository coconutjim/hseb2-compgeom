using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CG_IntersectHalfplanesDll.Primitives {
    public class Polygon {
        public List<Point> vertices;
        private bool _isClockwise;
        public bool finished = false;

        public Polygon() {
            vertices = new List<Point>();
        }

        public void add(Point p) {
            if (p != null) {
                vertices.Add(p);
            }
        }

        public Object clone() {
            Polygon copy = new Polygon();
            copy.vertices = new List<Point>(this.vertices);
            return copy;
        }

        public Polygon subPolygon(int start, int end) {
            Polygon p = new Polygon();
            if (start < end) {
                for (int i = start; i <= end; i++) {
                    p.add(get(i));
                }
            } else if (start > end) {
                for (int i = start; i < size(); i++) {
                    p.add(get(i));
                }
                for (int i = 0; i <= end; i++) {
                    p.add(get(i));
                }
            }

            p._isClockwise = this._isClockwise;
            return p;
        }

        public int size() {
            return vertices.Count;
        }

        public Point get(int i) {
            if (i >= 0 && i < size()) {
                return vertices[i];
            } else {
                return null;
            }
        }

        public bool isConvex(int i) {
            int prev = i - 1;
            if (prev < 0) prev += size();
            int next = (i + 1) % size();

            return (Point.isLeftTurn(get(prev), get(i), get(next))
                    ^ _isClockwise);
        }

        public bool isClockwise() {
            double sum = 0;

            for (int i = 0; i < size(); i++) {
                sum += (vertices[(i + 1) % size()].getX() - vertices[i].getX()) *
                        (vertices[(i + 1) % size()].getY() + vertices[i].getY());
            }

            _isClockwise = (sum > 0);
            return _isClockwise;
        }
    }


}
