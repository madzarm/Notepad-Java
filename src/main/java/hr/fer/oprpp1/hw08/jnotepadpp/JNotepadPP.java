package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * App must be executable from here
 */
public class JNotepadPP extends JFrame {

    private MultipleDocumentModel multipleDocumentModel;

    public JNotepadPP() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        multipleDocumentModel = new DefaultMultipleDocumentModel();
        getContentPane().add(multipleDocumentModel.getVisualComponent(), BorderLayout.CENTER);

        createMenus();
        createToolbar();
        createStatusBar();

        setTitle("JNotepad++");
        setSize(800, 600); // Set an appropriate size
        setLocationRelativeTo(null); // Center the window

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Implement closing logic (check for unsaved documents, etc.)
            }
        });
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
// Add buttons for common actions (New, Open, Save, etc.)
// Add action listeners to buttons

        add(toolBar, BorderLayout.NORTH);
    }


    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newDocument = new JMenuItem("New");
        newDocument.addActionListener(e -> handleNew());
        fileMenu.add(newDocument);

        JMenuItem openDocument = new JMenuItem("Open");
        openDocument.addActionListener(e -> {

        });
        fileMenu.add(openDocument);

        JMenuItem saveDocument = new JMenuItem("Save");
        saveDocument.addActionListener(e -> {

        });
        fileMenu.add(saveDocument);

        JMenuItem saveAsDocument = new JMenuItem("Save As...");
        saveAsDocument.addActionListener(e -> {

        });
        fileMenu.add(saveAsDocument);

        JMenuItem closeDocument = new JMenuItem("Close");
        closeDocument.addActionListener(e -> {

        });
        fileMenu.add(closeDocument);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenuItem cutText = new JMenuItem("Cut");
        cutText.addActionListener(e -> {

        });
        editMenu.add(cutText);

        JMenuItem copyText = new JMenuItem("Copy");
        copyText.addActionListener(e -> {

        });
        editMenu.add(copyText);

        JMenuItem pasteText = new JMenuItem("Paste");
        pasteText.addActionListener(e -> {

        });
        editMenu.add(pasteText);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void handleNew() {
        createDocumentAndSetFocus();
        updateWindowTitle();
    }

    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());

        add(statusBar, BorderLayout.SOUTH);
    }

    private boolean checkForUnsavedChanges() {
        if (multipleDocumentModel.getCurrentDocument() != null && multipleDocumentModel.getCurrentDocument().isModified()) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "The current document has unsaved changes. Do you want to save them?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                // Implement saving the current document
                return true; // Proceed after saving
            } else if (result == JOptionPane.NO_OPTION) {
                return true; // Proceed without saving
            } else {
                return false;
            }
        }
        return true;
    }

    private void updateWindowTitle() {
        SingleDocumentModel currentDoc = multipleDocumentModel.getCurrentDocument();
        String title = currentDoc != null && currentDoc.getFilePath() != null
                ? currentDoc.getFilePath().toString()
                : "unnamed";
        setTitle(title + " - JNotepad++");
    }

    private void createDocumentAndSetFocus() {
        SingleDocumentModel currentDoc = multipleDocumentModel.createNewDocument();
        int index = multipleDocumentModel.getIndexOfDocument(currentDoc);
        ((DefaultMultipleDocumentModel) multipleDocumentModel).setSelectedIndex(index);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }
}
