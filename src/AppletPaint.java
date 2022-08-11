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
    boolean isDragMode, eraseState = false, isFreeHand = false;
    int shapeFill = 1;
    Color selColor;
    Button clearBtn, undoBtn, eraseBtn, doneErasingBtn, colorChangeIndic;
    Graphics2D g2;
    BasicStroke dashed;

    public void displayGUI() {
        // Change the font
        mainFont = new Font("DialogInput", Font.PLAIN, 17);

        // Create erase button
        eraseBtn = new Button("Eraser");
        eraseBtn.addActionListener(new BtnActions());
        eraseBtn.setFont(mainFont);
        eraseBtn.setBackground(Color.white);
        eraseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eraseBtn.setFocusable(false);
        add(eraseBtn);

        // Create clear button
        clearBtn = new Button("Clear");
        clearBtn.addActionListener(new BtnActions());
        clearBtn.setFont(mainFont);
        clearBtn.setBackground(Color.white);
        clearBtn.setFocusable(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(clearBtn);

        // Create Undo button
        undoBtn = new Button("Undo");
        undoBtn.addActionListener(new BtnActions());
        undoBtn.setFont(mainFont);
        undoBtn.setBackground(Color.white);
        undoBtn.setFocusable(false);
        undoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(undoBtn);

        // Creat shapes menu
        shapeMenu = new Choice();
        shapeMenu.addItem("Line");
        shapeMenu.addItem("Rectangle");
        shapeMenu.addItem("Oval");
        shapeMenu.addItem("Rounded Rectangle");
        shapeMenu.addItem("FreeHand");
        shapeMenu.setFont(mainFont);
        shapeMenu.setFocusable(false);
        add(shapeMenu);

        // Creat colors menu
        colorMenu = new Choice();
        colorMenu.addItem("Black");
        colorMenu.addItem("Red");
        colorMenu.addItem("Yellow");
        colorMenu.addItem("Magenta");
        colorMenu.addItem("Green");
        colorMenu.addItem("Orange");
        colorMenu.addItem("Blue");
        colorMenu.setFont(mainFont);
        colorMenu.setFocusable(false);
        add(colorMenu);

        // Create a little color indication
        colorChangeIndic = new Button("");
        colorChangeIndic.setBackground(Color.BLACK);
        colorChangeIndic.setEnabled(false);
        add(colorChangeIndic);

        // Creat fill choices menu
        fillMenu = new Choice();
        fillMenu.addItem("Filled");
        fillMenu.addItem("Hollow");
        fillMenu.addItem("Dotted");
        fillMenu.setFont(mainFont);
        fillMenu.setFocusable(false);
        add(fillMenu);

        // Creat a button for when done erasing
        doneErasingBtn = new Button("Done");
        doneErasingBtn.addActionListener(new BtnActions());
        doneErasingBtn.setFont(mainFont);
        doneErasingBtn.setBackground(Color.white);
        doneErasingBtn.setFocusable(false);
        doneErasingBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        doneErasingBtn.setEnabled(false);
        add(doneErasingBtn);

        // Creat an image as a canvas to draw
        drawCanvas = createImage(getSize().width, getSize().height);
        drawGraphics = drawCanvas.getGraphics();
    }

    public void init() {
        // ArrayList to store all shapes drawn on the screen, so we can draw multiple shapes simultaneously
        shapesArr = new ArrayList<>();

        // Set the stroke settings (stroke width and type)
        float[] dashLine = {10.0f};
        dashed = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dashLine, 10.0f);

        // Display the GUI
        displayGUI();

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
            case "Yellow":
                selColor = Color.YELLOW;
                break;
            case "Magenta":
                selColor = Color.MAGENTA;
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
        // Change the indicator color
        colorChangeIndic.setBackground(selColor);

        switch (shapeChoice) {
            case "Line":
                // Construct a new line as a new shape using the start point same as other shapes
                isFreeHand = false;
                currentShape = new Line(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "Rectangle":
                isFreeHand = false;
                currentShape = new Rectangle(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "Oval":
                isFreeHand = false;
                currentShape = new Oval(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "Rounded Rectangle":
                isFreeHand = false;
                currentShape = new RoundRectangle(startPoint.x, startPoint.y, selColor, shapeFill);
                break;
            case "FreeHand":
                isFreeHand = true;
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

        // Get points as Arraylist for free-hand
        currentShape.setPointsArr(e.getPoint());

        // Update the canvas if free-hand mode is off to ensure smooth hand drawing
        if (!isFreeHand) getGraphics().drawImage(drawCanvas, 0, 0, null);

        // Draw while mouse is dragging
        currentShape.drawWhileDragging(getGraphics());
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
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
            if (e.getSource() == eraseBtn) {
                if (isFreeHand) {
                    eraseState = true;
                    eraseBtn.setBackground(Color.RED);
                }
            }
            if (e.getSource() == doneErasingBtn) {
                if (eraseState) {
                    eraseState = false;
                    eraseBtn.setBackground(Color.WHITE);
                }
            }
        }
    }

    // Creat an abstract parent class, so we can initiate multiple shapes objects
    abstract class MainShape {
        // Declare the required variables for each shape
        protected Point startPoint, currentPoint;
        protected Color shapeColor;
        protected int filledState, x, y, width, height;

        // Declare the needed methods for drawing as abstract
        abstract void shapeDraw(Graphics g);

        abstract void drawWhileDragging(Graphics g);

        abstract void setDragPoint(int x, int y);

        // Get the right dimensions, so we can draw in every direction
        protected void setDimensions() {
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
        }

        public void setPointsArr(Point point) {
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

    class Line extends MainShape {
        Line() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        // Create a parametrized constructor to set the initial values for the variables
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

        // Created a method to draw the shape by the specified fill state
        private void specifiedShape(Graphics g) {
            // Initialize a new 2D graphic object to support dash lines
            g2 = (Graphics2D) g;
            // Depending on the fill state number draw the desired shape
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
        public void setPointsArr(Point point) {
            pointsVec.add(point);
        }

        void drawWhileDragging(Graphics g) {
            if (eraseState) shapeColor = Color.WHITE;
            g.setColor(shapeColor);
            // For each point in the ArrayList draw an oval using them as coordinates
            for (Point c : pointsVec) {
                g.fillOval(c.x, c.y, 15, 15);
            }

        }

        public void shapeDraw(Graphics g) {
            if (eraseState) shapeColor = Color.WHITE;
            g.setColor(shapeColor);
            for (Point c : pointsVec) {
                g.fillOval(c.x, c.y, 15, 15);
            }
        }

        void setDragPoint(int x, int y) {
        }

    }

    class Rectangle extends MainShape {
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
            setDimensions();
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
    }

    class RoundRectangle extends MainShape {
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
            setDimensions();
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
    }


    class Oval extends MainShape {
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
            setDimensions();
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
    }
}