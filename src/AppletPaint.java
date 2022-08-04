import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.Dimension;
import java.util.Objects;

public class AppletPaint extends Applet {
    Font f1;
    String[] shapesArr = {"Rect", "Oval", "Line"};
    String drawShape;
    // width and height variables
    int width, height;
    // selected color from palette
    Color selectedColor;
    // buttons and buttons states
    Button blackColorBtn, blueColorBtn, redColorBtn, greenColorBtn, freeHandBtn, drawRecBtn, drawCircBtn;
    boolean freeHandSt = false, shapeSelectionState = false;
    private Point p1;
    private Point p2;
    private Rectangle2D rectangle;
    public Ellipse2D circle;

    private static final int D_W = 500;
    private static final int D_H = 500;

    public void init() {
        width = getWidth();
        height = getHeight();
        f1 = new Font("Arial", Font.BOLD, 18);
        // set the layout to null, so we can position the elements
        setLayout(null);
        // draw the buttons on screen
        setpalette();
        // add event listener to the mouse movement
        MouseMove bt = new MouseMove();
        addMouseMotionListener(bt);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                p1 = e.getPoint();

            }
        });


    }

    public void setpalette() {
        // creating the buttons & adding them to screen with click event listener
        blackColorBtn = new Button("");
        blackColorBtn.setBounds(width - 250, 50, 50, 50);
        blackColorBtn.setBackground(Color.black);
        add(blackColorBtn);
        blackColorBtn.addActionListener(new ColorChange());

        blueColorBtn = new Button("");
        blueColorBtn.setBounds(width - 250 + 50, 50, 50, 50);
        blueColorBtn.setBackground(Color.blue);
        add(blueColorBtn);
        blueColorBtn.addActionListener(new ColorChange());

        redColorBtn = new Button("");
        redColorBtn.setBounds(width - 250 + 100, 50, 50, 50);
        redColorBtn.setBackground(Color.red);
        add(redColorBtn);
        redColorBtn.addActionListener(new ColorChange());

        greenColorBtn = new Button("");
        greenColorBtn.setBounds(width - 250 + 150, 50, 50, 50);
        greenColorBtn.setBackground(Color.green);
        add(greenColorBtn);
        greenColorBtn.addActionListener(new ColorChange());

        freeHandBtn = new Button("Free Hand");
        freeHandBtn.setBounds(50, 30, 100, 30);
        add(freeHandBtn);
        freeHandBtn.addActionListener(new ColorChange());

        drawRecBtn = new Button("Rec");
        drawRecBtn.setBounds(width - 250 - 250, 50, 50, 50);
        add(drawRecBtn);
        drawRecBtn.addActionListener(new ColorChange());

        drawCircBtn = new Button("Circle");
        drawCircBtn.setBounds(width - 250 - 250 + 50, 50, 50, 50);
        add(drawCircBtn);
        drawCircBtn.addActionListener(new ColorChange());

    }

    public void drawShape() {

        if (Objects.equals(drawShape, shapesArr[0])) {
            okp.drRect();
        } else {
            okp.drCirc();
        }
        repaint();
    }

    public void paint(Graphics g) {
        g.setFont(f1);
        // draw the top section
        g.drawLine(0, 200, width, 200);
        g.drawRect(width - 250, 50, 200, 100);
        g.drawRect(width - 250 - 250, 50, 200, 100);
        g.setColor(selectedColor);

        Graphics2D g2 = (Graphics2D) g;
        if (rectangle != null) {
            g2.draw(rectangle);
        }
        if (circle != null) {
            g2.draw(circle);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(D_W, D_H);
    }

    // change the selected color when pressing the corresponding color
    class ColorChange implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (ev.getSource() == blackColorBtn) {
                System.out.println("Black color selected");
                selectedColor = Color.black;

            } else if (ev.getSource() == blueColorBtn) {
                System.out.println("Blue color selected");
                selectedColor = Color.blue;

            } else if (ev.getSource() == redColorBtn) {
                System.out.println("Red color selected");
                selectedColor = Color.red;

            } else if (ev.getSource() == greenColorBtn) {
                System.out.println("Green color selected");
                selectedColor = Color.green;

            } else if (ev.getSource() == freeHandBtn) {
                System.out.println("Free hand is active");
                freeHandSt = true;
                shapeSelectionState = false;

            } else if (ev.getSource() == drawRecBtn) {
                System.out.println("Draw rect is active");
                freeHandSt = false;
                shapeSelectionState = true;
                drawShape = shapesArr[0];

            } else if (ev.getSource() == drawCircBtn) {
                System.out.println("Draw circle is active");
                freeHandSt = false;
                shapeSelectionState = true;
                drawShape = shapesArr[1];

            }
        }
    }

    // event listener to mouse
    DrawingShapes okp = new DrawingShapes();

    class MouseMove implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            // when the free hand button is clicked go to free hand mode
            if (freeHandSt) {
                Graphics g = getGraphics();
                g.setColor(selectedColor);
                g.fillOval(e.getX(), e.getY(), 40, 40);
            }
            if (shapeSelectionState) {
                p2 = e.getPoint();
                //drRect();
                drawShape();
                //repaint();
            }

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public boolean isPointTwoInQuadOne(Point p1, Point p2) {
        return p1.x >= p2.x && p1.y >= p2.y;
    }

    class DrawingShapes {
        public void drRect() {
            rectangle = new Rectangle2D.Double(p1.x, p1.y, p1.x - p1.x, p1.y - p1.y);

            if (isPointTwoInQuadOne(p1, p2)) {
                rectangle.setRect(p2.x, p2.y, p1.x - p2.x, p1.y - p2.y);
            } else {
                rectangle.setRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
            }
        }

        public void drCirc() {
            circle = new Ellipse2D.Double(p1.x, p1.y, p1.x - p1.x, p1.y - p1.y);

            if (isPointTwoInQuadOne(p1, p2)) {
                circle.setFrame(p2.x, p2.y, p1.x - p2.x, p1.y - p2.y);
            } else {
                circle.setFrame(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
            }
        }
    }
}
