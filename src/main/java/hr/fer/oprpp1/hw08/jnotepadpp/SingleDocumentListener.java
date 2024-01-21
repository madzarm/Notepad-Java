package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Listener interface for changes in single document models.
 */
public interface SingleDocumentListener {

    /**
     * Called when the document's modify status is updated.
     * @param model The single document model whose status was updated.
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Called when the document's file path is updated.
     * @param model The single document model whose file path was updated.
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
