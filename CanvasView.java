import javax.swing.*;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class CanvasView extends JPanel implements Iview {
    private Model model;
    private Point2d mouse;

    private boolean moved;
    CanvasView (Model model){
        this.model=model;
        model.addObserver(this);
        this.setBackground(Color.white);

        // setup the event to go to the controller
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //System.out.println(model.getShapeOption());
                if(model.isMode()) {
                    Shape temp = new Shape(model);
                    temp.begining = new Point2d(e.getX(), e.getY());
                    model.addShape(temp);
                    System.out.println("Add new shape type: " + Integer.toString(temp.type));
                    System.out.println("Starting point : X:" +
                            Double.toString(temp.begining.x) + " Y:" +
                            Double.toString(temp.begining.y));

                    moved = false;
                    model.setSelectedShape(-1);
                    repaint();
                }else{

                }
            }

            public void mouseReleased(MouseEvent e) {
                if(model.isMode()) {
                    if (moved) {
                        Shape temp = model.getShape(model.getShapeSize() - 1);
                        temp.end = new Point2d(e.getX(), e.getY());
                        System.out.println("Ending point : X:" +
                                Double.toString(temp.end.x) + " Y:" +
                                Double.toString(temp.end.y));
                    }
                }else{
                    mouse = new Point2d(e.getX(), e.getY());
                    for (int i = 0; i < model.getShapeSize(); ++i) {
                        if (model.getShape(i) != null) {
                            if (model.getShape(i).hitTest(mouse.x, mouse.y)) {
                                System.out.println("Shape hit");
                                model.setSelectedShape(i);
                            }
                        }
                    }
                }
            }

            public void mouseClicked(MouseEvent e) {
                // hit test
                boolean hitAnything=false;
                if (!model.isMode()) {
                    mouse = new Point2d(e.getX(), e.getY());
                    for (int i = 0; i < model.getShapeSize(); ++i) {
                        if (model.getShape(i) != null) {
                            if (model.getShape(i).hitTest(mouse.x, mouse.y)) {
                                System.out.println("Shape hit");
                                model.setSelectedShape(i);
                                hitAnything=true;
                            }
                        }
                    }
                    if(!hitAnything){model.setSelectedShape(-1);}
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (model.getSelectedShape() == -1) {
                    model.getShape(model.getShapeSize() - 1).addPoint(new Point2d(e.getX(), e.getY()));
                    moved = true;
                } else {
                    mouse = new Point2d(e.getX(), e.getY());
                    //moved=true;
                }
                repaint();
            }
        });


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < model.getShapeSize(); ++i) {
            if (model.getShape(i) != null) {
                model.getShape(i).paint(g2);
            }
        }
    }

    @Override
    public void update() {
        System.out.println("update CanvasView");
        repaint();
    }
}
