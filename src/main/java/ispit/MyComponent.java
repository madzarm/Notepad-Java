package ispit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyComponent extends JComponent {
    private final List<Integer> numbers;
    private final int sum;

    public MyComponent(List<Integer> numbers) {
        this.numbers = numbers;
        this.sum = numbers.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sum == 0) return; // If the sum is zero, do not draw bars.

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / numbers.size();

        for (int i = 0; i < numbers.size(); i++) {
            int barHeight = (int) ((numbers.get(i) / (double) sum) * height);
            int x = i * barWidth;
            int y = height - barHeight;

            // Alternate colors between green and red
            g.setColor(i % 2 == 0 ? Color.GREEN : Color.RED);
            g.fillRect(x, y, barWidth, barHeight);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(numbers.size() * 100, 300); // Preferred size of the component
    }
}
