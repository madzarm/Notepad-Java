package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Interface defining a model for a single document.
 */
public interface SingleDocumentModel {

    /**
     * Returns the text component of this document.
     * @return The JTextArea containing the document's content.
     */
    JTextArea getTextComponent();

    /**
     * Retrieves the file path of the document.
     * @return The Path representing the file path.
     */
    Path getFilePath();

    /**
     * Sets the file path of the document.
     * @param path The new file path for the document.
     */
    void setFilePath(Path path);

    /**
     * Checks if the document has been modified.
     * @return True if the document is modified, false otherwise.
     */
    boolean isModified();

    /**
     * Sets the modification status of the document.
     * @param modified True to mark the document as modified, false otherwise.
     */
    void setModified(boolean modified);

    /**
     * Adds a listener that is notified of single document changes.
     * @param l The listener to add.
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes a listener from being notified of single document changes.
     * @param l The listener to remove.
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}

