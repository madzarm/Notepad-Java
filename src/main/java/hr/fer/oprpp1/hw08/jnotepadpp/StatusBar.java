package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusBar extends JPanel {
    private static StatusBar instance = null;
    private JLabel lengthLabel;
    private JLabel lineColSelLabel;
    private JTextArea currentTextArea;
    private JLabel clockLabel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private StatusBar() {
        super(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(1, 2)); // Holds the length and lineColSel labels
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Holds the clock

        // Initialize the labels
        lengthLabel = new JLabel("Length: 0");
        lineColSelLabel = new JLabel("Ln: 0 Col: 0 Sel: 0");
        clockLabel = new JLabel();
        updateClock(); // Initialize the clock label with the current time

        // Add labels to their respective panels
        leftPanel.add(lengthLabel);
        leftPanel.add(lineColSelLabel);
        rightPanel.add(clockLabel);

        // Add panels to the StatusBar
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Set up a timer to update the clockLabel every second
        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
    }

    private void updateClock() {
        String timeString = dateFormat.format(new Date());
        clockLabel.setText(timeString);
    }

    public static StatusBar getInstance() {
        if (instance == null) {
            instance = new StatusBar();
        }
        return instance;
    }

    public void updateStatusBar(JTextArea area) {
        if (area == null) {
            lengthLabel.setText("Length: 0");
            lineColSelLabel.setText("Ln: 0 Col: 0");
            return;
        }
        int length = area.getText().length();
        int lines = 1, columns = 1, sel = 0;
        try {
            int caretPos = area.getCaretPosition();
            lines = area.getLineOfOffset(caretPos) + 1;
            columns = caretPos - area.getLineStartOffset(lines - 1) + 1;
            sel = Math.max(
                    Math.abs(area.getCaretPosition() - area.getSelectionStart()),
                    Math.abs(area.getCaretPosition() - area.getSelectionEnd())
            );
        } catch (Exception ignored) {
        }
        lengthLabel.setText("Length: " + length);
        lineColSelLabel.setText("Ln: " + lines + " Col: " + columns + (sel != 0 ? " Sel: " + sel : ""));
    }

    public void attachToTextArea(JTextArea area) {
        detachCurrentTextArea();
        currentTextArea = area;
        if (area != null) {
            area.addCaretListener(e -> updateStatusBar(area));
        }
        updateStatusBar(area);
    }

    private void detachCurrentTextArea() {
        if (currentTextArea != null) {
            currentTextArea.removeCaretListener(e -> updateStatusBar(currentTextArea));
        }
    }
}
