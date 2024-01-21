package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.nio.file.Path;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    JComponent getVisualComponent();
    SingleDocumentModel createNewDocument();
    SingleDocumentModel getCurrentDocument();
    SingleDocumentModel loadDocument(Path path);
    void saveDocument(SingleDocumentModel model, Path newPath);
    void closeDocument(SingleDocumentModel model);
    void addMultipleDocumentListener(MultipleDocumentListener l);
    void removeMultipleDocumentListener(MultipleDocumentListener l);
    int getNumberOfDocuments();
    SingleDocumentModel getDocument(int index);
    SingleDocumentModel findForPath(Path path);
    int getIndexOfDocument(SingleDocumentModel doc);
}

