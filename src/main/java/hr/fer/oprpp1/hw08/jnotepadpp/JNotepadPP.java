package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;

public class JNotepadPP extends JFrame {

    private MultipleDocumentModel multipleDocumentModel;

    public JNotepadPP() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        //
        multipleDocumentModel = new DefaultMultipleDocumentModel();
        getContentPane().add(multipleDocumentModel.getVisualComponent(), BorderLayout.CENTER);
        addListeners();
        createMenus();
        createToolbar();
        createStatusBar();

        setTitle("JNotepad++");
        setSize(800, 600);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();

        JButton newButton = new JButton("New");
        newButton.addActionListener(e -> handleNew());
        toolBar.add(newButton);

        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> handleOpen());
        toolBar.add(openButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> handleSave());
        toolBar.add(saveButton);

        JButton saveAsButton = new JButton("Save As...");
        saveAsButton.addActionListener(e -> handleSaveAs());
        toolBar.add(saveAsButton);

        JButton cutButton = new JButton("Cut");
        cutButton.addActionListener(e -> handleCut());
        toolBar.add(cutButton);

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> handleCopy());
        toolBar.add(copyButton);

        JButton pasteButton = new JButton("Paste");
        pasteButton.addActionListener(e -> handlePaste());
        toolBar.add(pasteButton);

        JButton statsButton = new JButton("Statistics");
        statsButton.addActionListener(e -> handleStats());
        toolBar.add(statsButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> handleClose());
        toolBar.add(closeButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> handleWindowClosing());
        toolBar.add(exitButton);

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

        JMenuItem statistics = new JMenuItem("Statistics");
        statistics.addActionListener(e -> handleStats());
        fileMenu.add(statistics);

        JMenuItem closeDocument = new JMenuItem("Close");
        closeDocument.addActionListener(e -> handleClose());
        fileMenu.add(closeDocument);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> handleWindowClosing());
        fileMenu.add(exit);

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
        StatusBar statusBar = StatusBar.getInstance();
        add(statusBar, BorderLayout.SOUTH);
    }

    private void handleNew() {
        multipleDocumentModel.createNewDocument();
    }

    private void handleOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Open file");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            multipleDocumentModel.loadDocument(selectedFile.toPath());
        }
    }

    private void handleSave(SingleDocumentModel currentDoc) {
        handleSaveForDoc(currentDoc);
    }

    private void handleSave() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        handleSaveForDoc(currentDoc);
    }

    private void handleSaveForDoc(SingleDocumentModel currentDoc) {
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

    private void handleStats() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            String text = textArea.getText();
            int numOfChars = text.length();
            int numOfNonBlankChars = text.replaceAll("\\s+", "").length();
            int numOfLines = textArea.getLineCount();

            JOptionPane.showMessageDialog(
                    this,
                    "Your document has " + numOfChars + " characters, " + numOfNonBlankChars + " non-blank characters and " + numOfLines + " lines.",
                    "Statistics",
                    JOptionPane.INFORMATION_MESSAGE
            );
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error while saving file: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void addListeners() {
        multipleDocumentModel.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateWindowTitle();
                updateStatusBar();
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                updateWindowTitle();
                updateStatusBar();
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                updateWindowTitle();
                updateStatusBar();
            }
        });
    }


    private void updateStatusBar() {
        JTextArea textArea = getCurrentTextArea();
        StatusBar.getInstance().attachToTextArea(textArea);
    }

    private void updateWindowTitle() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) {
            setTitle("JNotepad++");
            return;
        }
        String title = currentDoc.getFilePath() != null
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
                return true;
            } else return result == JOptionPane.NO_OPTION;
        }
        return true;
    }

    private String getDocumentName(SingleDocumentModel documentModel) {
        if (documentModel.getFilePath() != null) {
            return documentModel.getFilePath().getFileName().toString();
        } else {
            return "unnamed";
        }
    }

    private boolean checkAndHandleUnsavedDocuments() {
        boolean shouldDispose = true;
        for (SingleDocumentModel doc : multipleDocumentModel) {
            if (doc.isModified()) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "The document '" + getDocumentName(doc) + "' has unsaved changes. Do you want to save them?",
                        "Unsaved Changes",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (result == JOptionPane.YES_OPTION) {
                    handleSave(doc);
                } else shouldDispose = result == JOptionPane.NO_OPTION;
            }
        }
        return shouldDispose;
    }

    private void handleWindowClosing() {
        if (checkAndHandleUnsavedDocuments()) {
            dispose();
        }
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
