package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private List<SingleDocumentModel> documentModels = new ArrayList<>();
    private SingleDocumentModel currentDocument;
    private List<MultipleDocumentListener> multipleDocumentListeners = new ArrayList<>();

    public DefaultMultipleDocumentModel() {
        this.addChangeListener(e -> {
            int selectedIndex = this.getSelectedIndex();
            if (selectedIndex != -1) {
                setCurrentDocument(documentModels.get(selectedIndex));
            }
        });
    }

    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel newDoc = new DefaultSingleDocumentModel(null, "");
        documentModels.add(newDoc);
        addTab("untitled", new JScrollPane(newDoc.getTextComponent()));
        setCurrentDocument(newDoc);
        return newDoc;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

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
        documentModels.add(newDoc);
        addTab(path.getFileName().toString(), new JScrollPane(newDoc.getTextComponent()));
        setCurrentDocument(newDoc);
        // Add any necessary listeners to newDoc
        return newDoc;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = documentModels.indexOf(model);
        if (index != -1) {
            removeTabAt(index);
            documentModels.remove(index);
            if (currentDocument == model) {
                currentDocument = documentModels.isEmpty() ? null : documentModels.get(0);
                // Notify listeners about the change if necessary
            }
        }
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        multipleDocumentListeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        multipleDocumentListeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documentModels.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documentModels.get(index);
    }

    @Override
    public SingleDocumentModel findForPath(Path path) {
        for (SingleDocumentModel doc : documentModels) {
            if (doc.getFilePath() != null && doc.getFilePath().equals(path)) {
                return doc;
            }
        }
        return null;
    }

    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documentModels.indexOf(doc);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documentModels.iterator();
    }

    public void setCurrentDocument(SingleDocumentModel currentDocument) {
        this.currentDocument = currentDocument;
    }
}
