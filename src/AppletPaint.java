import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class AppletPaint extends Applet implements MouseListener, MouseMotionListener {

    Shape shape;
    Point startPoint, dragPoint;
    ArrayList<Shape> shapes;
    Choice shapeChoice, colorChoice, fillChoice;
    Image drawingImage;
    Graphics drawGraphics;
    String shapeString, colorString, fillString;
    boolean isDragMode, filledShape;
    Color selColor;
    Button clearBtn;

    public void init() {
        shapes = new ArrayList<>();
        clearBtn = new Button("Clear");
        clearBtn.addActionListener(new BtnActions());
        add(clearBtn);
        shapeChoice = new Choice();
        shapeChoice.addItem("Line");
        shapeChoice.addItem("Rectangle");
        shapeChoice.addItem("Oval");
        shapeChoice.addItem("FreeHand");
        add(shapeChoice);

        colorChoice = new Choice();
        colorChoice.addItem("Red");
        colorChoice.addItem("Green");
        colorChoice.addItem("Blue");
        add(colorChoice);

        fillChoice = new Choice();
        fillChoice.addItem("Filled");
        fillChoice.addItem("Hollow");
        add(fillChoice);

        drawingImage = createImage(getSize().width, getSize().height);
        drawGraphics = drawingImage.getGraphics();

        System.out.println("set up image");

        startPoint = new Point(0, 0);
        dragPoint = new Point(0, 0);
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

        System.out.println("Pressed");
        shapeString = shapeChoice.getSelectedItem();
        colorString = colorChoice.getSelectedItem();
        fillString = fillChoice.getSelectedItem();
        startPoint.x = e.getX();
        startPoint.y = e.getY();
        switch (fillString) {
            case "Filled":
                filledShape = true;
                break;
            case "Hollow":
                filledShape = false;
                break;
        }
        switch (colorString) {
            case "Red":
                selColor = Color.RED;
                break;
            case "Green":
                selColor = Color.GREEN;
                break;
            case "Blue":
                selColor = Color.BLUE;
                break;
        }
        switch (shapeString) {
            case "Line":
                shape = new Line(startPoint.x, startPoint.y, selColor); // i construct a new line using the start point (the point at which the mouse is pressed)
                break;
            case "Rectangle":
                shape = new Rectangle(startPoint.x, startPoint.y, selColor, filledShape);
                break;
            case "Oval":
                shape = new Oval(startPoint.x, startPoint.y, selColor, filledShape);
                break;
            case "FreeHand":
                shape = new FreeShape();
                break;
        }


    }

    public void mouseReleased(MouseEvent e) {
        if (isDragMode) {
            shapes.add(shape);
            isDragMode = false;
        }
        repaint();

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        isDragMode = true;
        dragPoint.x = e.getX();
        dragPoint.y = e.getY();

        switch (shapeString) {
            case "FreeHand":
                shape = new FreeShape();
                break;
        }
        shape.setDragPoint(dragPoint.x, dragPoint.y);  // set the drag points to the already created shape

        getGraphics().drawImage(drawingImage, 0, 0, null);
        shape.drawWhileDragging(getGraphics()); // i call this method to draw while mouse is dragging
    }

    public void paint(Graphics g) {

        update(g);
    }

    public void update(Graphics g) {

        // create an off-screen graphics drawing environment if none
        //existed
        // or if the user resized the applet drawing area to a different
        // size
        if (drawingImage == null) {

            System.out.println("Image is Null");
            drawingImage = createImage(getSize().width, getSize().height);
            drawGraphics = drawingImage.getGraphics();
        }

        for (Shape s : shapes)
            s.draw(drawGraphics);

        // paint the offscreen image to the applet viewing window
        g.drawImage(drawingImage, 0, 0, this);

    }

    class BtnActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clearBtn) {
                // clear the screen
                System.out.println("Clearing");
                shapes.clear();
                drawGraphics.setColor(Color.WHITE);
                drawGraphics.fillRect(0, 0, getSize().width, getSize().height);
                repaint();
            }
        }
    }
}


abstract class Shape {

    Color shapeColor;
    boolean filled;

    abstract void draw(Graphics g);

    void drawWhileDragging(Graphics g) {
    }

    void setDragPoint(int x, int y) {
    }
}

class Line extends Shape {

    private Point startPoint;
    private Point currentPoint;
    private Color shapeColor;


    public Point getStartPoint() {
        return startPoint;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public void setStartPoint(Point point) {
        this.startPoint = point;
    }

    public void setCurrentPoint(Point point) {
        this.currentPoint = point;
    }

    void drawWhileDragging(Graphics g) {
        g.setColor(Color.ORANGE);
        g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
        g.setColor(Color.BLACK);
    }

    public void draw(Graphics g) {
        g.setColor(shapeColor);
        g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
    }

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

    void setDragPoint(int x, int y) {
        this.currentPoint.x = x;
        this.currentPoint.y = y;
        System.out.println("Current-X:" + currentPoint.x + " currentPoint-Y" + currentPoint.y);
        System.out.println("start-X:" + startPoint.x + " startPoint-Y" + startPoint.y);
    }

}

class FreeShape extends Shape {

    private ArrayList<Point> dragPoints = new ArrayList<Point>();

    public ArrayList<Point> getDragPoints() {
        return dragPoints;
    }

    public void setDragPoints(Point point) {
        dragPoints.add(point);
    }

    public void draw(Graphics g) {
        //g.fillOval();
    }

    public FreeShape() {
    }
}


class Rectangle extends Shape {
    private Point startPoint, currentPoint;
    private Color shapeColor;
    private int x, y, width, height;
    private boolean filledState;

    void drawWhileDragging(Graphics g) {
        x = startPoint.x;
        y = startPoint.y;
        width = currentPoint.x - startPoint.x;
        height = currentPoint.y - startPoint.y;
        // make sure to be able to draw in all directions
        if (width < 0) {
            width = -width;
            x = x - width + 1;
            if (x < 0) {
                width += x;
                x = 0;
            }
        }
        if (height < 0) {
            height = -height;
            y = y - height + 1;
            if (y < 0) {
                height += y;
                y = 0;
            }
        }
        g.setColor(shapeColor);
        if (filledState)
            g.fillRect(x, y, width, height);
        else
            g.drawRect(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(shapeColor);
        if (filledState)
            g.fillRect(x, y, width, height);
        else
            g.drawRect(x, y, width, height);
    }

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

    void setDragPoint(int x, int y) {
        this.currentPoint.x = x;
        this.currentPoint.y = y;
    }

}


class Oval extends Shape {
    private Point startPoint, currentPoint;
    private Color shapeColor;
    private int x, y, width, height;
    private boolean filledState;

    void drawWhileDragging(Graphics g) {
        x = startPoint.x;
        y = startPoint.y;
        width = currentPoint.x - startPoint.x;
        height = currentPoint.y - startPoint.y;
        // make sure to be able to draw in all directions
        if (width < 0) {
            width = -width;
            x = x - width + 1;
            if (x < 0) {
                width += x;
                x = 0;
            }
        }
        if (height < 0) {
            height = -height;
            y = y - height + 1;
            if (y < 0) {
                height += y;
                y = 0;
            }
        }
        g.setColor(shapeColor);
        if (filledState)
            g.fillOval(x, y, width, height);
        else
            g.drawOval(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(shapeColor);
        if (filledState)
            g.fillOval(x, y, width, height);
        else
            g.drawOval(x, y, width, height);
    }

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

    void setDragPoint(int x, int y) {
        this.currentPoint.x = x;
        this.currentPoint.y = y;
    }

}
