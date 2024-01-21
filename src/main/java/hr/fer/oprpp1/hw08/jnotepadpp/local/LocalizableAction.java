package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LocalizableAction extends AbstractAction {

    private String key;
    private ILocalizationProvider lp;

    public LocalizableAction(String key, ILocalizationProvider lp) {
        this.key = key;
        this.lp = lp;
        updateActionName();

        lp.addLocalizationListener(new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                updateActionName();
            }
        });
    }

    private void updateActionName() {
        String translation = lp.getString(key);
        putValue(NAME, translation);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
