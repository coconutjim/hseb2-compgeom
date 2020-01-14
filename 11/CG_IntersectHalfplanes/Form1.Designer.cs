namespace CG_IntersectHalfplanes {
    partial class Form1 {
        /// <summary>
        /// Требуется переменная конструктора.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Освободить все используемые ресурсы.
        /// </summary>
        /// <param name="disposing">истинно, если управляемый ресурс должен быть удален; иначе ложно.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

            private void InitializeComponent() {
            this.canvasHalfplanesIntersection = new System.Windows.Forms.PictureBox();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.button1 = new System.Windows.Forms.Button();
            this.lblRightBound = new System.Windows.Forms.Label();
            this.lblDelay = new System.Windows.Forms.Label();
            this.tbDelay = new System.Windows.Forms.TextBox();
            this.btnBuildPolygon = new System.Windows.Forms.Button();
            this.lbLogger = new System.Windows.Forms.ListBox();
            this.lblLeftBound = new System.Windows.Forms.Label();
            this.btnRightButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.canvasHalfplanesIntersection)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // canvasHalfplanesIntersection
            // 
            this.canvasHalfplanesIntersection.Dock = System.Windows.Forms.DockStyle.Fill;
            this.canvasHalfplanesIntersection.Location = new System.Drawing.Point(0, 0);
            this.canvasHalfplanesIntersection.Name = "canvasHalfplanesIntersection";
            this.canvasHalfplanesIntersection.Size = new System.Drawing.Size(414, 461);
            this.canvasHalfplanesIntersection.TabIndex = 0;
            this.canvasHalfplanesIntersection.TabStop = false;
            this.canvasHalfplanesIntersection.Paint += new System.Windows.Forms.PaintEventHandler(this.canvasHalfplanesIntersection_Paint);
            this.canvasHalfplanesIntersection.MouseClick += new System.Windows.Forms.MouseEventHandler(this.canvasHalfplanesIntersection_MouseClick);
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.canvasHalfplanesIntersection);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.tableLayoutPanel1);
            this.splitContainer1.Panel2MinSize = 200;
            this.splitContainer1.Size = new System.Drawing.Size(618, 461);
            this.splitContainer1.SplitterDistance = 414;
            this.splitContainer1.TabIndex = 1;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Controls.Add(this.button1, 1, 3);
            this.tableLayoutPanel1.Controls.Add(this.lblRightBound, 0, 3);
            this.tableLayoutPanel1.Controls.Add(this.lblDelay, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.tbDelay, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.btnBuildPolygon, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.lbLogger, 0, 4);
            this.tableLayoutPanel1.Controls.Add(this.lblLeftBound, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.btnRightButton, 1, 2);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 5;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 30F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 30F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 20F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 20F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.Size = new System.Drawing.Size(200, 461);
            this.tableLayoutPanel1.TabIndex = 1;
            // 
            // button1
            // 
            this.button1.AutoSize = true;
            this.button1.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.button1.BackColor = System.Drawing.Color.Blue;
            this.button1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.button1.Location = new System.Drawing.Point(188, 83);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(9, 14);
            this.button1.TabIndex = 6;
            this.button1.Text = "  ";
            this.button1.UseVisualStyleBackColor = false;
            // 
            // lblRightBound
            // 
            this.lblRightBound.AutoSize = true;
            this.lblRightBound.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lblRightBound.Font = new System.Drawing.Font("Verdana", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.lblRightBound.Location = new System.Drawing.Point(3, 80);
            this.lblRightBound.Name = "lblRightBound";
            this.lblRightBound.Size = new System.Drawing.Size(179, 20);
            this.lblRightBound.TabIndex = 4;
            this.lblRightBound.Text = "Цвет границы справа:";
            // 
            // lblDelay
            // 
            this.lblDelay.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
            this.lblDelay.AutoSize = true;
            this.lblDelay.Font = new System.Drawing.Font("Verdana", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.lblDelay.Location = new System.Drawing.Point(3, 6);
            this.lblDelay.Name = "lblDelay";
            this.lblDelay.Size = new System.Drawing.Size(179, 18);
            this.lblDelay.TabIndex = 0;
            this.lblDelay.Text = "Задержка:";
            this.lblDelay.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // tbDelay
            // 
            this.tbDelay.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
            this.tbDelay.Font = new System.Drawing.Font("Verdana", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.tbDelay.Location = new System.Drawing.Point(188, 3);
            this.tbDelay.Name = "tbDelay";
            this.tbDelay.Size = new System.Drawing.Size(9, 26);
            this.tbDelay.TabIndex = 1;
            this.tbDelay.Text = "500";
            // 
            // btnBuildPolygon
            // 
            this.btnBuildPolygon.AutoSize = true;
            this.btnBuildPolygon.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.tableLayoutPanel1.SetColumnSpan(this.btnBuildPolygon, 2);
            this.btnBuildPolygon.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btnBuildPolygon.Font = new System.Drawing.Font("Verdana", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.btnBuildPolygon.Location = new System.Drawing.Point(0, 30);
            this.btnBuildPolygon.Margin = new System.Windows.Forms.Padding(0);
            this.btnBuildPolygon.Name = "btnBuildPolygon";
            this.btnBuildPolygon.Size = new System.Drawing.Size(200, 30);
            this.btnBuildPolygon.TabIndex = 2;
            this.btnBuildPolygon.Text = "Найти пересечение";
            this.btnBuildPolygon.UseVisualStyleBackColor = true;
            // 
            // lbLogger
            // 
            this.tableLayoutPanel1.SetColumnSpan(this.lbLogger, 2);
            this.lbLogger.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lbLogger.Font = new System.Drawing.Font("Verdana", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.lbLogger.FormattingEnabled = true;
            this.lbLogger.ItemHeight = 18;
            this.lbLogger.Location = new System.Drawing.Point(3, 103);
            this.lbLogger.Name = "lbLogger";
            this.lbLogger.Size = new System.Drawing.Size(194, 454);
            this.lbLogger.TabIndex = 1;
            // 
            // lblLeftBound
            // 
            this.lblLeftBound.AutoSize = true;
            this.lblLeftBound.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lblLeftBound.Font = new System.Drawing.Font("Verdana", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.lblLeftBound.Location = new System.Drawing.Point(3, 60);
            this.lblLeftBound.Name = "lblLeftBound";
            this.lblLeftBound.Size = new System.Drawing.Size(179, 20);
            this.lblLeftBound.TabIndex = 3;
            this.lblLeftBound.Text = "Цвет границы слева:";
            // 
            // btnRightButton
            // 
            this.btnRightButton.AutoSize = true;
            this.btnRightButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.btnRightButton.BackColor = System.Drawing.Color.Green;
            this.btnRightButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btnRightButton.Location = new System.Drawing.Point(188, 63);
            this.btnRightButton.Name = "btnRightButton";
            this.btnRightButton.Size = new System.Drawing.Size(9, 14);
            this.btnRightButton.TabIndex = 5;
            this.btnRightButton.Text = "  ";
            this.btnRightButton.UseVisualStyleBackColor = false;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(618, 461);
            this.Controls.Add(this.splitContainer1);
            this.KeyPreview = true;
            this.Name = "Form1";
            this.Text = "Нахождение общего пересечения полуплоскостей";
            this.WindowState = System.Windows.Forms.FormWindowState.Maximized;
            this.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Form1_KeyPress);
            ((System.ComponentModel.ISupportInitialize)(this.canvasHalfplanesIntersection)).EndInit();
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.ResumeLayout(false);

        }

      

        private System.Windows.Forms.PictureBox canvasHalfplanesIntersection;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.Button btnBuildPolygon;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Label lblDelay;
        private System.Windows.Forms.TextBox tbDelay;
        private System.Windows.Forms.ListBox lbLogger;
        private System.Windows.Forms.Label lblLeftBound;
        private System.Windows.Forms.Label lblRightBound;
        private System.Windows.Forms.Button btnRightButton;
        private System.Windows.Forms.Button button1;
    }
}

