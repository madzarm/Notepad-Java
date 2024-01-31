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
                // Handle the change of current document if needed
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                updateListModel(); // Update list model when a document is added
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                updateListModel(); // Update list model when a document is removed
            }
        });
    }

    private void updateListModel() {
        listModel.clear(); // Clear the existing elements
        for (int i = 0; i < this.model.getNumberOfDocuments(); i++) {
            // Use your existing method to get the path or placeholder text
            String path = getElementAt(i);
            listModel.addElement(path); // Add the path to the list model
        }
    }

    private String getElementAt(int index) {
        if (model.getDocument(index).getFilePath() == null) {
            return "unnamed " + index;
        }
        return model.getDocument(index).getFilePath().toString();
    }
}
