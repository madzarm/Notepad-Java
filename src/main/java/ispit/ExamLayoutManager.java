package ispit;

import java.awt.*;
import javax.swing.*;

public class ExamLayoutManager implements LayoutManager {
    public static final String AREA1 = "Area1";
    public static final String AREA2 = "Area2";
    public static final String AREA3 = "Area3";

    private int percentage;
    private Component compArea1, compArea2, compArea3;

    public ExamLayoutManager(int percentage) {
        setPercentage(percentage);
    }

    public void setPercentage(int percentage) {
        if (percentage < 10 || percentage > 90) {
            throw new IllegalArgumentException("Percentage must be between 10 and 90");
        }
        this.percentage = percentage;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        switch (name) {
            case AREA1:
                compArea1 = comp;
                break;
            case AREA2:
                compArea2 = comp;
                break;
            case AREA3:
                compArea3 = comp;
                break;
            default:
                throw new IllegalArgumentException("Invalid area name: " + name);
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        if (comp == compArea1) compArea1 = null;
        if (comp == compArea2) compArea2 = null;
        if (comp == compArea3) compArea3 = null;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(500, 500);
    }

    @Override
    public void layoutContainer(Container parent) {
        int width = parent.getWidth();
        int height = parent.getHeight();

        int area1Height = height * percentage / 100;
        int area2Width = width * percentage / 100;

        Component[] components = parent.getComponents();
        if (components.length >= 1) {
            components[0].setBounds(0, 0, width, area1Height);
        }
        if (components.length >= 2) {
            components[1].setBounds(0, area1Height, area2Width, height - area1Height);
        }
        if (components.length >= 3) {
            components[2].setBounds(area2Width, area1Height, width - area2Width, height - area1Height);
        }
    }
}
