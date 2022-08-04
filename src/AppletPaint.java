import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class AppletPaint extends Applet {
    Font f1;
    // width and height variables
    int width, height;
    // selected color from palette
    Color selectedColor;
    // buttons and buttons states
    Button blackColorBtn, blueColorBtn, redColorBtn, greenColorBtn, freeHandBtn;
    boolean freeHandSt = false;

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

    }

    public void paint(Graphics g) {
        //g.setFont(f1);
        // draw the top section
        g.drawLine(0, 200, width, 200);
        g.drawRect(width - 250, 50, 200, 100);
        g.setColor(selectedColor);

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
                System.out.println("op color selected");
                freeHandSt = true;

            }
        }
    }

    // event listener to mouse
    class MouseMove implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            // when the free hand button is clicked go to free hand mode
            if (freeHandSt) {
                Graphics g = getGraphics();
                g.setColor(selectedColor);
                g.fillOval(e.getX(), e.getY(), 40, 40);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

}
