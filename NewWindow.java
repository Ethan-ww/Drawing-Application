import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewWindow extends JFrame implements PropertyChangeListener{
    private JLabel transLabel;
    private JLabel rotateLabel;
    private JLabel scaleLabel;

    private double t_x=0, t_y=0;
    private double deg=0;
    private double s_x=1, s_y=1;


    private JFormattedTextField t_x_f, t_y_f;
    private JFormattedTextField deg_f;
    private JFormattedTextField s_x_f, s_y_f;

    private JButton ok;
    private JButton cancel;

    private Model model;

    NewWindow(Model m) {
        this.model=m;
        this.setTitle("Transform shape");
        this.setVisible(true);
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(500, 200);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLayout(new GridLayout(4 ,1));

        t_x=model.getShape(model.getSelectedShape()).t_x;
        t_y=model.getShape(model.getSelectedShape()).t_y;
        deg=model.getShape(model.getSelectedShape()).rotate_degree;
        s_x=model.getShape(model.getSelectedShape()).s_x;
        s_y=model.getShape(model.getSelectedShape()).s_y;

        transLabel=new JLabel("Translate(px):");
        rotateLabel=new JLabel("Rotate(degrees):");
        scaleLabel=new JLabel("Scale(times):");

        t_x_f=new JFormattedTextField();
        t_x_f.setValue(new Double(t_x));
        t_x_f.setColumns(10);
        t_x_f.addPropertyChangeListener("value", this);

        t_y_f=new JFormattedTextField();
        t_y_f.setValue(new Double(t_y));
        t_y_f.setColumns(20);
        t_y_f.addPropertyChangeListener("value", this);

        deg_f=new JFormattedTextField();
        deg_f.setValue(new Double(deg));
        deg_f.setColumns(10);
        deg_f.addPropertyChangeListener("value", this);

        s_x_f=new JFormattedTextField();
        s_x_f.setValue(new Double(s_x));
        s_x_f.setColumns(10);
        s_x_f.addPropertyChangeListener("value", this);

        s_y_f=new JFormattedTextField();
        s_y_f.setValue(new Double(s_y));
        s_y_f.setColumns(20);
        s_y_f.addPropertyChangeListener("value", this);

        transLabel.setLabelFor(t_x_f);
        rotateLabel.setLabelFor(deg_f);
        scaleLabel.setLabelFor(s_x_f);

        JPanel layer1 = new JPanel(new GridLayout(0,3));
        JPanel layer2 = new JPanel(new GridLayout(0,2));
        JPanel layer3 = new JPanel(new GridLayout(0,3));
        JPanel layer4 = new JPanel(new GridLayout(0,2));

        layer1.setPreferredSize(new Dimension(this.getWidth(),50));
        layer2.setPreferredSize(new Dimension(this.getWidth(),50));
        layer3.setPreferredSize(new Dimension(this.getWidth(),50));
        layer4.setPreferredSize(new Dimension(this.getWidth(),50));


        layer1.add(transLabel);
        layer1.add(t_x_f);
        layer1.add(t_y_f);

        layer2.add(rotateLabel);
        layer2.add(deg_f);

        layer3.add(scaleLabel);
        layer3.add(s_x_f);
        layer3.add(s_y_f);

        ok=new JButton("OK");
        cancel=new JButton("Cancel");

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Transformation applied");

                model.setDegree(deg);
                model.setScale(s_x,s_y);
                model.setTrans(t_x,t_y);

                model.notifyObservers();

                setVisible(false);
                dispose();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });

        layer4.add(ok);
        layer4.add(cancel);


        this.getContentPane().add(layer1);
        this.getContentPane().add(layer2);
        this.getContentPane().add(layer3);
        this.getContentPane().add(layer4);



    }

    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        if (source == t_x_f) {
            t_x = ((Number)t_x_f.getValue()).doubleValue();
        } else if (source == t_y_f) {
            t_y = ((Number)t_y_f.getValue()).doubleValue();
        } else if (source == deg_f) {
            deg = ((Number)deg_f.getValue()).doubleValue();
        }else if (source == s_x_f) {
            s_x = ((Number)s_x_f.getValue()).doubleValue();
        }else if (source == s_y_f) {
            s_y = ((Number)s_y_f.getValue()).doubleValue();
        }

    }
}
