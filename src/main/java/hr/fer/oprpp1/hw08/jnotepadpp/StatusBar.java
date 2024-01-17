package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private static StatusBar instance = null;
    private JLabel statusLabel;
    private JTextArea currentTextArea;

    private StatusBar() {
        super(new BorderLayout());
        statusLabel = new JLabel("length: " + 0 + " lines: " + 0 + " column: " + 0);
        this.add(statusLabel, BorderLayout.WEST);
    }

    public static StatusBar getInstance() {
        if (instance == null) {
            instance = new StatusBar();
        }
        return instance;
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public void updateStatusBar(JTextArea area) {
        if (area == null) {
            updateStatus("length: " + 0 + " lines: " + 0 + " column: " + 0);
            return;
        }
        int length = area.getText().length();
        int lines = area.getLineCount();
        int column = 0;
        try {
            column = area.getCaretPosition() - area.getLineStartOffset(area.getLineOfOffset(area.getCaretPosition()));
        } catch (Exception ignored) {
        }
        updateStatus("length: " + length + " lines: " + lines + " column: " + column);
    }

    public void attachToTextArea(JTextArea area) {
        detachCurrentTextArea();
        currentTextArea = area;
        if (area != null) {
            area.addCaretListener(e -> updateStatusBar(area));
            updateStatusBar(area);
        }
    }

    private void detachCurrentTextArea() {
        if (currentTextArea != null) {
            currentTextArea.removeCaretListener(e -> updateStatusBar(currentTextArea));
        }
    }
}
