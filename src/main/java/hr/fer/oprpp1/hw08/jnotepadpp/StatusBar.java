package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing a status bar for the JNotepadPP application.
 */
public class StatusBar extends JPanel {

    /**
     * The singleton instance of the StatusBar.
     */
    private static StatusBar instance = null;

    /**
     * Label displaying the length of the text.
     */
    private JLabel lengthLabel;

    /**
     * Label displaying the line number, column number, and selection length.
     */
    private JLabel lineColSelLabel;

    /**
     * The current JTextArea to which this status bar is attached.
     */
    private JTextArea currentTextArea;

    /**
     * Label displaying the current time.
     */
    private JLabel clockLabel;

    /**
     * The format for displaying the date and time.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Private constructor for the StatusBar class.
     */
    private StatusBar() {
        super(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(1, 2));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        lengthLabel = new JLabel("Length: 0");
        lineColSelLabel = new JLabel("Ln: 0 Col: 0 Sel: 0");
        clockLabel = new JLabel();
        updateClock();

        leftPanel.add(lengthLabel);
        leftPanel.add(lineColSelLabel);
        rightPanel.add(clockLabel);

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
    }

    /**
     * Updates the clock display on the status bar.
     */
    private void updateClock() {
        String timeString = dateFormat.format(new Date());
        clockLabel.setText(timeString);
    }

    /**
     * Gets the singleton instance of the StatusBar.
     * @return The StatusBar instance.
     */
    public static StatusBar getInstance() {
        if (instance == null) {
            instance = new StatusBar();
        }
        return instance;
    }

    /**
     * Updates the status bar based on the given JTextArea.
     * @param area The JTextArea to use for status updates.
     */
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

    /**
     * Attaches the status bar to a given JTextArea.
     * @param area The JTextArea to attach the status bar to.
     */
    public void attachToTextArea(JTextArea area) {
        detachCurrentTextArea();
        currentTextArea = area;
        if (area != null) {
            area.addCaretListener(e -> updateStatusBar(area));
        }
        updateStatusBar(area);
    }

    /**
     * Detaches the status bar from the current text area.
     */
    private void detachCurrentTextArea() {
        if (currentTextArea != null) {
            currentTextArea.removeCaretListener(e -> updateStatusBar(currentTextArea));
        }
    }
}
