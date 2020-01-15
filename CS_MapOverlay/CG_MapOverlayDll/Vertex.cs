using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CG_MapOverlayDll {
    public class Vertex: IComparable<Vertex> {
        public int x, y;
        private readonly List<HalfEdge> _interHalfEdges = new List<HalfEdge>();
        private readonly List<HalfEdge> _halfEdges = new List<HalfEdge>();
        public HalfEdge IncidentEdge {set;get;}
        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Vertex(Point p) {
            x = p.X;
            y = p.Y;
        }
        public void setSegment(HalfEdge seg) {
            if (!_halfEdges.Contains(seg)) {
                _halfEdges.Add(seg);
            }
        }
        public List<HalfEdge> getSegment() {
            return _halfEdges;
        }


        public int CompareTo(Vertex other) {
            return x - other.x;
        }

        public void addInter(HalfEdge s) {
            if (!_interHalfEdges.Contains(s)) {
                _interHalfEdges.Add(s);
            }
        }

        public void clearInter() {
            _interHalfEdges.Clear();
        }

        public List<HalfEdge> getInter() {
            return _interHalfEdges;
        }

        public bool equal(Vertex p) {
            return p.x < x + 4 && p.x > x - 4 && p.y < y + 4 && p.y > y - 4;
        }

        public Point ToPoint() {
            return new Point(x, y);
        }

        public override string ToString() {
            var sb = new StringBuilder();
            return sb.Append('(').Append(x).Append("; ").Append(y).Append(')').ToString();
        }

      
    }
}
