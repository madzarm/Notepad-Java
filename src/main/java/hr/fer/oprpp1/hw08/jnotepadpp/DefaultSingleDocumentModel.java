package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a model for a single document, containing the text content, path, and modification status.
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel{

    /**
     * The file path of the document.
     */
    private Path filePath;

    /**
     * The text area component holding the document's content.
     */
    private JTextArea textArea;

    /**
     * The modification status of the document.
     */
    private boolean modified;

    /**
     * A list of listeners interested in document changes.
     */
    private List<SingleDocumentListener> listeners = new ArrayList<>();

    /**
     * Constructs a DefaultSingleDocumentModel with specified file path and text content.
     * @param filePath The file path of the document.
     * @param textContent The text content of the document.
     */
    public DefaultSingleDocumentModel(Path filePath, String textContent) {
        this.modified = false;
        this.filePath = filePath;
        this.textArea = new JTextArea(textContent);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modified = true;
                notifyModifyStatusChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modified = true;
                notifyModifyStatusChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modified = true;
                notifyModifyStatusChanged();
            }
        });

        notifyPathUpdated();
        notifyModifyStatusChanged();
    }

    /**
     * Returns the text component of the document.
     * @return The JTextArea containing the document's content.
     */
    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    /**
     * Retrieves the file path of the document.
     * @return The Path representing the file path.
     */
    @Override
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path of the document.
     * @param path The new file path for the document.
     */
    @Override
    public void setFilePath(Path path) {
        this.filePath = path;
        notifyPathUpdated();
    }

    /**
     * Checks if the document has been modified.
     * @return True if the document is modified, false otherwise.
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * Sets the modification status of the document.
     * @param modified True to mark the document as modified, false otherwise.
     */
    @Override
    public void setModified(boolean modified) {
        if (this.modified != modified) {
            this.modified = modified;
            notifyModifyStatusChanged();
        }
    }

    /**
     * Adds a listener that is notified of single document changes.
     * @param l The listener to add.
     */
    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener from being notified of single document changes.
     * @param l The listener to remove.
     */
    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all listeners that the document's modify status has been updated.
     */
    private void notifyModifyStatusChanged() {
        listeners.forEach(listener ->
                listener.documentModifyStatusUpdated(DefaultSingleDocumentModel.this));
    }

    /**
     * Notifies all listeners that the document's file path has been updated.
     */
    private void notifyPathUpdated() {
        listeners.forEach(listener ->
                listener.documentFilePathUpdated(DefaultSingleDocumentModel.this));
    }
}
