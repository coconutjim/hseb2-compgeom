using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CG_MapOverlayDll {
    public static class Methods {
        public static int direction(Vertex i, Vertex j, Vertex k) {
            return (k.x - i.x) * (j.y - i.y) - (j.x - i.x) * (k.y - i.y);
        }
        public static bool on_segment(Vertex i, Vertex j, Vertex k) {
            return (Math.Min(i.x, j.x) <= k.x) && (k.x <= Math.Max(i.x, j.x))
                    && (Math.Min(i.y, j.y) <= k.y) && (k.y <= Math.Max(i.y, j.y));
        }

        public static bool segments_intersect(params Vertex[] points) {
            int d1 = direction(points[2], points[3], points[0]);
            int d2 = direction(points[2], points[3], points[1]);
            int d3 = direction(points[0], points[1], points[2]);
            int d4 = direction(points[0], points[1], points[3]);

            if ((((d1 > 0) && (d2 < 0)) || ((d1 < 0) && d2 > 0))
                && (((d3 > 0) && (d4 < 0)) || ((d3 < 0) && (d4 > 0)))) {
                return true;
            }
            if ((d1 == 0) && (on_segment(points[2], points[3], points[0])))
                return true;
            if ((d2 == 0) && on_segment(points[2], points[3], points[1]))
                return true;
            if ((d3 == 0) && on_segment(points[0], points[1], points[2]))
                return true;
            if ((d4 == 0) && on_segment(points[0], points[1], points[3]))
                return true;
            return false;
        }


        private class pointComparer : IComparer<Vertex> {
            public int Compare(Vertex o1, Vertex o2) {
                return o1.y == o2.y ? o1.x - o2.x : o1.y - o2.y;
            }
        }

        public static List<Vertex> find_intersections(List<Vertex> list) {
            List<Vertex> points = new List<Vertex>(list); //for work
            List<Vertex> result = new List<Vertex>();    //array of intersection points
            List<Vertex> stack = new List<Vertex>();     //queue
            List<HalfEdge> halfEdges = new List<HalfEdge>();  //halfEdges in the queue
            List<HalfEdge> allHalfEdges = new List<HalfEdge>(); //all halfEdges
            points.Sort(new pointComparer());
            foreach (Vertex p in list) {
                if (p.getSegment().Count == 0) {
                    points.Remove(p);
                    break;
                }
                foreach (HalfEdge s in p.getSegment()) {
                    if (!allHalfEdges.Contains(s)) {
                        allHalfEdges.Add(s);
                        s.clearBelong();
                        p.clearInter();
                    }
                }
            }
            int i = 0;
            do {
                Vertex temp = null;
                if (i < points.Count) {    //adding new point from the list
                    temp = points[i];
                    foreach (Vertex p in stack) {
                        if (p.x >= temp.x - 3 && p.x <= temp.x + 3 && p.y >= temp.y - 3 && p.y <= temp.y + 3) {  //inner
                            stack.Remove(p);
                            break;
                        }
                    }
                    stack.Add(temp);
                    i++;
                    stack.Sort(new pointComparer());
                }
                if (temp != null) { //if added
                    int oldSize = stack.Count;
                    while (true) {
                        handleEventPoint(stack, result, halfEdges, allHalfEdges); //handle point
                        if (stack[0] == temp) { //if start/end point
                            stack.Remove(stack.First());
                            break;
                        }
                        stack.Remove(stack.First());
                        if (stack.Count != oldSize - 1) {  //if new point added in method
                            stack.Sort(new pointComparer());
                        }
                        if (result.Count > 100) { //for exceptions
                            // System.out.print("Sorry...");
                            return result;
                        }
                    }
                }
            }
            while (stack.Count != 0 || i < points.Count);
            foreach (Vertex p in points) {
                for (int j = 0; j < result.Count; j++) {
                    Vertex r = result[j];
                    if (r.x >= p.x - 3 && r.x <= p.x + 3 && r.y >= p.y - 3 && r.y <= p.y + 3) {
                        result.Remove(r);
                        j--;
                    }
                }
            }
            return result;
        }
        public static void handleEventPoint(List<Vertex> stack, List<Vertex> result,
                                       List<HalfEdge> halfEdges, List<HalfEdge> allHalfEdges) {
            Vertex temp = stack[0]; //current point
            List<HalfEdge> U = new List<HalfEdge>(); //started at this point
            foreach (HalfEdge s in allHalfEdges) {
                if (s.getHigher() == temp) {
                    U.Add(s);
                }
            }
            List<HalfEdge> L = new List<HalfEdge>();   //ended at this point
            List<HalfEdge> C = new List<HalfEdge>();   //halfEdges contains this point
            foreach (HalfEdge s in halfEdges) {
                if (s.getLower() == temp) {
                    L.Add(s);
                    continue;
                }
                if (s.isBelongs(temp)) {
                    C.Add(s);
                }
            }

            List<HalfEdge> LUC = merger(L, U, C);
            if (LUC.Count > 1) { //intersection is found
                if (!(LUC.Count == 2 && (temp.getSegment().Contains(LUC[0]) ||
                        temp.getSegment().Contains(LUC[1])))) {
                    addTo(temp, result);   //add to result
                    //modifySegments(temp, modSegments);  //лучше после всего обработать
                }
            }
            foreach (HalfEdge s in merger(L, C)) { //delete halfEdges which finished at this point and contains this point
                halfEdges.Remove(s);
            }
            //add halfEdges which started at this point and contains this point
            List<HalfEdge> UC = merger(U, C);
            List<HalfEdge> sorted = new List<HalfEdge>();
            if (UC.Count != 0) {
                List<double> d1 = new List<double>(), d2 = new List<double>();
                //sorting by angle
                foreach (HalfEdge s in UC) {
                    double angle = Math.Atan2(s.getLower().y - temp.y, s.getLower().x - temp.x);
                    d1.Add(angle);
                    d2.Add(angle);
                }
                d2.Sort();
                for (int i = d2.Count - 1; i >= 0; i--) {
                    sorted.Add(UC[d1.IndexOf(d2[i])]);
                }
                int index = findIndex(halfEdges, sorted[0], temp); //find index to add
                for (int i = sorted.Count - 1; i >= 0; i--) {
                    halfEdges.Insert(index, sorted[i]);
                }
            }
            if (merger(U, C).Count == 0) {  //if segment ended compare right and lift neighbours
                if (halfEdges.Count > 1) {
                    int i = halfEdges.Count / 2;
                    int j = halfEdges.Count;
                    while (true) {
                        if (i == halfEdges.Count - 1 || i == 0) { //if end of start
                            break;
                        }
                        if (halfEdges[i].getX(temp) < temp.x) { //check this segment
                            if (halfEdges[i + 1].getX(temp) >= temp.x) { //check neighbour
                                if (segments_intersect(halfEdges[i].get(0), halfEdges[i].get(1),
                                        halfEdges[i + 1].get(0), halfEdges[i + 1].get(1)) &&
                                        (!temp.getSegment().Contains(halfEdges[i]) ||
                                                !temp.getSegment().Contains(halfEdges[i + 1]))) {
                                    findNewEvent(halfEdges[i], halfEdges[i + 1], result);
                                }
                                break;

                            } else { //continue search
                                i += (j - i) / 2;
                            }
                        } else {
                            if (halfEdges[i - 1].getX(temp) <= temp.x) { //check neighbour
                                if (segments_intersect(halfEdges[i - 1].get(0), halfEdges[i - 1].get(1),
                                        halfEdges[i].get(0), halfEdges[i].get(1)) &&
                                        (!temp.getSegment().Contains(halfEdges[i - 1]) ||
                                                !temp.getSegment().Contains(halfEdges[i]))) {
                                    findNewEvent(halfEdges[i - 1], halfEdges[i], stack);
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
                HalfEdge sl = sorted[0];
                HalfEdge sr = sorted[sorted.Count - 1];
                if (halfEdges.IndexOf(sl) - 1 >= 0) {
                    HalfEdge sll = halfEdges[halfEdges.IndexOf(sl) - 1];
                    if (segments_intersect(sl.get(0), sl.get(1), sll.get(0), sll.get(1)))
                    if ( !sl.isIntersectedBefore(sll)) 
                        if (!temp.getSegment().Contains(sl) ||
                                    !temp.getSegment().Contains(sll)) {
                        findNewEvent(sl, sll, stack);
                    }
                }
                if (halfEdges.IndexOf(sr) + 1 < halfEdges.Count) {
                    HalfEdge srr = halfEdges[halfEdges.IndexOf(sr) + 1];
                    if (segments_intersect(sr.get(0), sr.get(1), srr.get(0), srr.get(1)) && !sr.isIntersectedBefore(srr) &&
                            (!temp.getSegment().Contains(sr) ||
                                    !temp.getSegment().Contains(srr))) {
                        findNewEvent(sr, srr, stack);
                    }
                }
            }
        }

        public static List<HalfEdge> merger(params List<HalfEdge>[] st) { //merge arrays
            List<HalfEdge> result = new List<HalfEdge>(st[0]);
            for (int i = 1; i < st.Length; i++) {
                foreach (HalfEdge s in st[i]) {
                    if (!result.Contains(s)) {
                        result.Add(s);
                    }
                }
            }
            return result;
        }

        public static void findNewEvent(HalfEdge s1, HalfEdge s2, List<Vertex> stack) {  //find intersection point
            int x, y;
            if (s1.get(0).x == s1.get(1).x) {  //if vertical segment
                x = s1.get(0).x;
                double k2 = (double)(s2.get(0).y - s2.get(1).y) / (double)(s2.get(0).x - s2.get(1).x);
                double b2 = s2.get(0).y - k2 * s2.get(0).x;
                y = (int)(k2 * x + b2);
            } else {
                if (s2.get(0).x == s2.get(1).x) {  //if vertical segment
                    x = s2.get(0).x;
                    double k1 = (double)(s1.get(0).y - s1.get(1).y) / (double)(s1.get(0).x - s1.get(1).x);
                    double b1 = s1.get(0).y - k1 * s1.get(0).x;
                    y = (int)(k1 * x + b1);
                } else {   //if halfEdges are "normal"
                    double k1 = (double)(s1.get(0).y - s1.get(1).y) / (double)(s1.get(0).x - s1.get(1).x);
                    double b1 = s1.get(0).y - k1 * s1.get(0).x;
                    double k2 = (double)(s2.get(0).y - s2.get(1).y) / (double)(s2.get(0).x - s2.get(1).x);
                    double b2 = s2.get(0).y - k2 * s2.get(0).x;
                    x = (int)((b2 - b1) / (k1 - k2));
                    y = (int)(k1 * x + b1);
                }
            }
            Vertex temp = new Vertex(x, y);
            s1.addBelong(temp);  //add point to segment
            s2.addBelong(temp);
            if (!stack.Contains(temp)) { //if stack haven't this point
                temp.addInter(s1);
                temp.addInter(s2);
                stack.Add(temp);
            } else { //if point was found earlier
                Vertex p = stack[stack.IndexOf(temp)];
                p.addInter(s1);
                p.addInter(s2);
            }
        }

        public static int findIndex(List<HalfEdge> halfEdges, HalfEdge halfEdge, Vertex temp) {  //find index to put
            int i = halfEdges.Count / 2;
            int j = halfEdges.Count;
            if (halfEdges.Count == 0) { //empty list
                return 0;
            }
            if (halfEdges.Count == 1) { //compare with element
                return halfEdge.getX(temp) < halfEdges[0].getX(temp) ? 0 : 1;
            }
            if (halfEdges.Count == 2) { //compare with each element
                if (halfEdges[0].getX(temp) > halfEdge.getX(temp)) {
                    return 0;
                } else {
                    if (halfEdges[1].getX(temp) < halfEdge.getX(temp)) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            }
            while (true) { //if size > 2
                if (i == halfEdges.Count - 1) { //end of list
                    if (halfEdges[i].getX(temp) < halfEdge.getX(temp)) {
                        return halfEdges.Count;
                    } else {
                        return halfEdges.Count - 1;
                    }
                }
                if (i == 0) { //start of list
                    if (halfEdges[0].getX(temp) < halfEdge.getX(temp)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                if (halfEdges[i].getX(temp) < halfEdge.getX(temp)) { //check this halfEdge
                    if (halfEdges[i + 1].getX(temp) >= halfEdge.getX(temp)) {  //check neighbour*
                        return i + 1;
                    } else {
                        i += (j - i) / 2; //continue search
                    }
                } else {
                    if (halfEdges[i - 1].getX(temp) <= halfEdge.getX(temp)) {  //check neighbour
                        return i;
                    } else { //continue search
                        j = i;
                        i /= 2;
                    }
                }
            }
        }

       public static void addTo (Vertex temp, List<Vertex> list) { //add to result list
        foreach (Vertex p in list) {
            if (temp.equal(p)) { //if point was added earlier
                foreach (HalfEdge s in temp.getInter()) {
                    p.addInter(s); //add all halfEdges from temp
                }
                return;
            }
        }
        list.Add(temp);  //else add point to list
    }

    public static void mapOverlay(List<Vertex> points, List<Face> faces, List<HalfEdge> loops) {
        travers(faces, loops, false);
        List<Vertex> interPoints = find_intersections(points);
        modifySegments(interPoints, loops);
        travers(faces, loops, true);
      //  System.out.print(loops.size());
    }

         public static void modifySegments (List<Vertex> points, List<HalfEdge> loops) {
        List<HalfEdge> modifSeg = new List<HalfEdge>();
        foreach (Vertex p in points) {
            List<HalfEdge> inner  = p.getInter();
            for (int i = 0; i < inner.Count; i ++) {
                HalfEdge s = inner[i];
                if (!modifSeg.Contains(s)) {
                    modifSeg.Add(s);
                } else {
                    inner.Remove(s);
                    HalfEdge temp1 = s.getPrev().getNext();
                    HalfEdge temp2 = s.getNext().getPrev();
                    if ((p.x < temp1.get(0).x && p.x > temp1.get(1).x) || (p.x < temp1.get(1).x && p.x > temp1.get(0).x)) {
                        s = temp1;
                    } else {
                        s = temp2;
                    }
                    inner.Insert(i, s);
                }

            }
            List<HalfEdge> currSeg = new List<HalfEdge>();
            foreach (HalfEdge s in inner) {
                if (loops.Contains(s)) {
                    loops.Remove(s); //не уверен
                }
                if (loops.Contains(s.twin)) {
                    loops.Remove(s.twin); //не уверен
                }
                //modSegments.Remove(s);
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

                currSeg.Add(temp1);
                currSeg.Add(temp2);
                currSeg.Add(twin1);
                currSeg.Add(twin2);
            }
            //считаем новых соседей
            while (currSeg.Count != 0) {
                HalfEdge temp1 = currSeg[0];
                if (temp1.origin == p) {
                    currSeg.Remove(temp1);
                    currSeg.Add(temp1);
                    continue;
                }
                for (int i = 1; i < currSeg.Count; i ++) {
                    HalfEdge temp2 = currSeg[i];
                    if (temp2.origin != p || temp2 == temp1.getNext()) {
                        continue;
                    }
                    if (direction(temp1.origin, p, temp2.twin.origin) < 0) { //левый поворот
                        temp1.setNext(temp2);
                        temp2.setPrev(temp1);
                        currSeg.Remove(temp2);
                        loops.Add(temp1); //добаляем новый цикл на случай возникновения новой плоскости
                        break;
                    }
                }
                currSeg.Remove(temp1);
            }
        }
    }

         public static void travers (List<Face> faces, List<HalfEdge> loops, bool full) {
        faces.Clear();
        for (int i = 0; i < loops.Count; i ++) {
            HalfEdge loop = loops[i];
            for (int j = i + 1; j < loops.Count; j ++) {
                if (loop.isHasEqualLoop(loops[j])) {
                    loops.Remove(loops[j]);
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
                    HalfEdge z1 = loop;
                    do {
                        if (z1.getIncidentFace() != null && z1.getIncidentFace() != face) { //если в контуре разные плоскости
                            newFace.addParents(z1.getIncidentFace());
                            newFace.addParents(loop.getIncidentFace());
                        }
                        z1 = z1.getNext();
                    } while (z1 != loop);
                    if (newFace.parents.Count!=0) {
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

                if (!faces.Contains(face)) {
                    faces.Add(face);
                }
            } else { //дыра
                if (full) { //в первый раз не рассматриваем дыры
                    foreach (HalfEdge s in loops) {
                        if (getLeftSegment(left, s)) {
                            Face face = s.getIncidentFace() ?? new Face();
                            loop.setIncidentFace(face);
                            s.setIncidentFace(face);
                            face.outerComponent = s;
                            face.innerComponents.Add(loop);
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
                            if (!faces.Contains(face)) {
                                faces.Add(face);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

       public static bool getLeftSegment(Vertex p, HalfEdge start) { //left by y
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
        int xx = int.MaxValue;
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
}
