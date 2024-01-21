package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An action that is localized, changing its name based on the current localization settings.
 */
public class LocalizableAction extends AbstractAction {

    /**
     * The key for the action's name in the resource bundle.
     */
    private String key;

    /**
     * The localization provider.
     */
    private ILocalizationProvider lp;

    /**
     * Creates a LocalizableAction.
     * @param key The key for the action's name in the resource bundle.
     * @param lp The localization provider.
     */
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

    /**
     * Updates the action's name based on the current localization.
     */
    private void updateActionName() {
        String translation = lp.getString(key);
        putValue(NAME, translation);
    }

    /**
     * Not Implemented
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
