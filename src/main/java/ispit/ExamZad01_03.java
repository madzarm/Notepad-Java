package ispit;

import hr.fer.oprpp1.hw08.jnotepadpp.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class ExamZad01_03 extends JDialog {

    private MultipleDocumentModel model;
    private DefaultListModel<String> listModel;

    public ExamZad01_03(MultipleDocumentModel model) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(false);
        this.model = model;
        this.listModel = new DefaultListModel<>();
        initGUI();
        pack();
        addListeners();
    }

    private void initGUI() {
        JList<String> list = new JList<>(listModel);
        updateListModel();
        add(list);
    }

    private void addListeners() {
        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                updateListModel();
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                updateListModel();
            }
        });
    }

    private void updateListModel() {
        listModel.clear();
        for (int i = 0; i < this.model.getNumberOfDocuments(); i++) {
            String path = getElementAt(i);
            listModel.addElement(path);
        }
    }

    private String getElementAt(int index) {
        if (model.getDocument(index).getFilePath() == null) {
            return "unnamed " + index;
        }
        return model.getDocument(index).getFilePath().toString();
    }
}
