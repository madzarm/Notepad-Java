package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedButton;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedMenuItem;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Main application frame for JNotepad++, a multiple document text editor.
 */
public class JNotepadPP extends JFrame {

    /**
     * Model handling multiple documents.
     */
    private MultipleDocumentModel multipleDocumentModel;

    /**
     * Localization provider for form localization.
     */
    private FormLocalizationProvider flp;

    /**
     * A map for storing buttons with their corresponding actions.
     */
    private Map<String, AbstractButton> buttons = new HashMap<>();
    private JToolBar toolBar;


    /**
     * Constructs the JNotepadPP main frame.
     */
    public JNotepadPP() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initGui();
    }

    /**
     * Initializes the graphical user interface.
     */
    private void initGui() {
        multipleDocumentModel = new DefaultMultipleDocumentModel();
        getContentPane().add(multipleDocumentModel.getVisualComponent(), BorderLayout.CENTER);

        flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        addListeners();
        createMenus();
        createToolbar();
        createStatusBar();

        setTitle("JNotepad++");
        setSize(800, 600);
        setLocationRelativeTo(null);

        updateButtonsEnableStatus();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    /**
     * Creates and initializes the toolbar.
     */
    private void createToolbar() {
        toolBar = new JToolBar();
        addToolButton("new", e -> handleNew());
        addToolButton("open", e -> handleOpen());
        addToolButton("save", e -> handleSave());
        addToolButton("save_as", e -> handleSaveAs());
        addToolButton("cut", e -> handleCut());
        addToolButton("copy", e -> handleCopy());
        addToolButton("paste", e -> handlePaste());
        addToolButton("statistics", e -> handleStats());
        addToolButton("close", e -> handleClose());
        addToolButton("exit", e -> handleWindowClosing());

        add(toolBar, BorderLayout.NORTH);
    }

    private void addToolButton(String actionCommand, ActionListener actionListener) {
        JButton button = new LocalizedButton(actionCommand, flp);
        button.addActionListener(actionListener);
        toolBar.add(button);
    }


    /**
     * Creates and initializes the menus.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LocalizedMenu("file", flp);
        menuBar.add(fileMenu);

        JMenuItem newDocument = new LocalizedMenuItem("new", flp);
        newDocument.addActionListener(e -> handleNew());
        fileMenu.add(newDocument);

        JMenuItem openDocument = new LocalizedMenuItem("open", flp);
        openDocument.addActionListener(e -> handleOpen());
        fileMenu.add(openDocument);

        JMenuItem saveDocument = new LocalizedMenuItem("save", flp);
        saveDocument.addActionListener(e -> handleSave());
        fileMenu.add(saveDocument);

        JMenuItem saveAsDocument = new LocalizedMenuItem("save_as", flp);
        saveAsDocument.addActionListener(e -> handleSaveAs());
        fileMenu.add(saveAsDocument);

        JMenuItem statistics = new LocalizedMenuItem("statistics", flp);
        statistics.addActionListener(e -> handleStats());
        fileMenu.add(statistics);

        JMenuItem closeDocument = new LocalizedMenuItem("close", flp);
        closeDocument.addActionListener(e -> handleClose());
        fileMenu.add(closeDocument);

        JMenuItem exit = new LocalizedMenuItem("exit", flp);
        exit.addActionListener(e -> handleWindowClosing());
        fileMenu.add(exit);

        JMenu editMenu = new LocalizedMenu("edit", flp);
        menuBar.add(editMenu);

        JMenuItem cutText = new LocalizedMenuItem("cut", flp);
        cutText.addActionListener(e -> handleCut());
        editMenu.add(cutText);

        JMenuItem copyText = new LocalizedMenuItem("copy", flp);
        copyText.addActionListener(e -> handleCopy());
        editMenu.add(copyText);

        JMenuItem pasteText = new LocalizedMenuItem("paste", flp);
        pasteText.addActionListener(e -> handlePaste());
        editMenu.add(pasteText);

        JMenu toolsMenu = new LocalizedMenu("tools", flp);
        menuBar.add(toolsMenu);

        JMenu changeCase = new LocalizedMenu("change_case", flp);
        toolsMenu.add(changeCase);

        JMenuItem toUppercase = new LocalizedMenuItem("to_uppercase", flp);
        modifyTextCase(toUppercase, String::toUpperCase);
        changeCase.add(toUppercase);
        buttons.put("to_uppercase", toUppercase);

        JMenuItem toLowercase = new LocalizedMenuItem("to_lowercase", flp);
        modifyTextCase(toLowercase, String::toLowerCase);
        changeCase.add(toLowercase);
        buttons.put("to_lowercase", toLowercase);

        JMenuItem invertCase = new LocalizedMenuItem("invert_case", flp);
        modifyTextCase(invertCase, text -> {
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = Character.isLowerCase(chars[i]) ? Character.toUpperCase(chars[i]) : Character.toLowerCase(chars[i]);
            }
            return new String(chars);
        });
        changeCase.add(invertCase);
        buttons.put("invert_case", invertCase);

        JMenu sort = new LocalizedMenu("sort", flp);
        toolsMenu.add(sort);

        JMenuItem ascending = new LocalizedMenuItem("ascending", flp);
        ascending.addActionListener(e -> handleSortAscending());
        sort.add(ascending);
        buttons.put("ascending", ascending);

        JMenuItem descending = new LocalizedMenuItem("descending", flp);
        descending.addActionListener(e -> handleSortDescending());
        sort.add(descending);
        buttons.put("descending", descending);

        JMenuItem unique = new LocalizedMenuItem("unique", flp);
        unique.addActionListener(e -> handleUnique());
        toolsMenu.add(unique);
        buttons.put("unique", unique);

        JMenu langMenu = new LocalizedMenu("languages", flp);
        menuBar.add(langMenu);

        JMenuItem hr = new JMenuItem("hr");
        JMenuItem en = new JMenuItem("en");
        JMenuItem de = new JMenuItem("de");

        langMenu.add(hr);
        langMenu.add(en);
        langMenu.add(de);

        hr.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("hr"));
        en.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("en"));
        de.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("de"));

        JMenu ispitMenu = new JMenu("menu");
        menuBar.add(ispitMenu);


        fileMenu.add(newDocument);

        setJMenuBar(menuBar);
    }

    private List<Integer> getDocumentLengths() {
        List<Integer> lengths = new ArrayList<>();
        for (int i = 0; i < multipleDocumentModel.getNumberOfDocuments(); i++) {
            lengths.add(multipleDocumentModel.getDocument(i).getTextComponent().getText().length());
        }
        return lengths;
    }

    /**
     * Handles the 'unique' action.
     */
    private void handleUnique() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea == null) return;

        try {
            int start = textArea.getLineStartOffset(textArea.getLineOfOffset(textArea.getSelectionStart()));
            int end = textArea.getLineEndOffset(textArea.getLineOfOffset(textArea.getSelectionEnd()));
            String selectedText = textArea.getText(start, end - start);

            String[] lines = selectedText.split("\\r?\\n");
            Set<String> uniqueLines = new LinkedHashSet<>();
            Collections.addAll(uniqueLines, lines);

            StringBuilder sb = new StringBuilder();
            for (String line : uniqueLines) {
                sb.append(line).append("\n");
            }

            textArea.replaceRange(sb.toString(), start, end);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the 'sort ascending' action.
     */
    private void handleSortAscending() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea == null) return;

        Comparator<String> ascendingComparator = (s1, s2) ->
                Collator.getInstance(new Locale(flp.getLanguage())).compare(s1, s2);
        sortTextArea(textArea, ascendingComparator);
    }

    /**
     * Handles the 'sort descending' action.
     */
    private void handleSortDescending() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea == null) return;

        Comparator<String> descendingComparator = (s1, s2) ->
                Collator.getInstance(new Locale(flp.getLanguage())).compare(s2, s1);
        sortTextArea(textArea, descendingComparator);
    }

    /**
     * Sorts the text area's content using the provided comparator.
     * @param textArea The JTextArea to sort.
     * @param comparator The comparator to use for sorting.
     */
    private void sortTextArea(JTextArea textArea, Comparator<String> comparator) {
        try {
            int start = textArea.getLineStartOffset(textArea.getLineOfOffset(textArea.getSelectionStart()));
            int end = textArea.getLineEndOffset(textArea.getLineOfOffset(textArea.getSelectionEnd()));
            String selectedText = textArea.getText(start, end - start);

            String[] lines = selectedText.split("\\r?\\n");
            Arrays.sort(lines, comparator);

            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append("\n");
            }

            textArea.replaceRange(sb.toString(), start, end);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies text case based on the provided function.
     * @param menuItem The menu item associated with the action.
     * @param caseModifier The function to apply for case modification.
     */
    private void modifyTextCase(JMenuItem menuItem, Function<String, String> caseModifier) {
        menuItem.addActionListener(e -> {
            JTextArea textArea = getCurrentTextArea();
            if (textArea != null) {
                String text = textArea.getSelectedText();
                if (text != null) {
                    textArea.replaceSelection(caseModifier.apply(text));
                }
            }
        });
    }

    /**
     * Creates and initializes the status bar.
     */
    private void createStatusBar() {
        StatusBar statusBar = StatusBar.getInstance();
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Handles the action for creating a new document.
     */
    private void handleNew() {
        multipleDocumentModel.createNewDocument();
    }

    /**
     * Handles the action for opening a document.
     */
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

    /**
     * Saves the current document.
     * @param currentDoc The current document to save.
     */
    private void handleSave(SingleDocumentModel currentDoc) {
        handleSaveForDoc(currentDoc);
    }

    /**
     * Saves the current document. This method is used by both handleSave and handleSaveAs.
     */
    private void handleSave() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        handleSaveForDoc(currentDoc);
    }

    /**
     * Handles saving a document, given a SingleDocumentModel.
     * @param currentDoc The document to be saved.
     */
    private void handleSaveForDoc(SingleDocumentModel currentDoc) {
        if (currentDoc == null) return;

        Path newFilePath = currentDoc.getFilePath();
        if (newFilePath == null) {
            newFilePath = chooseFilePath();
            if (newFilePath == null) return;
        }

        saveDocument(currentDoc, newFilePath);
    }

    /**
     * Handles the action for saving a document with a new path.
     */
    private void handleSaveAs() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return;

        Path newFilePath = chooseFilePath();
        if (newFilePath == null) return;

        saveDocument(currentDoc, newFilePath);
    }

    /**
     * Handles the action for closing the current document.
     */
    private void handleClose() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return;

        if (checkForUnsavedChanges()) {
            multipleDocumentModel.closeDocument(currentDoc);
        }
    }

    /**
     * Handles the action for cutting text from the document.
     */
    private void handleCut() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.cut();
        }
    }

    /**
     * Handles the action for copying text from the document.
     */
    private void handleCopy() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.copy();
        }
    }

    /**
     * Handles the action for pasting text into the document.
     */
    private void handlePaste() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.paste();
        }
    }

    /**
     * Handles the action for displaying statistics about the current document.
     */
    private void handleStats() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            String text = textArea.getText();
            int numOfChars = text.length();
            int numOfNonBlankChars = text.replaceAll("\\s+", "").length();
            int numOfLines = textArea.getLineCount();

            String messagePattern = flp.getString("stats_message");
            String formattedMessage = MessageFormat.format(messagePattern, numOfChars, numOfNonBlankChars, numOfLines);

            JOptionPane.showMessageDialog(
                    this,
                    formattedMessage,
                    flp.getString("statistics"),
                    JOptionPane.INFORMATION_MESSAGE
            );
        	}
    }

    /**
     * Retrieves the current document's JTextArea.
     * @return The JTextArea of the current document.
     */
    private JTextArea getCurrentTextArea() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return null;

        return currentDoc.getTextComponent();
    }

    /**
     * Chooses a file path for saving a document.
     * @return The selected file path.
     */
    private Path chooseFilePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle(flp.getString("save_as"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.toPath();
        } else {
            return null;
        }
    }

    /**
     * Saves the specified document to the given path.
     * @param doc The document to save.
     * @param filePath The path to save the document.
     */
    private void saveDocument(SingleDocumentModel doc, Path filePath) {
        try {
            multipleDocumentModel.saveDocument(doc, filePath);
            doc.setFilePath(filePath);
        } catch (Exception ex) {
            String formattedMessage = flp.getString("save_error_message") + ": " + ex.getMessage();
            JOptionPane.showMessageDialog(
                    this,
                    formattedMessage,
                    flp.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Adds listeners to various components and models.
     */
    private void addListeners() {
        multipleDocumentModel.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateWindowTitle();
                updateStatusBar();
                updateButtonsEnableStatus();
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                updateWindowTitle();
                updateStatusBar();
                updateButtonsEnableStatus();
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                updateWindowTitle();
                updateStatusBar();
                updateButtonsEnableStatus();
            }
        });
    }


    /**
     * Updates the enable status of buttons based on the current state.
     */
    private void updateButtonsEnableStatus() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea != null) {
            textArea.addCaretListener(e -> {
                boolean hasSelection = textArea.getSelectionStart() != textArea.getSelectionEnd();
                for (AbstractButton button : buttons.values()) {
                    button.setEnabled(hasSelection);
                }
            });
        } else {
            for (AbstractButton button : buttons.values()) {
                button.setEnabled(false);
            }
        }
    }

    /**
     * Updates the status bar.
     */
    private void updateStatusBar() {
        JTextArea textArea = getCurrentTextArea();
        StatusBar.getInstance().attachToTextArea(textArea);
    }

    /**
     * Updates the window title based on the current document.
     */
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

    /**
     * Checks for unsaved changes in the current document.
     * @return True if it's safe to proceed, false otherwise.
     */
    private boolean checkForUnsavedChanges() {
        if (getCurrentDocument() != null && getCurrentDocument().isModified()) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    flp.getString("unsaved_changes_message"),
                    flp.getString("unsaved_changes"),
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

    /**
     * Retrieves the name of the document from the provided document model.
     * @param documentModel The document model.
     * @return The name of the document.
     */
    private String getDocumentName(SingleDocumentModel documentModel) {
        if (documentModel.getFilePath() != null) {
            return documentModel.getFilePath().getFileName().toString();
        } else {
            return "unnamed";
        }
    }

    /**
     * Checks and handles unsaved documents before closing.
     * @return True if it's safe to close, false otherwise.
     */
    private boolean checkAndHandleUnsavedDocuments() {
        boolean shouldDispose = true;
        for (SingleDocumentModel doc : multipleDocumentModel) {
            if (doc.isModified()) {

                String formattedMessage = MessageFormat.format(
                        flp.getString("unsaved_changes_message_var"),
                        getDocumentName(doc)
                );

                Object[] options = {
                        flp.getString("yes"),
                        flp.getString("no"),
                        flp.getString("cancel")
                };

                int result = JOptionPane.showOptionDialog(
                        this,
                        formattedMessage,
                        flp.getString("unsaved_changes"),
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (result == JOptionPane.YES_OPTION) {
                    handleSave(doc);
                } else shouldDispose = result == JOptionPane.NO_OPTION;
            }
        }
        return shouldDispose;
    }

    /**
     * Handles the window closing event.
     */
    private void handleWindowClosing() {
        if (checkAndHandleUnsavedDocuments()) {
            dispose();
        }
    }

    /**
     * Retrieves the current document model.
     * @return The current SingleDocumentModel.
     */
    private SingleDocumentModel getCurrentDocument() {
        return multipleDocumentModel.getCurrentDocument();
    }

    /**
     * The main method to start the JNotepadPP application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }
}
