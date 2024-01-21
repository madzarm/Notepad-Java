package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Interface defining a model for handling multiple documents.
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

    /**
     * Returns the visual component of this document model.
     * @return The visual component.
     */
    JComponent getVisualComponent();

    /**
     * Creates a new document.
     * @return The new single document model.
     */
    SingleDocumentModel createNewDocument();

    /**
     * Gets the current document.
     * @return The current single document model.
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a document from the specified path.
     * @param path The path of the document to load.
     * @return The loaded single document model.
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves the specified document to the given path.
     * @param model The document to save.
     * @param newPath The path to save the document to.
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes the specified document.
     * @param model The document to close.
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds a listener for multiple document events.
     * @param l The listener to add.
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes a listener for multiple document events.
     * @param l The listener to remove.
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Gets the number of documents.
     * @return The number of documents in the model.
     */
    int getNumberOfDocuments();

    /**
     * Gets the document at the specified index.
     * @param index The index of the document.
     * @return The single document model at the specified index.
     */
    SingleDocumentModel getDocument(int index);

    /**
     * Finds the document for the given path.
     * @param path The path of the document.
     * @return The found single document model, or null if not found.
     */
    SingleDocumentModel findForPath(Path path);

    /**
     * Gets the index of the specified document.
     * @param doc The document to find the index of.
     * @return The index of the specified document.
     */
    int getIndexOfDocument(SingleDocumentModel doc);
}

