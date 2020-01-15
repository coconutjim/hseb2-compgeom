using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CG_IntersectHalfplanesDll.Primitives;

namespace CG_IntersectHalfplanesDll {
    public class HalfplanesIntersection {
         public static Polygon intersectHalfplanes(List<Halfplane> halfplanes) {
        var left = new List<Halfplane>();
        var right = new List<Halfplane>();
        var leftChain = new List<Halfplane>();
        var rightChain = new List<Halfplane>();
        var resLeft = new List<Halfplane>();
        var resRight = new List<Halfplane>();

        foreach (Halfplane h in halfplanes) {
            if (h.rightSide) {
                right.Add(h);
            } else {
                left.Add(h);
            }
        }

        if (left.Count!=0) {
            leftChain.Add(left.First());
            left.Remove(leftChain.First());
        }
        foreach (Halfplane h in left) {
            var insertPos = 0;

            while (insertPos < leftChain.Count && h.line.angle > leftChain[insertPos].line.angle) {
                insertPos++;
            }
            if (insertPos == 0) {
                while(leftChain.Count() > 1 && !h.includes(
                        Line.intersection(leftChain[0].line, leftChain[1].line))) {
                    leftChain.Remove(leftChain.First());
                }
                leftChain.Insert(insertPos, h);
            } else if (insertPos == leftChain.Count) {
                while(leftChain.Count > 1 && !h.includes(
                        Line.intersection(leftChain[leftChain.Count - 1].line, leftChain[leftChain.Count - 2].line))) {
                    leftChain.Remove(leftChain.Last());
                    insertPos--;
                }
                leftChain.Insert(insertPos, h);
            }
            else {
                var vertices = 0;
                var i = 0;
                bool intersection1;
                bool intersection2;

                while (i < leftChain.Count - 1) {
                    intersection1 = intersection2 = false;

                    if (!h.includes(Line.intersection(leftChain[i].line, leftChain[i+1].line))) {
                        vertices++;
                        intersection1 = true;
                    }
                    if (i < leftChain.Count - 2 && !h.includes(
                            Line.intersection(leftChain[i+1].line, leftChain[i+2].line))) {
                        vertices ++;
                        intersection2 = true;
                    }
                    if (intersection1 && intersection2) {
                        leftChain.Remove(leftChain[i + 1]);
                        if (i + 1 < insertPos) {
                            insertPos--;
                        }
                    } else {
                        i++;
                    }
                }
                if (vertices > 0) {
                    leftChain.Insert(insertPos, h);
                }
            }
        }

        if (right.Count !=0) {
            rightChain.Add(right[0]);
            right.Remove(right.First());
        }
        foreach (Halfplane h in right) {
            var insertPos = 0;

            while (insertPos < rightChain.Count && h.line.angle < rightChain[insertPos].line.angle) {
                insertPos++;
            }
            if (insertPos == 0) {
                while(rightChain.Count > 1 && !h.includes(
                        Line.intersection(rightChain[0].line, rightChain[1].line))) {
                    rightChain.Remove(rightChain.First());
                }
                rightChain.Insert(insertPos, h);
            } else if (insertPos == rightChain.Count) {
                while(rightChain.Count > 1 && !h.includes(
                        Line.intersection(rightChain[rightChain.Count - 1].line, rightChain[rightChain.Count - 2].line))) {
                    rightChain.Remove(rightChain.Last());
                    insertPos--;
                }
                rightChain.Insert(insertPos, h);
            } else {
                var vertices = 0;
                var i = 0;
                bool intersection1;
                bool intersection2;

                while (i < rightChain.Count - 1) {
                    intersection1 = intersection2 = false;

                    if (!h.includes(
                            Line.intersection(rightChain[i].line, rightChain[i+1].line))) {
                        vertices++;
                        intersection1 = true;
                    }
                    if (i < rightChain.Count - 2 && !h.includes(
                            Line.intersection(rightChain[i+1].line, rightChain[i+2].line))) {
                        vertices++;
                        intersection2 = true;
                    }
                    if (intersection1 && intersection2) {
                        rightChain.Remove(rightChain[i + 1]);
                        if (i + 1 < insertPos) {
                            insertPos--;
                        }
                    } else {
                        i++;
                    }
                }

                if (vertices > 0) {
                    rightChain.Insert(insertPos, h);
                }
            }
        }

        int startLeft = 0, endLeft = leftChain.Count - 1,
                startRight = 0, endRight = rightChain.Count - 1;
        Point intersection;

        for (var i = 0; i < leftChain.Count; i++) {
            for (var j = 0; j < rightChain.Count; j++) {
                if (((j == 0) || !leftChain[i].includes(Line.intersection(rightChain[j-1].line, rightChain[j].line))) &&
                        ((j == rightChain.Count-1) || leftChain[i].includes(Line.intersection(rightChain[j].line, rightChain[j+1].line))) &&
                        ((i == 0) || ! rightChain[j].includes(Line.intersection(leftChain[i - 1].line, leftChain[i].line))) &&
                        ((i == leftChain.Count-1) || rightChain[j].includes(Line.intersection(leftChain[i].line, leftChain[i + 1].line)))) {
                    startLeft = i;
                    startRight = j;
                } else if (((j == 0) || leftChain[i].includes(Line.intersection(rightChain[j-1].line, rightChain[j].line))) && ((j == rightChain.Count-1) || !leftChain[i].includes(Line.intersection(rightChain[j].line, rightChain[j+1].line))) &&
                        ((i == 0) || rightChain[j].includes(Line.intersection(leftChain[i - 1].line, leftChain[i].line))) &&
                        ((i == leftChain.Count-1) || ! rightChain[j].includes(Line.intersection(leftChain[i].line, leftChain[i + 1].line)))) {
                    endLeft = i;
                    endRight = j;
                }
            }
        }

        for (var i = startLeft; i <= endLeft; i++) {
            resLeft.Add(leftChain[i]);
        }
        for (var j = startRight; j <= endRight; j++) {
            resRight.Add(rightChain[j]);
        }

        Polygon res = createPolygonFromHalfplaneChains(resLeft, resRight);

        return res;
    }
         private static Polygon createPolygonFromHalfplaneChains(List<Halfplane> left, List<Halfplane> right) {
             Polygon res = new Polygon();

             for (int i = 0; i < left.Count() - 1; i++) {
                 res.add(Line.intersection(left[i].line, left[i + 1].line));
             }

             if (left.Count != 0 && right.Count!=0) {
                 Point bottom = Line.intersection(left.Last().line, right.Last().line);
                 Point lowestExistent = new Point(0, Double.MaxValue);

                 if (left.Count() > 1) {
                     lowestExistent = Line.intersection(left.Last().line,
                             left[left.Count - 2].line);
                 }

                 if (right.Count() > 1) {
                     Point temp = Line.intersection(right.Last().line, right[right.Count - 2].line);
                     if (temp.getY() < lowestExistent.getY()) {
                         lowestExistent = temp;
                     }
                 }

                 if (bottom != null && bottom.getY() <= lowestExistent.getY()) {
                     res.add(bottom);
                 }
             }

             for (int i = right.Count() - 1; i > 0; i--) {
                 res.add(Line.intersection(right[i].line, right[i - 1].line));
             }


             if (left.Count !=0 && right.Count !=0) {
                 Point upper = Line.intersection(left[0].line, right[0].line);
                 Point highestExistent = new Point(0, Double.MinValue);

                 if (left.Count() > 1) {
                     highestExistent = Line.intersection(left[0].line, left[1].line);
                 }

                 if (right.Count() > 1) {
                     Point temp = Line.intersection(right[0].line, right[1].line);
                     if (temp.getY() > highestExistent.getY()) {
                         highestExistent = temp;
                     }
                 }

                 if (upper != null && upper.getY() >= highestExistent.getY()) {
                     res.add(upper);
                 }
             }
             return res;
         }


    }
}
