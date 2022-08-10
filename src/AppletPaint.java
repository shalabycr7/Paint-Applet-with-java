import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AppletPaint extends Applet implements MouseListener, MouseMotionListener {
    Shape shape;
    Point startPoint, dragPoint;
    Font mainFont;
    ArrayList<Shape> shapes;
    Choice shapeChoice, colorChoice, fillChoice;
    Image drawingImage;
    Graphics drawGraphics;
    String shapeString, colorString, fillString;
    boolean isDragMode, filledShape, eraseState = false;
    Color selColor;
    Button clearBtn, undoBtn, eraseBtn, doneErasingBtn;

    public void displayGUI() {
        mainFont = new Font("DialogInput", Font.PLAIN, 17);

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
        shapeChoice = new Choice();
        shapeChoice.addItem("Line");
        shapeChoice.addItem("Rectangle");
        shapeChoice.addItem("Oval");
        shapeChoice.addItem("Rounded Rectangle");
        shapeChoice.addItem("FreeHand");
        shapeChoice.setFont(mainFont);
        add(shapeChoice);
        // Creat colors menu
        colorChoice = new Choice();
        colorChoice.addItem("Black");
        colorChoice.addItem("Red");
        colorChoice.addItem("Green");
        colorChoice.addItem("Orange");
        colorChoice.addItem("Blue");
        colorChoice.setFont(mainFont);
        add(colorChoice);
        // Creat fill choices menu
        fillChoice = new Choice();
        fillChoice.addItem("Filled");
        fillChoice.addItem("Hollow");
        fillChoice.setFont(mainFont);
        add(fillChoice);
        // Creat a button for when done erasing
        doneErasingBtn = new Button("Done");
        doneErasingBtn.addActionListener(new BtnActions());
        doneErasingBtn.setFont(mainFont);
        add(doneErasingBtn);
        doneErasingBtn.setEnabled(false);

        // Creat an image as a canvas to draw
        drawingImage = createImage(getSize().width, getSize().height);
        drawGraphics = drawingImage.getGraphics();
    }

    public void init() {
        // ArrayList to store all shapes drawn on the screen, so we can draw multiple shapes simultaneously
        shapes = new ArrayList<>();

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
        shapeString = shapeChoice.getSelectedItem();
        colorString = colorChoice.getSelectedItem();
        fillString = fillChoice.getSelectedItem();

        // Set the state of the erase button
        doneErasingBtn.setEnabled(eraseState);

        // Get the values of the mouse pointer
        startPoint.x = e.getX();
        startPoint.y = e.getY();

        // Switch on the menu values
        switch (fillString) {
            case "Filled":
                filledShape = true;
                break;
            case "Hollow":
                filledShape = false;
                break;
        }
        switch (colorString) {
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
        switch (shapeString) {
            case "Line":
                // Construct a new line as a new shape using the start point same as other shapes
                shape = new Line(startPoint.x, startPoint.y, selColor);
                break;
            case "Rectangle":
                shape = new Rectangle(startPoint.x, startPoint.y, selColor, filledShape);
                break;
            case "Oval":
                shape = new Oval(startPoint.x, startPoint.y, selColor, filledShape);
                break;
            case "Rounded Rectangle":
                shape = new RoundRectangle(startPoint.x, startPoint.y, selColor, filledShape);
                break;
            case "FreeHand":
                shape = new FreeShape(startPoint.x, startPoint.y, selColor, eraseState);
                break;
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (isDragMode) {
            // Add the current shape to ArrayList
            isDragMode = false;
            shapes.add(shape);
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
        shape.setDragPoint(dragPoint.x, dragPoint.y);
        // Get points as list for free hand
        shape.setPointsVec(e.getPoint());

        // Update the image
        getGraphics().drawImage(drawingImage, 0, 0, null);

        // Method to draw while mouse is dragging
        shape.drawWhileDragging(getGraphics());
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {

        // Creat an image canvas if none existed
        if (drawingImage == null) {
            drawingImage = createImage(getSize().width, getSize().height);
            drawGraphics = drawingImage.getGraphics();
        }
        // Draw every shape in the ArrayList
        for (Shape s : shapes) {
            s.draw(drawGraphics);
        }
        // paint the canvas image to the applet viewing window
        g.drawImage(drawingImage, 0, 0, this);
    }

    // Just to demonstrate another way to add event listeners
    class BtnActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // clear the screen
            if (e.getSource() == clearBtn) {
                shapes.clear();
                drawGraphics.setColor(Color.WHITE);
                drawGraphics.fillRect(0, 0, getSize().width, getSize().height);
                repaint();
            } else if (e.getSource() == undoBtn) {
                // check that the ArrayList is not empty
                // remove the last drawn shape from the ArrayList then redraw
                if (shapes.size() != 0) shapes.remove(shapes.size() - 1);
                drawGraphics.setColor(Color.WHITE);
                drawGraphics.fillRect(0, 0, getSize().width, getSize().height);
                repaint();
            }
            if (e.getSource() == eraseBtn) eraseState = true;
            if (e.getSource() == doneErasingBtn) eraseState = false;
        }
    }

    abstract class Shape {
        protected Point startPoint, currentPoint;
        protected Color shapeColor;
        protected boolean filledState;

        abstract void draw(Graphics g);

        abstract void drawWhileDragging(Graphics g);

        abstract void setDragPoint(int x, int y);

        public void setPointsVec(Point point) {
        }
    }

    class Line extends Shape {
        Line() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        Line(int x1, int y1, Color c) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
        }

        void drawWhileDragging(Graphics g) {
            g.setColor(shapeColor);
            g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
        }

        public void draw(Graphics g) {
            g.setColor(shapeColor);
            g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
        }

        void setDragPoint(int x, int y) {
            this.currentPoint.x = x;
            this.currentPoint.y = y;
        }

    }

    class FreeShape extends Shape {
        private boolean eraseState;
        private final ArrayList<Point> pointsVec = new ArrayList<>();

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

        public void setPointsVec(Point point) {
            pointsVec.add(point);
        }

        void drawWhileDragging(Graphics g) {
            if (eraseState) shapeColor = Color.WHITE;
            g.setColor(shapeColor);
            for (Point c : pointsVec) {
                g.fillOval(c.x, c.y, 20, 20);
            }

        }

        public void draw(Graphics g) {
            if (eraseState) shapeColor = Color.WHITE;
            g.setColor(shapeColor);
            for (Point c : pointsVec) {
                g.fillOval(c.x, c.y, 20, 20);
            }
        }

        void setDragPoint(int x, int y) {
        }

    }

    class Rectangle extends Shape {
        private int x;
        private int y;
        private int width;
        private int height;

        Rectangle() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        Rectangle(int x1, int y1, Color c, boolean filledState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.filledState = filledState;
        }

        void drawWhileDragging(Graphics g) {
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
            if (filledState) g.fillRect(getX(), getY(), getWidth(), getHeight());
            else g.drawRect(getX(), getY(), getWidth(), getHeight());
        }

        public void draw(Graphics g) {
            g.setColor(shapeColor);
            if (filledState) g.fillRect(getX(), getY(), getWidth(), getHeight());
            else g.drawRect(getX(), getY(), getWidth(), getHeight());
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

    class RoundRectangle extends Shape {
        private int x;
        private int y;
        private int width;
        private int height;

        RoundRectangle() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        RoundRectangle(int x1, int y1, Color c, boolean filledState) {
            this();
            this.startPoint.x = x1;
            this.startPoint.y = y1;
            this.shapeColor = c;
            this.filledState = filledState;
        }

        void drawWhileDragging(Graphics g) {
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
            if (filledState) g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
            else g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
        }

        public void draw(Graphics g) {
            g.setColor(shapeColor);
            if (filledState) g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
            else g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 30, 30);
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


    class Oval extends Shape {
        private int x;
        private int y;
        private int width;
        private int height;

        Oval() {
            startPoint = new Point(0, 0);
            currentPoint = new Point(0, 0);
        }

        Oval(int x1, int y1, Color c, boolean filledState) {
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
            // make sure to be able to draw in all directions
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
            if (filledState) g.fillOval(getX(), getY(), getWidth(), getHeight());
            else g.drawOval(getX(), getY(), getWidth(), getHeight());
        }

        public void draw(Graphics g) {
            g.setColor(shapeColor);
            if (filledState) g.fillOval(getX(), getY(), getWidth(), getHeight());
            else g.drawOval(getX(), getY(), getWidth(), getHeight());
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