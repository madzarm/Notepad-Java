package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements a tabbed pane model for handling multiple document interfaces.
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    /**
     * A list of single document models.
     */
    private final List<SingleDocumentModel> documentModels = new ArrayList<>();

    /**
     * A list of single document models.
     */
    private SingleDocumentModel currentDocument;

    /**
     * Listeners for multiple document events.
     */
    private final List<MultipleDocumentListener> multipleDocumentListeners = new ArrayList<>();

    /**
     * Icon used to represent an unmodified document.
     */
    private ImageIcon unmodifiedIcon;

    /**
     * Icon used to represent a modified document.
     */
    private ImageIcon modifiedIcon;

    /**
     * Constructs a DefaultMultipleDocumentModel and initializes its components.
     */
    public DefaultMultipleDocumentModel() {
        unmodifiedIcon = resizeIcon(loadIcon("green-diskette.png"), 16, 16);
        modifiedIcon = resizeIcon(loadIcon("red-diskette.png"), 16, 16);
        this.addChangeListener(e -> {
            int selectedIndex = this.getSelectedIndex();
            if (selectedIndex != -1) {
                setCurrentDocument(documentModels.get(selectedIndex));
            }
        });
    }

    /**
     * Returns the visual component of this model.
     * @return The visual component.
     */
    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    /**
     * Creates a new document.
     * @return The newly created single document model.
     */
    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel newDoc = new DefaultSingleDocumentModel(null, "");
        setupDocumentTab(newDoc, "unnamed");
        return newDoc;
    }

    /**
     * Retrieves the current document.
     * @return The current single document model.
     */
    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    /**
     * Loads a document from the specified path.
     * @param path The path of the document to load.
     * @return The loaded single document model.
     */
    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null.");
        }
        for (SingleDocumentModel doc : documentModels) {
            if (doc.getFilePath() != null && doc.getFilePath().equals(path)) {
                setCurrentDocument(doc);
                setSelectedIndex(documentModels.indexOf(doc));
                return doc;
            }
        }
        String content = "";
        try {
            content = new String(Files.readAllBytes(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, content);
        setupDocumentTab(newDoc, path.getFileName().toString());

        return newDoc;
    }

    /**
     * Sets up the tab for the given document.
     * @param document The document to set up a tab for.
     * @param title The title for the tab.
     */
    private void setupDocumentTab(SingleDocumentModel document, String title) {
        documentModels.add(document);
        addTab(title, new JScrollPane(document.getTextComponent()));
        setCurrentDocument(document);
        updateTabIcon(document);
        notifyDocumentAdded(document);
        setFocus(document);
        updateTabTitleAndTooltip(document, title);
        updateStatusBar(document);
    }

    /**
     * Saves the given document to the specified path.
     * @param model The single document model to save.
     * @param newPath The path to save the document to.
     */
    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        if (newPath != model.getFilePath() && newPath != null && Files.exists(newPath)) {
            throw new IllegalArgumentException("Document with the given path already exists.");
        }
        Path path = newPath != null ? newPath : model.getFilePath();
        if (path == null) {
            return;
        }

        try {
            Files.write(path, model.getTextComponent().getText().getBytes());
            model.setModified(false);
            model.setFilePath(path);
            notifyPathChanged(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the specified document.
     * @param model The single document model to close.
     */
    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = documentModels.indexOf(model);
        if (index != -1) {
            removeTabAt(index);
            documentModels.remove(index);
            if (currentDocument == model) {
                currentDocument = documentModels.isEmpty() ? null : documentModels.get(0);
            }
            notifyDocumentRemoved(model);
        }
    }

    /**
     * Adds a listener for multiple document events.
     * @param l The listener to add.
     */
    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        multipleDocumentListeners.add(l);
    }

    /**
     * Removes a listener for multiple document events.
     * @param l The listener to remove.
     */
    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        multipleDocumentListeners.remove(l);
    }

    /**
     * Returns the number of documents.
     * @return The number of documents.
     */
    @Override
    public int getNumberOfDocuments() {
        return documentModels.size();
    }

    /**
     * Retrieves the document at the specified index.
     * @param index The index of the document to retrieve.
     * @return The single document model at the specified index.
     */
    @Override
    public SingleDocumentModel getDocument(int index) {
        return documentModels.get(index);
    }

    /**
     * Finds the document for the specified path.
     * @param path The path of the document to find.
     * @return The found single document model, or null if not found.
     */
    @Override
    public SingleDocumentModel findForPath(Path path) {
        for (SingleDocumentModel doc : documentModels) {
            if (doc.getFilePath() != null && doc.getFilePath().equals(path)) {
                return doc;
            }
        }
        return null;
    }

    /**
     * Returns the index of the specified document.
     * @param doc The document to find the index for.
     * @return The index of the document.
     */
    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documentModels.indexOf(doc);
    }

    /**
     * Returns an iterator over the single document models.
     * @return An iterator.
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documentModels.iterator();
    }

    /**
     * Adds listeners to the given document.
     * @param newDoc The document to add listeners to.
     */
    private void addListeners(SingleDocumentModel newDoc) {
        newDoc.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                updateTabIcon(model);
                updateTabTitleAndTooltip(model, model.getFilePath() != null ? model.getFilePath().getFileName().toString() : "unnamed");
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                updateTabIcon(model);
                updateTabTitleAndTooltip(model, model.getFilePath() != null ? model.getFilePath().getFileName().toString() : "unnamed");
            }
        });
    }

    /**
     * Updates the status bar according to the given document model.
     * @param model The model to update the status bar for.
     */
    private void updateStatusBar(SingleDocumentModel model) {
        JTextArea textArea = model.getTextComponent();
        StatusBar.getInstance().attachToTextArea(textArea);
    }

    /**
     * Sets the current document to the specified document model.
     * @param currentDocument The document model to set as current.
     */
    public void setCurrentDocument(SingleDocumentModel currentDocument) {
        this.currentDocument = currentDocument;
        notifyPathChanged(currentDocument);
    }

    /**
     * Sets focus to the specified document model.
     * @param currentDoc The document model to set focus to.
     */
    private void setFocus(SingleDocumentModel currentDoc) {
        int index = getIndexOfDocument(currentDoc);
        setSelectedIndex(index);
    }

    /**
     * Notifies listeners that a document has been added.
     * @param model The document model that has been added.
     */
    private void notifyDocumentAdded(SingleDocumentModel model) {
        for (MultipleDocumentListener listener : multipleDocumentListeners) {
            listener.documentAdded(model);
        }
        addListeners(model);
    }

    /**
     * Notifies listeners that a document has been removed.
     * @param model The document model that has been removed.
     */
    private void notifyDocumentRemoved(SingleDocumentModel model) {
        for (MultipleDocumentListener listener : multipleDocumentListeners) {
            listener.documentRemoved(model);
        }
    }

    /**
     * Notifies listeners that the path of a document has changed.
     * @param model The document model whose path has changed.
     */
    private void notifyPathChanged(SingleDocumentModel model) {
        for (MultipleDocumentListener listener : multipleDocumentListeners) {
            listener.currentDocumentChanged(model, model);
        }

    }

    /**
     * Updates the title and tooltip of the tab for the given document.
     * @param doc The document model to update the tab for.
     * @param title The title to set for the tab.
     */
    private void updateTabTitleAndTooltip(SingleDocumentModel doc, String title) {
        int index = getIndexOfDocument(doc);
        setTitleAt(index, title);
        String tooltip = doc.getFilePath() != null ? doc.getFilePath().toString() : "unnamed";
        setToolTipTextAt(index, tooltip);
    }

    /**
     * Updates the icon of the tab for the given document model.
     * @param model The document model to update the tab icon for.
     */
    private void updateTabIcon(SingleDocumentModel model) {
        int index =getIndexOfDocument(model);
        if (model.isModified()) {
            setIconAt(index, modifiedIcon);
        } else {
            setIconAt(index, unmodifiedIcon);
        }
    }

    /**
     * Loads an icon from the specified file name.
     * @param iconName The name of the icon file to load.
     * @return The loaded ImageIcon.
     */
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

    /**
     * Resizes an icon to the specified width and height.
     * @param icon The icon to resize.
     * @param width The width to resize the icon to.
     * @param height The height to resize the icon to.
     * @return The resized ImageIcon.
     */
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }


}
