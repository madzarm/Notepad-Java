package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * A JButton that is localized, changing its text based on the current localization settings.
 */
public class LocalizedButton extends JButton {

    /**
     * Creates a LocalizedButton.
     * @param key The key for the button's text in the resource bundle.
     * @param lp The localization provider.
     */
    public LocalizedButton(String key, ILocalizationProvider lp) {
        super(new LocalizableAction(key, lp) {});
    }
}
