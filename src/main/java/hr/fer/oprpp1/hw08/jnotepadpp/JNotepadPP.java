package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * App must be executable from here
 */
public class JNotepadPP extends JFrame {

    private MultipleDocumentModel multipleDocumentModel;
    private ImageIcon unmodifiedIcon;
    private ImageIcon modifiedIcon;

    public JNotepadPP() {
        unmodifiedIcon = resizeIcon(loadIcon("green-diskette.png"), 16, 16);
        modifiedIcon = resizeIcon(loadIcon("red-diskette.png"), 16, 16);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initGui();
    }

    private ImageIcon loadIcon(String iconName) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(iconName);
        if (is == null) {
            throw new RuntimeException("Icon file not found: " + iconName);
        }
        try {
            byte[] bytes = is.readAllBytes();
            return new ImageIcon(bytes);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading icon file: " + iconName, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
        openDocument.addActionListener(e -> handleOpen());
        fileMenu.add(openDocument);

        JMenuItem saveDocument = new JMenuItem("Save");
        saveDocument.addActionListener(e -> handleSave());
        fileMenu.add(saveDocument);

        JMenuItem saveAsDocument = new JMenuItem("Save As...");
        saveAsDocument.addActionListener(e -> handleSaveAs());
        fileMenu.add(saveAsDocument);

        JMenuItem closeDocument = new JMenuItem("Close");
        closeDocument.addActionListener(e -> handleClose());
        fileMenu.add(closeDocument);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenuItem cutText = new JMenuItem("Cut");
        cutText.addActionListener(e -> handleCut());
        editMenu.add(cutText);

        JMenuItem copyText = new JMenuItem("Copy");
        copyText.addActionListener(e -> handleCopy());
        editMenu.add(copyText);

        JMenuItem pasteText = new JMenuItem("Paste");
        pasteText.addActionListener(e -> handlePaste());
        editMenu.add(pasteText);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());

        add(statusBar, BorderLayout.SOUTH);
    }

    private void handleNew() {
        createDocumentAndSetFocus();
        updateWindowTitle();
    }

    private void handleOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Open file");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            var currentModel = multipleDocumentModel.loadDocument(selectedFile.toPath());
            addIconListener(currentModel);
            updateTabIcon(currentModel);
            setFocus(currentModel);
        }
    }

    private void handleSave() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return;

        Path newFilePath = currentDoc.getFilePath();
        if (newFilePath == null) {
            newFilePath = chooseFilePath();
            if (newFilePath == null) return;
        }

        saveDocument(currentDoc, newFilePath);
    }

    private void handleSaveAs() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return;

        Path newFilePath = chooseFilePath();
        if (newFilePath == null) return;

        saveDocument(currentDoc, newFilePath);
    }

    private void handleClose() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return;

        if (checkForUnsavedChanges()) {
            multipleDocumentModel.closeDocument(currentDoc);
        }
    }

    private void handleCut() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.cut();
        }
    }

    private void handleCopy() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.copy();
        }
    }

    private void handlePaste() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.paste();
        }
    }

    private JTextArea getCurrentTextArea() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return null;

        return currentDoc.getTextComponent();
    }



    private Path chooseFilePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Save file");
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.toPath();
        } else {
            return null;
        }
    }

    private void saveDocument(SingleDocumentModel doc, Path filePath) {
        try {
            multipleDocumentModel.saveDocument(doc, filePath);
            doc.setFilePath(filePath);
            updateTabIcon(doc);
            updateTabName(String.valueOf(filePath.getFileName()), doc);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error while saving file: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateTabName(String newName, SingleDocumentModel currentDoc) {
        if (newName != null && !newName.isBlank()) {
            ((DefaultMultipleDocumentModel) multipleDocumentModel).setTitleAt(
                    multipleDocumentModel.getIndexOfDocument(currentDoc),
                    newName
            );
        }
    }

    private void createDocumentAndSetFocus() {
        SingleDocumentModel currentDoc = multipleDocumentModel.createNewDocument();
        updateTabIcon(currentDoc);
        addIconListener(currentDoc);
        setFocus(currentDoc);
    }

    private void setFocus(SingleDocumentModel currentDoc) {
        int index = multipleDocumentModel.getIndexOfDocument(currentDoc);
        ((DefaultMultipleDocumentModel) multipleDocumentModel).setSelectedIndex(index);
    }

    private void updateTabIcon(SingleDocumentModel model) {
        int index = multipleDocumentModel.getIndexOfDocument(model);
        if (model.isModified()) {
            ((DefaultMultipleDocumentModel) multipleDocumentModel).setIconAt(index, modifiedIcon);
        } else {
            ((DefaultMultipleDocumentModel) multipleDocumentModel).setIconAt(index, unmodifiedIcon);
        }
    }

    private void addIconListener(SingleDocumentModel currentDoc) {
        currentDoc.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                updateTabIcon(model);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                updateTabIcon(model);
            }
        });
    }

    private void updateWindowTitle() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        String title = currentDoc != null && currentDoc.getFilePath() != null
                ? currentDoc.getFilePath().toString()
                : "unnamed";
        setTitle(title + " - JNotepad++");
    }

    private boolean checkForUnsavedChanges() {
        if (getCurrentDocument() != null && getCurrentDocument().isModified()) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "The current document has unsaved changes. Do you want to save them?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                handleSave();
                return true; // Proceed after saving
            } else if (result == JOptionPane.NO_OPTION) {
                return true; // Proceed without saving
            } else {
                return false;
            }
        }
        return true;
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private SingleDocumentModel getCurrentDocument() {
        return multipleDocumentModel.getCurrentDocument();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }
}
