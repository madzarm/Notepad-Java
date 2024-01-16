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

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private final List<SingleDocumentModel> documentModels = new ArrayList<>();
    private SingleDocumentModel currentDocument;
    private final List<MultipleDocumentListener> multipleDocumentListeners = new ArrayList<>();
    private ImageIcon unmodifiedIcon;
    private ImageIcon modifiedIcon;

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
        addListeners(newDoc);
        notifyDocumentAdded(newDoc);
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
        addListeners(newDoc);
        notifyDocumentAdded(newDoc);
        setCurrentDocument(newDoc);
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
                notifyDocumentRemoved(model);
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

    private void addListeners(SingleDocumentModel newDoc) {
        newDoc.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                updateTabIcon(model);
                updateTabTitleAndTooltip(model, model.getFilePath() != null ? model.getFilePath().getFileName().toString() : "(unnamed)");
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                updateTabIcon(model);
                updateTabTitleAndTooltip(model, model.getFilePath() != null ? model.getFilePath().getFileName().toString() : "(unnamed)");
            }
        });
    }

    public void setCurrentDocument(SingleDocumentModel currentDocument) {
        this.currentDocument = currentDocument;
    }

    // Implement the listener methods
    private void notifyDocumentAdded(SingleDocumentModel model) {
        for (MultipleDocumentListener listener : multipleDocumentListeners) {
            listener.documentAdded(model);
        }
        addListeners(model);
    }

    private void notifyDocumentRemoved(SingleDocumentModel model) {
        for (MultipleDocumentListener listener : multipleDocumentListeners) {
            listener.documentRemoved(model);
        }
    }

    private void notifyPathChanged(SingleDocumentModel model) {
        for (MultipleDocumentListener listener : multipleDocumentListeners) {
            listener.currentDocumentChanged(model, model);
        }

    }

    private void updateTabTitleAndTooltip(SingleDocumentModel doc, String title) {
        int index = getIndexOfDocument(doc);
        setTitleAt(index, title);
        String tooltip = doc.getFilePath() != null ? doc.getFilePath().toString() : "unnamed";
        setToolTipTextAt(index, tooltip);
    }

    private void updateTabIcon(SingleDocumentModel model) {
        int index =getIndexOfDocument(model);
        if (model.isModified()) {
            setIconAt(index, modifiedIcon);
        } else {
            setIconAt(index, unmodifiedIcon);
        }
    }

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


    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }


}
