
import javax.vecmath.Point2d;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Model {
    /** The iviews that are watching this model for changes. */
    private List<Iview> iviews;
    private List<Shape> shapes;

    // drawing information
    private boolean mode;
    int shapeOption;
    float strokeWidth;
    Color strokeColor;
    Color fillColor;


    // selection information
    private int selectedShape;

    public int getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(int selectedShape) {
        this.selectedShape = selectedShape;
        notifyObservers();
    }
    // setters and getters

    public void setMode(boolean mode) {
        this.mode = mode;
        notifyObservers();
    }

    public void setShapeOption(int shapeOption) {
        this.shapeOption = shapeOption;
        notifyObservers();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        notifyObservers();
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        notifyObservers();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        notifyObservers();
    }


    public boolean isMode() {
        return mode;
    }

    public int getShapeOption() {
        return shapeOption;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public int getShapeSize(){
        return shapes.size();
    }

    public Shape getShape(int i){
        return shapes.get(i);
    }

    public void setTrans(double t_x, double t_y){
        if(selectedShape!=-1){
            this.getShape(this.getSelectedShape()).t_x=t_x;
            this.getShape(this.getSelectedShape()).t_y=t_y;
        }
        //notifyObservers();
    }

    public void setDegree(double deg){
        if(selectedShape!=-1){
            this.getShape(this.getSelectedShape()).rotate_degree=deg;
        }
        //notifyObservers();
    }

    public void setScale(double s_x, double s_y){
        if(selectedShape!=-1){
            this.getShape(this.getSelectedShape()).s_x=s_x;
            this.getShape(this.getSelectedShape()).s_y=s_y;
        }
        //notifyObservers();
    }
    /**
     * Create a new model.
     */
    public Model() {
        this.iviews = new ArrayList<Iview>();
        this.shapes = new ArrayList<Shape>();
        // true for drawing mode, false for selection mode
        boolean mode=true;
        // 1: freeform
        // 2: straight line
        // 3: rectangle
        // 4: ellipse
        this.shapeOption=1;
        this.strokeWidth=2.0f;
        this.strokeColor = Color.black;
        this.fillColor = Color.white;
        this.selectedShape=-1;
        notifyObservers();
    }

    /**
     * Add an iview to be notified when this model changes.
     */
    public void addObserver(Iview iview) {
        this.iviews.add(iview);
    }

    /**
     * Remove an iview from this model.
     */
    public void removeObserver(Iview iview) {
        this.iviews.remove(iview);
    }

    /**
     * Notify all iviews that the model has changed.
     */
    private boolean updatedShape;
    public void notifyObservers() {
        for (Iview iview : this.iviews) {
            iview.update();
        }
        if(getSelectedShape()!=-1){
            //shapes.get(selectedShape).updateDrawInfo();
            shapes.get(selectedShape).changeModel();
        }
    }

    public void addShape(Shape shape){
        shapes.add(shape);
        notifyObservers();
    }

    public void resetShapes(){
        shapes=new ArrayList<Shape>();
        notifyObservers();
    }



    public void removeShape(){
        int i=this.getSelectedShape();
        if(i>=0&&i<shapes.size()){
            shapes.remove(i);
        }else{
            System.out.println("removeShape: Invalid index");
        }
    }



}
