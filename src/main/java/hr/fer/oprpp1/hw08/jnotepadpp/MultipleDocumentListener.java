package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Listener interface for changes in multiple document models.
 */
public interface MultipleDocumentListener {

    /**
     * Called when the current document changes.
     * @param previousModel The previous single document model.
     * @param currentModel The current single document model.
     */
    void currentDocumentChanged(SingleDocumentModel previousModel,
                                SingleDocumentModel currentModel);

    /**
     * Called when a new document is added.
     * @param model The added single document model.
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Called when a document is removed.
     * @param model The removed single document model.
     */
    void documentRemoved(SingleDocumentModel model);
}
