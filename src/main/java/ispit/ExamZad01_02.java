package ispit;

import javax.swing.*;
import java.awt.*;

public class ExamZad01_02 extends JDialog {
    private ExamLayoutManager exlm;

    public ExamZad01_02() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        initGUI();
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Initialize the layout manager with a default percentage
        exlm = new ExamLayoutManager(20);

        // Panel to hold the slider
        JPanel topPanel = new JPanel();
        JSlider slider = new JSlider(10, 90, 20); // Slider from 10 to 90, starting at 20
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        topPanel.add(slider);
        cp.add(topPanel, BorderLayout.NORTH);

        JPanel layoutPanel = new JPanel(exlm);
        layoutPanel.add(makeLabel("Ovo je tekst za područje 1.", Color.RED), ExamLayoutManager.AREA1);
        layoutPanel.add(makeLabel("Područje 2.", Color.GREEN), ExamLayoutManager.AREA2);
        layoutPanel.add(makeLabel("Područje 3.", Color.YELLOW), ExamLayoutManager.AREA3);
        cp.add(layoutPanel, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            int percentage = slider.getValue();
            exlm.setPercentage(percentage);
            layoutPanel.revalidate();
        });
    }

    private Component makeLabel(String txt, Color col) {
        JLabel lab = new JLabel(txt);
        lab.setOpaque(true);
        lab.setBackground(col);
        return lab;
    }
}
