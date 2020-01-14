using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CG_IntersectHalfplanesDll;
using CG_IntersectHalfplanesDll.Primitives;
using Point = CG_IntersectHalfplanesDll.Primitives.Point;

namespace CG_IntersectHalfplanes {
    public partial class Form1 : Form {
        private List<Halfplane> halfplanes = new List<Halfplane>();
        private Polygon result = null;
        private Line inputLine = null;
        private Point temp = null;
        private Point temp2 = null;
        private Pen tempLinePen = new Pen(Brushes.Gray, 3);
        private Pen rightLinePen = new Pen(Brushes.Blue, 3);
        private Pen leftLinePen = new Pen(Brushes.Green, 4);
        private Pen resultLinePen = new Pen(Brushes.Red, 4);
        private Pen helpLinePen = new Pen(Brushes.LightGray, 4);


        public Form1() {
            InitializeComponent();
        }

        private void Form1_KeyPress(object sender, KeyPressEventArgs e) {
            if (inputLine != null) {
                char input = e.KeyChar;
                if (input == 'r') {
                    halfplanes.Add(new Halfplane(inputLine, false));
                    AddHalfplaneLog(true);
                    inputLine = null;
                    temp = temp2 = null;
                   
                }
                else if (input == 'l') {
                    halfplanes.Add(new Halfplane(inputLine, true));
                    AddHalfplaneLog(false);
                    inputLine = null;
                    temp = temp2 = null;
                }
                canvasHalfplanesIntersection.Invalidate(false);
            }
            else {
                char input = e.KeyChar;
                if (input == 'c') {
                    halfplanes.Clear();
                    inputLine = null;
                    temp = temp2 = null;
                    ClearLog();
                }
                else if (input == 's') {
                    result = HalfplanesIntersection.intersectHalfplanes(halfplanes);
                    FinishAlgorithmLog();
                    inputLine = null;
                    temp = temp2 = null;
                }
                canvasHalfplanesIntersection.Invalidate(false);
            }
        }


        private void canvasHalfplanesIntersection_MouseClick(object sender, MouseEventArgs e) {
            if (temp == null && inputLine == null) {
                temp = new Point(e.X, e.Y);
            } else if (inputLine == null) {
                temp2 = new Point(e.X, e.Y);
                inputLine = new Line(temp, temp2);
            }
            AddPointLog(e.X, e.Y);
            canvasHalfplanesIntersection.Invalidate(false);
        }

        private void canvasHalfplanesIntersection_Paint(object sender, PaintEventArgs e) {
            var g = e.Graphics;
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
            if (temp != null && temp2 != null) {
                g.DrawLine(tempLinePen, (int)temp.getX(), (int)temp.getY(), (int)temp2.getX(), (int)temp2.getY());
            }
            if (temp != null) {
                g.FillEllipse(Brushes.Red, (int)temp.getX() - 4, (int)temp.getY() - 4, 8, 8);
            }
            if (temp2 != null) {
                g.FillEllipse(Brushes.Red, (int)temp2.getX() - 4, (int)temp2.getY() - 4, 8, 8);         
            }
             foreach (Halfplane halfplane in halfplanes) {
                 g.DrawLine(halfplane.rightSide?rightLinePen:leftLinePen, (int)(-halfplane.line.c / halfplane.line.a), 0,
                            (int)((-halfplane.line.c - halfplane.line.b * canvasHalfplanesIntersection.Height) / halfplane.line.a),
                           canvasHalfplanesIntersection.Height);

                }

             if (result != null) {
                 if (result.vertices.Count == 1) {
                     g.FillEllipse(Brushes.Red, (int) result.vertices[0].getX() - 3, (int) result.vertices[0].getY() - 3,
                         6, 6);
                 } else 
                 for (int i = 0; i < result.vertices.Count; ++i) {
                     g.DrawLine(resultLinePen, (int)result.vertices[i].getX(),
                             (int)result.vertices[i].getY(),
                             (int)result.vertices[(i + 1) % result.vertices.Count].getX(),
                             (int)result.vertices[(i + 1) % result.vertices.Count].getY());
                 }
                 result = null;
             }

        }

        #region Логгинг
        private void AddPointLog(int x, int y) {
            lbLogger.Items.Add("Добавлено: (" + x + ", " + y + ")");
            SelectLastEntryLog();
        }

        private void AddHalfplaneLog(bool isLeftBound) {
            lbLogger.Items.Add("Добавлена " + (isLeftBound ? "левая" : "правая") + " граница");
            SelectLastEntryLog();
        }
        private void ClearLog() {
            lbLogger.Items.Clear();
        }


        private void FinishAlgorithmLog() {
            lbLogger.Items.Add("Конец алгоритма!");
            SelectLastEntryLog();
        }

        private void SelectLastEntryLog() {
            lbLogger.SelectedIndex = lbLogger.Items.Count - 1;
        }


        #endregion
    }

}
