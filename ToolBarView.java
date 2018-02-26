import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ToolBarView extends JPanel implements Iview{
    private JButton selectButton;
    private JButton drawButton;
    private JComboBox shapeBox;
    private JComboBox widthBox;
    private JButton strokeColor;
    private JButton fillColor;

    private Model model;


    public ToolBarView(Model model){
        this.model=model;
        model.addObserver(this);

        selectButton=new JButton("Select");
        ActionListener selectButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                model.setMode(false);
                System.out.println("Set to select mode");
            }
        };
        selectButton.addActionListener(selectButtonListener);

        drawButton=new JButton("Draw");
        ActionListener drawButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                model.setMode(true);
                System.out.println("Set to draw mode");
            }
        };
        drawButton.addActionListener(drawButtonListener);

        shapeBox=new JComboBox();
        shapeBox.addItem("Freeform line");
        shapeBox.addItem("Straight line");
        shapeBox.addItem("Rectangle");
        shapeBox.addItem("Ellipse");
        shapeBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Combobox event: "+e.getItem());
                    String temp=e.getItem().toString();
                    if(!model.isMode()) {
                        if(model.getSelectedShape()!=-1){
                            Shape s=model.getShape(model.getSelectedShape());
                            if (temp == "Freeform line") {
                                s.type=1;
                            } else if (temp == "Straight line") {
                                s.type=2;
                            } else if (temp == "Rectangle") {
                                s.type=3;
                            } else if (temp == "Ellipse") {
                                s.type=4;
                            }
                        }
                    }else {
                        if (temp == "Freeform line") {
                            model.setShapeOption(1);
                        } else if (temp == "Straight line") {
                            model.setShapeOption(2);
                        } else if (temp == "Rectangle") {
                            model.setShapeOption(3);
                        } else if (temp == "Ellipse") {
                            model.setShapeOption(4);
                        }
                    }
                }
            }
        });

        widthBox=new JComboBox();
        for(int i=1; i<11; ++i){
            String temp=Integer.toString(i)+"px";
            //System.out.println(temp);
            widthBox.addItem(temp);
        }
        widthBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    String temp=e.getItem().toString();
                    if(temp.length()==4){ //10px
                        if(!model.isMode()) {
                            if (model.getSelectedShape() != -1) {
                                Shape s = model.getShape(model.getSelectedShape());
                                s.thickness=10;
                                model.setStrokeWidth(10);
                                model.notifyObservers();
                            }
                        } else {
                            model.setStrokeWidth(10);
                            System.out.println("Combobox event: " + 10);
                        }

                    }else{
                        if(!model.isMode()) {
                            if (model.getSelectedShape() != -1) {
                                Shape s = model.getShape(model.getSelectedShape());
                                s.thickness=Integer.parseInt((temp.substring(0, 1)));
                                model.setStrokeWidth(Integer.parseInt((temp.substring(0, 1))));
                                model.notifyObservers();
                            }
                        } else {
                            model.setStrokeWidth(Integer.parseInt((temp.substring(0, 1))));
                            System.out.println("Combobox event: " + model.getStrokeWidth());
                        }
                    }
                }
            }
        });

        strokeColor=new JButton("Stroke Colour");
        ActionListener strokeColorListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color temp = JColorChooser.showDialog(null, "Choose Stroke Colour", strokeColor.getBackground());
                if (temp != null) {
                    if(!model.isMode()) {
                        if (model.getSelectedShape() != -1) {
                            Shape s = model.getShape(model.getSelectedShape());
                            s.strokeColour=temp;
                            model.notifyObservers();
                        }
                    } else {
                        model.setStrokeColor(temp);
                        System.out.println("Stroke colour selected!");
                    }
                }
            }
        };
        strokeColor.addActionListener(strokeColorListener);

        fillColor=new JButton("Fill Colour");
        ActionListener fillColorListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color temp = JColorChooser.showDialog(null, "Choose Fill Colour", fillColor.getBackground());
                if (temp != null) {
                    if(!model.isMode()) {
                        if (model.getSelectedShape() != -1) {
                            Shape s = model.getShape(model.getSelectedShape());
                            s.fillColor=temp;
                            model.notifyObservers();
                        }
                    } else {
                        model.setFillColor(temp);
                        System.out.println("Fill colour selected!");
                    }
                }
            }
        };
        fillColor.addActionListener(fillColorListener);

        this.add(selectButton);
        this.add(drawButton);
        this.add(shapeBox);
        this.add(widthBox);
        this.add(strokeColor);
        this.add(fillColor);
    }
    public void update(){

        System.out.println("Update ToolBarView");

        fillColor.setBackground(model.getFillColor());
        strokeColor.setBackground(model.getStrokeColor());

        if(model.isMode()){
            drawButton.setSelected(true);
            selectButton.setSelected(false);
        }else{
            drawButton.setSelected(false);
            selectButton.setSelected(true);
        }

        String item=Integer.toString((int)model.strokeWidth)+"px";
        System.out.println("Selected item: "+item);
        widthBox.setSelectedItem(item);

    }
}
