package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedButton;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedMenuItem;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class JNotepadPP extends JFrame {

    private MultipleDocumentModel multipleDocumentModel;
    private FormLocalizationProvider flp;
    private Map<String, AbstractButton> buttons = new HashMap<>();

    public JNotepadPP() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initGui();
    }

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

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();

        JButton newButton = new LocalizedButton("new", flp);
        newButton.addActionListener(e -> handleNew());
        toolBar.add(newButton);

        JButton openButton = new LocalizedButton("open", flp);
        openButton.addActionListener(e -> handleOpen());
        toolBar.add(openButton);

        JButton saveButton = new LocalizedButton("save", flp);
        saveButton.addActionListener(e -> handleSave());
        toolBar.add(saveButton);

        JButton saveAsButton = new LocalizedButton("save_as", flp);
        saveAsButton.addActionListener(e -> handleSaveAs());
        toolBar.add(saveAsButton);

        JButton cutButton = new LocalizedButton("cut", flp);
        cutButton.addActionListener(e -> handleCut());
        toolBar.add(cutButton);

        JButton copyButton = new LocalizedButton("copy", flp);
        copyButton.addActionListener(e -> handleCopy());
        toolBar.add(copyButton);

        JButton pasteButton = new LocalizedButton("paste", flp);
        pasteButton.addActionListener(e -> handlePaste());
        toolBar.add(pasteButton);

        JButton statsButton = new LocalizedButton("statistics", flp);
        statsButton.addActionListener(e -> handleStats());
        toolBar.add(statsButton);

        JButton closeButton = new LocalizedButton("close", flp);
        closeButton.addActionListener(e -> handleClose());
        toolBar.add(closeButton);

        JButton exitButton = new LocalizedButton("exit", flp);
        exitButton.addActionListener(e -> handleWindowClosing());
        toolBar.add(exitButton);

        add(toolBar, BorderLayout.NORTH);
    }


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

        setJMenuBar(menuBar);
    }

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

    private void handleSortAscending() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea == null) return;

        Comparator<String> ascendingComparator = (s1, s2) ->
                Collator.getInstance(new Locale(flp.getLanguage())).compare(s1, s2);
        sortTextArea(textArea, ascendingComparator);
    }

    private void handleSortDescending() {
        JTextArea textArea = getCurrentTextArea();
        if (textArea == null) return;

        Comparator<String> descendingComparator = (s1, s2) ->
                Collator.getInstance(new Locale(flp.getLanguage())).compare(s2, s1);
        sortTextArea(textArea, descendingComparator);
    }

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

    private JTextArea getCurrentTextArea() {
        SingleDocumentModel currentDoc = getCurrentDocument();
        if (currentDoc == null) return null;

        return currentDoc.getTextComponent();
    }



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
