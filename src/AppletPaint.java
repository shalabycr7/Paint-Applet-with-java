import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AppletPaint extends Applet implements MouseListener, MouseMotionListener {
    MainShape currentShape;
    Point startPoint, dragPoint;
    Font mainFont;
    ArrayList<MainShape> shapesArr;
    Choice shapeMenu, colorMenu, fillMenu;
    Image drawCanvas;
    Graphics drawGraphics;
    String shapeChoice, colorChoice, fillChoice;
    boolean isDragMode, eraseState = false;
    int shapeFill = 1;
    Color selColor;
    Button clearBtn, undoBtn, eraseBtn, doneErasingBtn;
    Graphics2D g2;
    BasicStroke dashed;

    public void displayGUI() {
        // Change the font
        mainFont = new Font("DialogInput", Font.PLAIN, 17);

        // Create erase button
        eraseBtn = new Button("Eraser");
        eraseBtn.addActionListener(new BtnActions());
        eraseBtn.setFont(mainFont);
        add(eraseBtn);

        // Create clear button
        clearBtn = new Button("Clear");
        clearBtn.addActionListener(new BtnActions());
        clearBtn.setFont(mainFont);
        add(clearBtn);

        // Create Undo button
        undoBtn = new Button("Undo");
        undoBtn.addActionListener(new BtnActions());
        undoBtn.setFont(mainFont);
        add(undoBtn);

        // Creat shapes menu
        shapeMenu = new Choice();
        shapeMenu.addItem("Line");
        shapeMenu.addItem("Rectangle");
        shapeMenu.addItem("Oval");
        shapeMenu.addItem("Rounded Rectangle");
        shapeMenu.addItem("FreeHand");
        shapeMenu.setFont(mainFont);
        add(shapeMenu);

        // Creat colors menu
        colorMenu = new Choice();
        colorMenu.addItem("Black");
        colorMenu.addItem("Red");
        colorMenu.addItem("Green");
        colorMenu.addItem("Orange");
        colorMenu.addItem("Blue");
        colorMenu.setFont(mainFont);
        add(colorMenu);

        // Creat fill choices menu
        fillMenu = new Choice();
        fillMenu.addItem("Filled");
        fillMenu.addItem("Hollow");
        fillMenu.addItem("Dotted");
        fillMenu.setFont(mainFont);
        add(fillMenu);

        // Creat a button for when done erasing
        doneErasingBtn = new Button("Done");
        doneErasingBtn.addActionListener(new BtnActions());
        doneErasingBtn.setFont(mainFont);
        add(doneErasingBtn);
        doneErasingBtn.setEnabled(false);

        // Creat an image as a canvas to draw
        drawCanvas = createImage(getSize().width, getSize().height);
        drawGraphics = drawCanvas.getGraphics();
    }

    public void init() {
        // ArrayList to store all shapes drawn on the screen, so we can draw multiple shapes simultaneously
        shapesArr = new ArrayList<>();

        // Display the GUI
        displayGUI();

        // Set the stroke settings
        float[] dash1 = {10.0f};
        dashed = new BasicStroke(1.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);


        // Initiate the first points
        startPoint = new Point(0, 0);
        dragPoint = new Point(0, 0);

        // Add mouse event listeners
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        // Get the values of the choice menu when the mouse is clicked
        shapeChoice = shapeMenu.getSelectedItem();
        colorChoice = colorMenu.getSelectedItem();
        fillChoice = fillMenu.getSelectedItem();

        // Set the state of the erase button
        doneErasingBtn.setEnabled(eraseState);

        // Get the values of the mouse pointer
        startPoint.x = e.getX();
        startPoint.y = e.getY();

        // Switch on the menu values
        switch (fillChoice) {
            case "Filled":
                shapeFill = 1;
                break;
            case "Hollow":
                shapeFill = 2;
                break;
            case "Dotted":
                shapeFill = 3;
                break;
        }
        switch (colorChoice) {
            case "Black":
                selColor = Color.BLACK;
                break;
            case "Red":
                selColor = Color.RED;
                break;
            case "Green":
                selColor = Color.GREEN;
                break;
            case "Orange":
                selColor = Color.ORANGE;
                break;
            case "Blue":
                selColor = Color.BLUE;
                break;
        }
        switch (shapeChoice) {
            case "Line":
                // Construct a new line as a new shape using the start point same as other shapes
                currentShape = new Line(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "Rectangle":
                currentShape = new Rectangle(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "Oval":
                currentShape = new Oval(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "Rounded Rectangle":
                currentShape = new RoundRectangle(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "FreeHand":
                currentShape = new FreeShape(startPoint.x, startPoint.y, selColor, eraseState);
                break;
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (isDragMode) {
            // Add the current shape to ArrayList
            isDragMode = false;
            shapesArr.add(currentShape);
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        // Indicate the drag mode is on
        isDragMode = true;

        // Get the mouse pointer location while dragging
        dragPoint.x = e.getX();
        dragPoint.y = e.getY();
        currentShape.setDragPoint(dragPoint.x, dragPoint.y);

        // Get points as list for free hand
        currentShape.setPointsVec(e.getPoint());

        // Update the canvas
        getGraphics().drawImage(drawCanvas, 0, 0, null);

        // Draw while mouse is dragging
        currentShape.drawWhileDragging(getGraphics());
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {

        // Creat an image canvas if none existed
        if (drawCanvas == null) {
            drawCanvas = createImage(getSize().width, getSize().height);
            drawGraphics = drawCanvas.getGraphics();
        }
        // Draw every shape in the ArrayList
        for (MainShape s : shapesArr) {
            s.shapeDraw(drawGraphics);
        }
        // paint the canvas image to the applet viewing window
        g.drawImage(drawCanvas, 0, 0, this);
    }

    // Just to demonstrate another way to add event listeners
    class BtnActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Clear the screen
            if (e.getSource() == clearBtn) {
                shapesArr.clear();
                drawGraphics.setColor(Color.WHITE);
                drawGraphics.fillRect(0, 0, getSize().width, getSize().height);
                repaint();
            } else if (e.getSource() == undoBtn) {
                // Check that the ArrayList is not empty
                // Remove the last drawn shape from the ArrayList then redraw
                if (shapesArr.size() != 0) shapesArr.remove(shapesArr.size() - 1);
                drawGraphics.setColor(Color.WHITE);
                drawGraphics.fillRect(0, 0, getSize().width, getSize().height);
                repaint();
            }
            // Enable erase button & done button
            if (e.getSource() == eraseBtn) eraseState = true;
            if (e.getSource() == doneErasingBtn) eraseState = false;
        }
    }

    // Creat an abstract parent class, so we can initiate multiple shapes object
    abstract class MainShape {
        // Declare the required variables for each shape
        protected Point startPoint, currentPoint;
        protected Color shapeColor;
        protected int filledState;

        // Declare the needed methods as abstract
        abstract void shapeDraw(Graphics g);

        abstract void drawWhileDragging(Graphics g);

        abstract void setDragPoint(int x, int y);

        public void setPointsVec(Point point) {
        }
    }

    class Line extends MainShape {
        Line() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        // Create a parametrized constructor to set the initial values for te variables
        Line(int x1, int y1, Color c, int filledState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.filledState = filledState;
        }

        // Override the parent class methods to draw that specific shape
        void drawWhileDragging(Graphics g) {
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        public void shapeDraw(Graphics g) {
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        private void specifiedShape(Graphics g) {
            g2 = (Graphics2D) g;
            if (filledState == 1 || filledState == 2) {
                g2.setStroke(new BasicStroke(0));
                g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
            } else {
                g2.setStroke(dashed);
                g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
            }
        }

        void setDragPoint(int x, int y) {
            this.currentPoint.x = x;
            this.currentPoint.y = y;
        }

    }

    class FreeShape extends MainShape {
        // Create an ArrayList to store the dragging coordinates as points
        private final ArrayList<Point> pointsVec = new ArrayList<>();
        private boolean eraseState;

        FreeShape() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        FreeShape(int x1, int y1, Color c, boolean eraseState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.eraseState = eraseState;
        }

        // Add the points to the ArrayList while dragging
        public void setPointsVec(Point point) {
            pointsVec.add(point);
        }

        void drawWhileDragging(Graphics g) {
            if (eraseState) shapeColor = Color.WHITE;
            g.setColor(shapeColor);
            // For each point in the ArrayList draw an oval using them as coordinates
            for (Point c : pointsVec) {
                g.fillOval(c.x, c.y, 20, 20);
            }

        }

        public void shapeDraw(Graphics g) {
            if (eraseState) shapeColor = Color.WHITE;
            g.setColor(shapeColor);
            for (Point c : pointsVec) {
                g.fillOval(c.x, c.y, 20, 20);
            }
        }

        void setDragPoint(int x, int y) {
        }

    }

    class Rectangle extends MainShape {
        private int x;
        private int y;
        private int width;
        private int height;

        Rectangle() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        Rectangle(int x1, int y1, Color c, int filledState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.filledState = filledState;
        }

        void drawWhileDragging(Graphics g) {
            //System.out.println(dotState);
            // Get the dimensions
            setX(startPoint.x);
            setY(startPoint.y);
            setWidth(currentPoint.x - startPoint.x);
            setHeight(currentPoint.y - startPoint.y);
            // Make sure to be able to draw in all directions
            if (getWidth() < 0) {
                setWidth(-getWidth());
                setX(getX() - getWidth() + 1);
                if (getX() < 0) {
                    setWidth(getWidth() + getX());
                    setX(0);
                }
            }
            if (getHeight() < 0) {
                setHeight(-getHeight());
                setY(getY() - getHeight() + 1);
                if (getY() < 0) {
                    setHeight(getHeight() + getY());
                    setY(0);
                }
            }
            g.setColor(shapeColor);

            specifiedShape(g);
        }

        public void shapeDraw(Graphics g) {
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        private void specifiedShape(Graphics g) {
            g2 = (Graphics2D) g;
            if (filledState == 1) {
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            } else if (filledState == 2) {
                g2.setStroke(new BasicStroke(0));
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            } else {
                g2.setStroke(dashed);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
        }

        void setDragPoint(int x, int y) {
            this.currentPoint.x = x;
            this.currentPoint.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    class RoundRectangle extends MainShape {
        private int x;
        private int y;
        private int width;
        private int height;

        RoundRectangle() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        RoundRectangle(int x1, int y1, Color c, int filledState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.filledState = filledState;
        }

        void drawWhileDragging(Graphics g) {
            setX(startPoint.x);
            setY(startPoint.y);
            setWidth(currentPoint.x - startPoint.x);
            setHeight(currentPoint.y - startPoint.y);
            if (getWidth() < 0) {
                setWidth(-getWidth());
                setX(getX() - getWidth() + 1);
                if (getX() < 0) {
                    setWidth(getWidth() + getX());
                    setX(0);
                }
            }
            if (getHeight() < 0) {
                setHeight(-getHeight());
                setY(getY() - getHeight() + 1);
                if (getY() < 0) {
                    setHeight(getHeight() + getY());
                    setY(0);
                }
            }
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        void shapeDraw(Graphics g) {
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        private void specifiedShape(Graphics g) {
            g2 = (Graphics2D) g;
            if (filledState == 1) {
                g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
            } else if (filledState == 2) {
                g2.setStroke(new BasicStroke(0));
                g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
            } else {
                g2.setStroke(dashed);
                g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
            }
        }

        void setDragPoint(int x, int y) {
            this.currentPoint.x = x;
            this.currentPoint.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }


    class Oval extends MainShape {
        private int x;
        private int y;
        private int width;
        private int height;

        Oval() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        Oval(int x1, int y1, Color c, int filledState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.filledState = filledState;
        }

        void drawWhileDragging(Graphics g) {
            setX(startPoint.x);
            setY(startPoint.y);
            setWidth(currentPoint.x - startPoint.x);
            setHeight(currentPoint.y - startPoint.y);
            if (getWidth() < 0) {
                setWidth(-getWidth());
                setX(getX() - getWidth() + 1);
                if (getX() < 0) {
                    setWidth(getWidth() + getX());
                    setX(0);
                }
            }
            if (getHeight() < 0) {
                setHeight(-getHeight());
                setY(getY() - getHeight() + 1);
                if (getY() < 0) {
                    setHeight(getHeight() + getY());
                    setY(0);
                }
            }
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        void shapeDraw(Graphics g) {
            g.setColor(shapeColor);
            specifiedShape(g);
        }

        private void specifiedShape(Graphics g) {
            g2 = (Graphics2D) g;
            if (filledState == 1) {
                g.fillOval(getX(), getY(), getWidth(), getHeight());
            } else if (filledState == 2) {
                g2.setStroke(new BasicStroke(0));
                g.drawOval(getX(), getY(), getWidth(), getHeight());
            } else {
                g2.setStroke(dashed);
                g.drawOval(getX(), getY(), getWidth(), getHeight());
            }
        }

        void setDragPoint(int x, int y) {
            this.currentPoint.x = x;
            this.currentPoint.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}