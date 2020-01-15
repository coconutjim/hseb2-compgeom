using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CG_MapOverlayDll;

namespace CG_MapOverlayForm {
    public partial class Form1 : Form {
        private bool isPressed;
        private Pen _linePen = new Pen(Color.Black, 2);

        private Vertex prev;
        private Vertex first;
        private HalfEdge prevSeg;
        private HalfEdge firstSeg;

        private List<Vertex> points = new List<Vertex>();
        private List<Vertex> result = new List<Vertex>();
        private List<Face> faces = new List<Face>();
        private List<HalfEdge> loops = new List<HalfEdge>();

        public Form1() {
            InitializeComponent();
           
        }

        private void GenerateTemplate1() {
            AddPoint(68, 295);
            AddPoint(96, 185);
            AddPoint(124, 126);
            AddPoint(202, 85);
            AddPoint(354, 58);
            AddPoint(429, 58);
            AddPoint(559, 128);
            AddPoint(621, 196);
            AddPoint(650, 264);
            AddPoint(638, 408);
            AddPoint(623, 529);
            AddPoint(522, 628);
            AddPoint(424, 662);
            AddPoint(350, 669);
            AddPoint(228, 628);
            AddPoint(143, 573);
            AddPoint(92, 497);
            AddPoint(71, 391);
            AddPoint(63, 334);
            AddPoint(67, 296);
            AddPoint(180, 232);
            AddPoint(198, 203);
            AddPoint(262, 167);
            AddPoint(350, 135);
            AddPoint(416, 137);
            AddPoint(478, 166);
            AddPoint(529, 208);
            AddPoint(561, 256);
            AddPoint(570, 333);
            AddPoint(558, 419);
            AddPoint(550, 500);
            AddPoint(531, 542);
            AddPoint(488, 571);
            AddPoint(436, 581);
            AddPoint(376, 589);
            AddPoint(305, 570);
            AddPoint(259, 541);
            AddPoint(217, 494);
            AddPoint(179, 423);
            AddPoint(162, 373);
            AddPoint(159, 327);
            AddPoint(166, 277);
            AddPoint(179, 233);
            AddPoint(257, 240);
            AddPoint(312, 223);
            AddPoint(399, 213);
            AddPoint(443, 218);
            AddPoint(488, 258);
            AddPoint(500, 308);
            AddPoint(508, 371);
            AddPoint(493, 459);
            AddPoint(469, 511);
            AddPoint(438, 536);
            AddPoint(389, 528);
            AddPoint(309, 498);
            AddPoint(264, 447);
            AddPoint(240, 373);
            AddPoint(235, 321);
            AddPoint(252, 276);
            AddPoint(259, 239);
            AddPoint(303, 286);
            AddPoint(327, 271);
            AddPoint(367, 261);
            AddPoint(414, 300);
            AddPoint(441, 336);
            AddPoint(454, 395);
            AddPoint(451, 436);
            AddPoint(414, 462);
            AddPoint(387, 470);
            AddPoint(354, 469);
            AddPoint(323, 440);
            AddPoint(303, 380);
            AddPoint(288, 329);
            AddPoint(302, 284);
            AddPoint(328, 319);
            AddPoint(375, 324);
            AddPoint(395, 343);
            AddPoint(414, 373);
            AddPoint(409, 411);
            AddPoint(390, 426);
            AddPoint(366, 429);
            AddPoint(342, 413);
            AddPoint(329, 351);
            AddPoint(326, 317);
        }

        private void GenerateTemplate2() {
            AddPoint(226, 198);
            AddPoint(241, 464);
            AddPoint(589, 491);
            AddPoint(581, 224);
            AddPoint(226, 199);
            AddPoint(72, 120);
            AddPoint(80, 294);
            AddPoint(310, 314);
            AddPoint(334, 97);
            AddPoint(74, 121);
            AddPoint(474, 129);
            AddPoint(514, 298);
            AddPoint(637, 370);
            AddPoint(666, 150);
            AddPoint(473, 127);
            AddPoint(105, 402);
            AddPoint(362, 401);
            AddPoint(266, 584);
            AddPoint(107, 401);
            AddPoint(485, 575);
            AddPoint(497, 415);
            AddPoint(685, 463);
            AddPoint(635, 583);
            AddPoint(485, 574);
        }

        private void ClearAllData() {
            isPressed = false;
            prev = null;
            first = null;
            prevSeg = null;
            firstSeg = null;
            points.Clear();
            result.Clear();
            faces.Clear();
            loops.Clear();
        }

        private void canvasMapOverlay_Paint(object sender, PaintEventArgs e) {
            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
             if (faces.Count !=0) {
                List<Face> tempFace = new List<Face>(faces);
                List<Face> interSeg = new List<Face>();
                for (int i = 0; i < tempFace.Count; i ++) {
                    Brush polygonBrush = new SolidBrush(Color.FromArgb(((2*i + 1)*40)%255, ((i + 1)*20)%255, ((i + 1)*30)%255));  
                    Face f = tempFace[i];
                    if (f.parents.Count > 0) {
                        i --;
                        tempFace.Remove(f);
                        interSeg.Add(f);
                        continue;
                    }
                    List<Point> polygPoints = new List<Point>();
                    HalfEdge temp = f.getOuterComponent();
                    do {
                        polygPoints.Add(temp.origin.ToPoint());
                        temp = temp.getNext();
                    } while (temp != f.getOuterComponent());
                    e.Graphics.FillPolygon(polygonBrush, polygPoints.ToArray());
                }
                for (int i = 0; i < interSeg.Count; i ++) {
                    Face f = interSeg[i];
                    AddFaceToLog(f);
                    List<Point> polygPoints = new List<Point>();
                    HalfEdge temp = f.getOuterComponent();
                    do {
                        polygPoints.Add(temp.origin.ToPoint());
                        temp = temp.getNext();
                    } while (temp != f.getOuterComponent());
                    e.Graphics.FillPolygon(Brushes.Green, polygPoints.ToArray());
                }
            }
             for (int i = 0; i < points.Count; i++) {
                 try {
                     Vertex p1 = points[i].IncidentEdge.get(0);
                     Vertex p2 = points[i].IncidentEdge.get(1);
                     e.Graphics.DrawLine(_linePen, p1.ToPoint(), p2.ToPoint());
                 }
                 catch (Exception ex) { }
                 e.Graphics.FillEllipse(Brushes.Yellow, points[i].x - 5, points[i].y - 5, 10, 10);
             }
        }

       

        private void btnStart_Click(object sender, EventArgs e) {
            StartAlgorithmLog();
            result.Clear();
            Methods.mapOverlay(points, faces, loops);
            canvasMapOverlay.Invalidate(false);
            int totalOverlays = 0;
            for (int i = 0; i < faces.Count; i ++) {
                if (faces[i].parents.Count != 0)
                    totalOverlays++;
            }
            ResultAlgorithmLog(totalOverlays);
            
            FinishAlgorithmLog();


        }

        private void AddPoint(int x, int y) {
            Vertex curr = new Vertex(x, y);
            if (!isPressed) { //нажимаем первый раз
                isPressed = true;
                first = curr;
            } else { //уже нажимали прежде
                if (curr.x > first.x - 5 && curr.x < first.x + 5 && curr.y > first.y - 5 && curr.y < first.y + 5) {
                    //закончить цикл
                    HalfEdge temp = new HalfEdge(prev, first);
                    HalfEdge twin = new HalfEdge(null, null);  //плоскости задать
                    temp.origin = prev;
                    prev.IncidentEdge = temp;
                    temp.twin = twin;
                    twin.twin = temp;
                    twin.origin = first;
                    temp.setPrev(prevSeg);
                    temp.setNext(firstSeg);
                    twin.setPrev(firstSeg.twin);
                    twin.setNext(prevSeg.twin);
                    prevSeg.twin.setPrev(twin);
                    prevSeg.setNext(temp);
                    firstSeg.setPrev(temp);
                    firstSeg.twin.setNext(twin);
                    loops.Add(temp);
                    loops.Add(twin); //стоит ли?
                    isPressed = false;
                    prev = null;
                    prevSeg = null;
                    first = null;
                    firstSeg = null;
                    canvasMapOverlay.Invalidate(false);
                    return;
                } else {
                    HalfEdge temp = new HalfEdge(prev, curr);
                    HalfEdge twin = new HalfEdge(null, null);
                    temp.origin = prev;
                    prev.IncidentEdge = temp;
                    twin.origin = curr;
                    temp.twin = twin;
                    twin.twin = temp;
                    if (firstSeg == null) {
                        firstSeg = temp;
                    } else {
                        temp.setPrev(prevSeg);
                        prevSeg.setNext(temp);
                        prevSeg.twin.setPrev(twin);
                        twin.setNext(prevSeg.twin);
                    }
                    prevSeg = temp;
                }
            }
            prev = curr;
            points.Add(curr);
        }
        private void canvasMapOverlay_MouseClick(object sender, MouseEventArgs e) {
            if (e.Button == MouseButtons.Left) {
                Vertex curr = new Vertex(e.Location);
                AddPoint(curr.x, curr.y);
                AddPointLog(curr.x, curr.y);
                
            }
            if (e.Button == MouseButtons.Right) {
                ClearAllData();
                ClearLog();
            }
            canvasMapOverlay.Invalidate(false);
        }

        #region Логгинг

        private void AddFaceToLog(Face face) {
            lbLogger.Items.Add("Граница: "+face.outerComponent.ToString());

        }

        private void AddPointLog(int x, int y) {
            lbLogger.Items.Add("Добавлено: (" + x +", "+y+")");
            SelectLastEntryLog();
        }

        private void StartAlgorithmLog() {
            lbLogger.Items.Add("Старт алгоритма!");
            SelectLastEntryLog();
        }

        private void ResultAlgorithmLog(int overlayCount) {
            if (overlayCount == 0)
                lbLogger.Items.Add("Нет наложений");
            else
                lbLogger.Items.Add("Наложений найдено: " + overlayCount);
            SelectLastEntryLog();
        }

        private void FinishAlgorithmLog() {
            lbLogger.Items.Add("Конец алгоритма!");
            SelectLastEntryLog();
        }

        private void ClearLog() {
            lbLogger.Items.Clear();
        }

        private void SelectLastEntryLog() {
            lbLogger.SelectedIndex = lbLogger.Items.Count - 1;
        }

        #endregion

        private void Form1_KeyPress(object sender, KeyPressEventArgs e) {
            if (e.KeyChar=='1')
                GenerateTemplate1();
            if (e.KeyChar == '2')
                GenerateTemplate2();

        }

    }
}
