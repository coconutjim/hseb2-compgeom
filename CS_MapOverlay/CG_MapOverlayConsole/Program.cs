using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CG_MapOverlayDll;

namespace CG_MapOverlayConsole {
    class Program {
        static void Main(string[] args) {
          /*  List<Vertex> vertexes = new List<Vertex>() {
                new Vertex(229, 117),
                new Vertex(357, 215),
                new Vertex(216, 212),
                new Vertex(390, 123),
                new Vertex(357, 215),
                new Vertex(279, 366),
                new Vertex(385, 310),
                new Vertex(216, 212)
            };*/
            HalfEdge first = new HalfEdge(new Vertex(229, 117), new Vertex(357, 215));
            HalfEdge second = new HalfEdge(new Vertex(216, 212), new Vertex(390, 123));
            HalfEdge third = new HalfEdge(new Vertex(357, 215), new Vertex(279, 366));
            HalfEdge fourth = new HalfEdge(new Vertex(385, 310), new Vertex(216, 212));
            List<Vertex> vertexes = new List<Vertex>() {
                first.get(0), first.get(1),
                second.get(0), second.get(1),
                third.get(0), third.get(1),
                fourth.get(0), fourth.get(1)
            };
          //  List<Vertex> stack = new List<Vertex>();
          //  Methods.findNewEvent(third, fourth, stack);
            var result = Methods.segments_intersect(second.get(0), second.get(1), first.get(0), first.get(1));
           // var result = Methods.find_intersections(vertexes);
            Console.WriteLine(result);
            Console.ReadLine();
        }
    }
}
