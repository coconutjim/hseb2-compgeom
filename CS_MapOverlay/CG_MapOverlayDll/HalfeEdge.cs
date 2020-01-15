using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CG_MapOverlayDll {
    public class HalfEdge {
        public Vertex[] points = new Vertex[2];
        public List<Vertex> belong = new List<Vertex>();
        //for ex 6
        public Vertex origin;
        public HalfEdge twin;
        public Face IncidentFace;
        public HalfEdge next;
        public HalfEdge prev;

        public HalfEdge(Vertex p1, Vertex p2) {
            points[0] = p1;
            points[1] = p2;
            try {
                p1.setSegment(this);
                p2.setSegment(this);
            }
            catch (Exception e) { }
        }

        public Vertex get(int index) {
            if (index >= 0 && index < 2) {
                if (points[0] != null) {
                    return points[index];
                } else {
                    return twin.get(index);
                }
            }
            throw new ArgumentException();
        }

        public double getX(Vertex point) {
            if (points[0] != null) {
                if (get(0).x == get(1).x) {  //
                    return get(0).x;
                }
                double k = (double)(get(0).y - get(1).y) / (double)(get(0).x - get(1).x);
                double b = get(0).y - k * get(0).x;
                return (point.y - b) / k;
            } else {
                return twin.getX(point);
            }
        }

        public Vertex getHigher() {
            if (points[0] != null) {
                return points[0].y > points[1].y ? points[1] : points[0];
            } else {
                return twin.getHigher();
            }
        }

        public Vertex getLower() {
            if (points[0] != null) {
                return points[0].y < points[1].y ? points[1] : points[0];
            } else {
                return twin.getLower();
            }

        }

        public void addBelong(Vertex p) {
            if (!belong.Contains(p)) {
                belong.Add(p);
            }
        }

        public bool isBelongs(Vertex point) {
            if (belong.Contains(point)) {
                return true;
            }
            double p1 = (double)(point.x - points[0].x) / (points[1].x - points[0].x);
            double p2 = (double)(point.y - points[0].y) / (points[1].y - points[0].y);
            return p1 == p2 && p1 >= 0 && p1 <= 1;
        }

        public bool isIntersectedBefore(HalfEdge s) {
            foreach (Vertex m in belong) {
                if (s.getBelong().Contains(m)) {
                    return true;
                }
            }
            return false;
        }

        public List<Vertex> getBelong() {
            return belong;
        }

        public void clearBelong() {
            belong.Clear();
        }

        public Face getIncidentFace() {
            return IncidentFace;
        }

        public void setIncidentFace(Face incidentFace) {
            IncidentFace = incidentFace;
        }

        public HalfEdge getNext() {
            return next;
        }

        public void setNext(HalfEdge next) {
            this.next = next;
        }

        public HalfEdge getPrev() {
            return prev;
        }

        public void setPrev(HalfEdge prev) {
            this.prev = prev;
        }

        public bool isHasEqualLoop(HalfEdge loop) {
            HalfEdge temp = loop.getNext();
            do {
                if (temp == loop && temp != this) {
                    return false;
                }
                if (temp == this) {
                    break;
                }
                temp = temp.getNext();
            } while (true);
            HalfEdge start = this;
            do {
                if (start != temp) {
                    return false;
                }
                start = start.getNext();
                temp = temp.getNext();
            } while (start != this);
            return true;
        }

        public override string ToString() {
            StringBuilder sb = new StringBuilder();
            sb.Append("{").Append(origin).Append(", ").Append(twin.origin).Append("}");
            return sb.ToString();
        }


    }
}
