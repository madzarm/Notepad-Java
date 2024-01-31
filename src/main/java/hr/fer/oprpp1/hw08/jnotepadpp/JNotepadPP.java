package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedButton;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizedMenuItem;
import ispit.ExamZad01_02;
import ispit.ExamZad01_03;
import ispit.ExamZad01_1;
import ispit.MyComponent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
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

    private ExamZad01_03 dialog;

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

        JMenuItem zad01_1 = new JMenuItem("Zadatak 1.1.");
        zad01_1.addActionListener(e -> {
            ExamZad01_1 dialog = new ExamZad01_1();
            dialog.setVisible(true);
        });
        ispitMenu.add(zad01_1);

        JMenuItem zad1 = new JMenuItem("Zadatak 1.2.");
        zad1.addActionListener(e -> {
            ExamZad01_02 dialog = new ExamZad01_02();
            dialog.setVisible(true);
        });
        ispitMenu.add(zad1);

        JMenuItem zad2 = new JMenuItem("Zadatak 2");
        zad2.addActionListener(e -> openBarChartDialog());
        ispitMenu.add(zad2);

//        U Vaš notepad u izbornik “Ispit”, dodajte stavku “Zadatak 3.”. Odabirom te stavke treba se
//        otvoriti novi dijalog (ali uz setModal(false); tako da ga možete gledati i vratiti se u editor, a
//        da dijalog ostane prikazan). Osigurajte da se ova stavka može aktivirati samo ako u dokumentu koji
//        korisnik trenutno gleda postoji selektirani tekst (ako ne postoji, stavka treba biti i vizualno
//        onemogućena); jednom kad je korisnik aktiviranjem stavke otvorio dijalog, status selekcije nema
//        nikakvog utjecaja na rad samog dijaloga.
//        Dijalog mora biti modeliran kao vršni razred (dakle, ne smije biti ugniježđeni razred) te kroz
//        konstruktor smije dobiti isključivo referencu na MultipleDocumentModel Vašeg editora (ne
//                smije petljati po drugim internim stvarima/varijablama editora).
//                Dijalog treba prikazivati listu koja ima onoliko elemenata koliko editor ima otvorenih dokumenata,
//        a i-ta stavka treba prikazivati stazu i-tog dokumenta (ili neki specifičan tekst ako dokument još nije
//                snimljen).
//                Ovaj pogled treba biti živ, u smislu da ako korisnik otvara ili zatvara nove dokumente, ili snimi
//        dokument pa isti “dobije” stazu, i u ovoj listi se ažuriraju informacije.
//        U listi se automatski treba ažurirati i selekcija, tako da uvijek bude selektirana stavka koja odgovara
//        dokumentu koji korisnik trenutno uređuje

        JMenuItem zad3 = new JMenuItem("Zadatak 3");
        zad3.addActionListener(e -> {
            this.dialog = new ExamZad01_03(multipleDocumentModel);
            dialog.setVisible(true);
        });
        ispitMenu.add(zad3);





        fileMenu.add(newDocument);

        setJMenuBar(menuBar);
    }

    private void openBarChartDialog() {
        List<Integer> documentLengths = getDocumentLengths(); // Method to get lengths of open documents
        MyComponent barChart = new MyComponent(documentLengths);

        JDialog chartDialog = new JDialog(this, "Document Lengths Bar Chart", true); // 'this' should be your main JFrame
        chartDialog.getContentPane().add(barChart, BorderLayout.CENTER);
        chartDialog.setSize(400, 300);
        chartDialog.setLocationRelativeTo(this);
        chartDialog.setVisible(true);
    }

    private List<Integer> getDocumentLengths() {
        // Method to retrieve the lengths of all open documents in your notepad
        // This is just a placeholder, you need to implement this based on your document management logic
        List<Integer> lengths = new ArrayList<>();
        for (int i = 0; i < multipleDocumentModel.getNumberOfDocuments(); i++) {
            lengths.add(multipleDocumentModel.getDocument(i).getTextComponent().getText().length());
        }
        // For each open document, add its length to the list, e.g., lengths.add(document.getLength());
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
                updatePathsDialog();
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                updateWindowTitle();
                updateStatusBar();
                updateButtonsEnableStatus();
                updatePathsDialog();
            }
        });
    }

    private void updatePathsDialog() {
        this.dialog.revalidate();
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
