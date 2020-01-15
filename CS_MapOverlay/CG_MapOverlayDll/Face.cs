using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CG_MapOverlayDll {
    public class Face {
        public HalfEdge outerComponent;
        public List<HalfEdge> innerComponents = new List<HalfEdge>();
        public List<Face> parents = new List<Face>();

        public HalfEdge getOuterComponent() {
            return outerComponent;
        }

        public void addParents(Face face) {
            if (!parents.Contains(face)) parents.Add(face);
        }
    }
}
