import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        MainView mainView = new MainView(model);
        ToolBarView toolBarView=new ToolBarView(model);
        CanvasView canvasView=new CanvasView(model);


        // create toolbar panel
        JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBarPanel.setPreferredSize(new Dimension(mainView.getWidth(), 50));
        // canvas panel for drawing
        JPanel canvasPanel = new JPanel(new BorderLayout());
        canvasPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        canvasPanel.setPreferredSize(new Dimension(mainView.getWidth(), 500));
        canvasPanel.add(canvasView);

        mainView.getContentPane().add(toolBarPanel, BorderLayout.NORTH);
        mainView.getContentPane().add(canvasPanel, BorderLayout.CENTER);

        toolBarPanel.add(toolBarView);
        canvasPanel.add(canvasView);

        model.setMode(true);

        mainView.setResizable(true);
        mainView.setPreferredSize(new Dimension(800, 600));
        mainView.pack();
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainView.setVisible(true);

    }
}
