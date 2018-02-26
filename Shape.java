import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Shape {

    private Model model;
    /* everything about bound box */

    double left_x;
    double right_x;
    double top_y;
    double bottom_y;
    Point2d center;

    public void get_x_min() {
        double min = 100000;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() < min) {
                min = points.get(i).getX();
            }
        }
        left_x = min;
    }

    public void get_x_max() {
        double max = -100000;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() > max) {
                max = points.get(i).getX();
            }
        }
        right_x = max;
    }

    public void get_y_min() {
        double min = 100000;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getY() < min) {
                min = points.get(i).getY();
            }
        }
        top_y = min;
    }

    public void get_y_max() {
        double max = -100000;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getY() > max) {
                max = points.get(i).getY();
            }
        }
        bottom_y = max;
    }


    public void set_bound(){
        get_x_max();
        get_x_min();
        get_y_max();
        get_y_min();

        center = new Point2d((left_x+right_x)/2,(top_y+bottom_y)/2);
    }


    /* all info about transformation */
    float thickness;
    Color strokeColour;
    Color fillColor;

    double t_x=0, t_y=0;
    double rotate_degree=0;
    double s_x=1,s_y=1;

    AffineTransform atm;

    int type;

    ArrayList<Point2d> points; //polyline
    Point2d begining;
    Point2d end;


    public void addPoint(Point2d p){
        points.add(p);
        this.end=p;
    }

    Shape(Model m){
        this.model=m;
        this.points=new ArrayList<Point2d>();
        this.type=m.getShapeOption();
        this.thickness=m.getStrokeWidth();
        this.strokeColour=m.getStrokeColor();
        this.fillColor=m.getFillColor();
        this.rotate_degree=0;
        this.s_x=1;
        this.s_y=1;
    }

    int[] x_points;
    int[] y_points;
    boolean updated;
    void cachePointsArray() {
        x_points = new int[points.size()];
        y_points = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x_points[i] = (int) points.get(i).getX();
            y_points[i] = (int) points.get(i).getY();
        }
        updated = false;
    }

    public void paint(Graphics2D g2) {
        // don't draw if points are empty (not shape)
        if (points == null)
            return;
        set_bound();

        cachePointsArray();
        // save the current g2 transform matrix
        AffineTransform M = g2.getTransform();
        // multiply in this shape's transform
        // (uniform scale)
        g2.translate(center.getX(), center.getY());
        g2.scale(s_x, s_y);
        g2.rotate(Math.toRadians(rotate_degree));
        g2.translate((-1) * center.getX(), (-1) * center.getY());
        g2.translate(t_x,t_y);
        // save the modified g2 transform matrix
        atm = g2.getTransform();

        int b_x=(int)this.begining.x;
        int b_y=(int)this.begining.y;
        int e_x=(int)this.end.x;
        int e_y=(int)this.end.y;
        int width=Math.abs((int)this.begining.x-(int)this.end.x);
        int height=Math.abs((int)this.begining.y-(int)this.end.y);

        switch (this.type){
            case 1:
                g2.setColor(strokeColour);
                g2.setStroke(new BasicStroke(thickness ));
                g2.drawPolyline(x_points, y_points, points.size());
                break;
            case 2:
                g2.setColor(strokeColour);
                g2.setStroke(new BasicStroke(thickness ));
                g2.drawLine((int)this.begining.x,(int)this.begining.y,(int)this.end.x,(int)this.end.y);
                break;
            case 3:
                if(fillColor!=Color.white) {
                    g2.setColor(fillColor);
                    g2.fillRect(Math.min(b_x,e_x), Math.min(b_y,e_y), width, height);
                }

                g2.setColor(strokeColour);
                g2.setStroke(new BasicStroke(thickness ));
                g2.drawRect(Math.min(b_x,e_x), Math.min(b_y,e_y),width,height);

                break;
            case 4:
                if(fillColor!=Color.white) {
                    g2.setColor(fillColor);
                    g2.fillOval(Math.min(b_x,e_x), Math.min(b_y,e_y), width, height);
                }

                g2.setColor(strokeColour);
                g2.setStroke(new BasicStroke(thickness ));
                g2.drawOval(Math.min(b_x,e_x), Math.min(b_y,e_y),width,height);
                break;

            default:
                System.err.println("Invalid shape type!");
                break;
        }
        // call drawing functions

        // if select, highlight this shape
        if (model.getSelectedShape()!=-1) {
            if(model.getShape(model.getSelectedShape())==this) {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(Color.blue);
                if(this.type==1) {
                    g2.drawRect((int) left_x - 2, (int) top_y - 2, (int) (right_x - left_x) + 4, (int) (bottom_y - top_y) + 4);
                }else{
                    g2.drawRect(Math.min(b_x,e_x)-2, Math.min(b_y,e_y)-2,width+4,height+4);
                }
            }

        }

        g2.setTransform(M);
    }

    public void updateDrawInfo(){
        this.thickness=model.getStrokeWidth();
        this.strokeColour=model.getStrokeColor();
        this.fillColor=model.getFillColor();
    }

    static Point2d closestPoint(Point2d m, Point2d P0, Point2d P1) {
        Vector2d v = new Vector2d();
        v.sub(P1, P0); // v = P2 - P1

        if (v.lengthSquared() < 0.5)
            return P0;

        Vector2d u = new Vector2d();
        u.sub(m, P0); // u = M - P1
        double s = u.dot(v) / v.dot(v);

        if (s < 0)
            return P0;
        else if (s > 1)
            return P1;
        else {
            Point2d I = P0;
            Vector2d w = new Vector2d();
            w.scale(s, v); // w = s * v
            I.add(w); // I = P1 + w
            return I;
        }
    }



    public boolean hitTest(double x, double y) {
        Point2d mouse = new Point2d(x, y);
        Point2d temp;
        try {
            Point2D dst = new Point2D.Double(x,y);
            Point2D src = new Point2D.Double(x,y);
            atm.inverseTransform(src, dst);
            mouse = new Point2d(dst.getX(), dst.getY());
        } catch (NoninvertibleTransformException ex) {
            mouse = new Point2d(x, y);
        }

        int b_x=(int)this.begining.x;
        int b_y=(int)this.begining.y;
        int e_x=(int)this.end.x;
        int e_y=(int)this.end.y;
        int width=Math.abs((int)this.begining.x-(int)this.end.x);
        int height=Math.abs((int)this.begining.y-(int)this.end.y);

        switch (this.type) {
            case 1:
                if (points != null) {
                    for (int i = 0; i < points.size() - 1; ++i) {
                        temp = closestPoint(mouse, points.get(i), points.get(i + 1));
                        if (mouse.distance(temp) <= 5) {
                            return true;
                        }
                    }
                }
                break;

            case 2:
                temp = closestPoint(mouse, this.begining, this.end);
                if (mouse.distance(temp) <= 5) {
                    return true;
                }
                break;
            case 3:
                if((mouse.x<=e_x+5)&&(mouse.x>=b_x-5)
                        &&(mouse.y<=e_y+5)&&(mouse.y>=b_y-5))
                return true;
                break;
            case 4:
                // not fully implemented, need time to prepare for cs350...
                if((mouse.x<=e_x+5)&&(mouse.x>=b_x-5)
                        &&(mouse.y<=e_y+5)&&(mouse.y>=b_y-5))
                    return true;
                break;
            default:
                break;
        }
        return false;
    }

    public void changeModel(){
        model.strokeWidth=thickness;
        model.shapeOption=type;
        model.fillColor=fillColor;
        model.strokeColor=strokeColour;
    }

}