package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel{

    private Path filePath;
    private JTextArea textArea;
    private boolean modified;
    private List<SingleDocumentListener> listeners = new ArrayList<>();

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
    }

    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path path) {
        this.filePath = path;
        notifyPathUpdated();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        if (this.modified != modified) {
            this.modified = modified;
            notifyModifyStatusChanged();
        }
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    private void notifyModifyStatusChanged() {
        listeners.forEach(listener ->
                listener.documentModifyStatusUpdated(DefaultSingleDocumentModel.this));
    }

    private void notifyPathUpdated() {
        listeners.forEach(listener ->
                listener.documentFilePathUpdated(DefaultSingleDocumentModel.this));
    }
}
