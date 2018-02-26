
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class MainView extends JFrame implements Iview {

    private Model model;
    private JMenuBar menuBar;


    private JMenu getMenu(String menuName) {
        /*
         * return the menu with menuName
         */
        JMenu menu = null;
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            menu = menuBar.getMenu(i);
            if (menu.getText().equals(menuName))
                return menu;
        }
        return null;
    }

    private JMenu createMenu(String name, String mnemonic) {
        /*
         * create menu
         */
        JMenu menu = new JMenu(name);
        if (mnemonic != null)
            menu.setMnemonic(mnemonic.toCharArray()[0]);
        return menu;
    }

    private JMenuItem createMenuItem(String name, String mnemonic, Icon icon, KeyStroke keyStroke) {
        /*
         * create item
         */
        JMenuItem menuItem = new JMenuItem(name, icon);
        if (mnemonic != null)
            menuItem.setMnemonic(mnemonic.toCharArray()[0]);
        if (keyStroke != null)
            menuItem.setAccelerator(keyStroke);
        return menuItem;
    }
    private JMenu swm;

    /**
     * Create a new View.
     */
    public MainView(Model model) {
        // Set up the window.
        this.setTitle("CS 349 W18 A2");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menuBar=new JMenuBar();
        setJMenuBar(this.menuBar);

        menuBar.add(createMenu("File", "f"));
        menuBar.add(createMenu("Edit", "e"));
        menuBar.add(createMenu("Format", "f"));

        JMenu fileMenu=getMenu("File");
        JMenuItem fm1=createMenuItem("New", "n", null, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        JMenuItem fm2=createMenuItem("Exit", "e", null, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        fileMenu.add(fm1);
        fileMenu.add(fm2);

        fm1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                model.resetShapes();
            }
        });

        fm2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        JMenu editMenu=getMenu("Edit");
        JCheckBoxMenuItem em1=new JCheckBoxMenuItem("Selection Mode");
        JCheckBoxMenuItem em2=new JCheckBoxMenuItem("Drawing Mode");
        em1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        em2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));

        editMenu.add(em1);
        editMenu.add(em2);

        em1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setMode(false);
            }
        });

        em2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setMode(true);
            }
        });

        editMenu.addSeparator();

        JMenuItem em3=createMenuItem("Delete shape", "d", null, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK));
        JMenuItem em4=createMenuItem("Transform shape", "t", null, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        JMenuItem em5=createMenuItem("X-Reflect", "x", null, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        JMenuItem em6=createMenuItem("Y-Reflect", "y", null, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));

        em3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                model.removeShape();
                repaint();
            }
        });

        //MainView mv=this;
        em4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //mv.disable();
                NewWindow newWindow=new NewWindow(model);
                //mv.enable();
            }
        });

        em5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.getSelectedShape()!=-1) {
                    Shape temp_shape = model.getShape(model.getSelectedShape());
                    model.setScale(temp_shape.s_x,-1*temp_shape.s_y);
                    model.notifyObservers();
                }
            }
        });

        em6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.getSelectedShape()!=-1) {
                    Shape temp_shape = model.getShape(model.getSelectedShape());
                    model.setScale(-1*temp_shape.s_x,temp_shape.s_y);
                    model.notifyObservers();
                }
            }
        });


        editMenu.add(em3);
        editMenu.add(em4);
        editMenu.add(em5);
        editMenu.add(em6);

        JMenu formatMenu=getMenu("Format");
        JMenu fom1=new JMenu("Stroke width");
        swm=fom1;
        JMenuItem fom2=createMenuItem("Fill Colour", "F", null, null);
        JMenuItem fom3=createMenuItem("Stroke Colour", "C", null, null);
        formatMenu.add(fom1);
        formatMenu.add(fom2);
        formatMenu.add(fom3);

        for(int i=1;i<11;++i){
            float wi=i;
            JCheckBoxMenuItem temp=new JCheckBoxMenuItem(Integer.toString(i)+"px");
            temp.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!model.isMode()) {
                        if (model.getSelectedShape() != -1) {
                            Shape s = model.getShape(model.getSelectedShape());
                            s.thickness=wi;
                            model.notifyObservers();
                        }
                    } else {
                        model.setStrokeWidth(wi);
                        System.out.println("Format menu event: " + wi);
                    }

                }
            });
            fom1.add(temp);
        }

        fom2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Color temp = JColorChooser.showDialog(null, "Choose Fill Colour", fom2.getBackground());
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
        });

        fom3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Color temp = JColorChooser.showDialog(null, "Choose Stroke Colour", fom3.getBackground());
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
        });

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update() {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        System.out.println("update MainView");
        JMenu editMenu=getMenu("Edit");
        //System.out.println(editMenu.getText());
        if(model.isMode()){
            //System.out.println("Item:" + editMenu.getItem(3).getText());
            editMenu.getItem(0).setSelected(false);
            editMenu.getItem(1).setSelected(true);
            editMenu.getItem(3).setEnabled(false);
            editMenu.getItem(4).setEnabled(false);
            editMenu.getItem(5).setEnabled(false);
            editMenu.getItem(6).setEnabled(false);
        }else{
            editMenu.getItem(0).setSelected(true);
            editMenu.getItem(1).setSelected(false);
            editMenu.getItem(3).setEnabled(true);
            editMenu.getItem(4).setEnabled(true);
            editMenu.getItem(5).setEnabled(true);
            editMenu.getItem(6).setEnabled(true);
        }


        for(int i=0;i<10;++i){
            JMenuItem temp=swm.getItem(i);
            if(temp.getText()==Integer.toString((int)model.strokeWidth)+"px"){
                swm.getItem(i).setSelected(true);
            }else{
                swm.getItem(i).setSelected(false);
            }
        }
    }
}
