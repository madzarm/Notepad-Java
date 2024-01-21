package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * A JMenu that is localized, changing its text based on the current localization settings.
 */
public class LocalizedMenu extends JMenu {

    /**
     * Creates a LocalizedMenu.
     * @param key The key for the menu's text in the resource bundle.
     * @param lp The localization provider.
     */
    public LocalizedMenu(String key, ILocalizationProvider lp) {
            super(new LocalizableAction(key, lp) {});
        }
}
