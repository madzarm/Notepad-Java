package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * A JMenuItem that is localized, changing its text based on the current localization settings.
 */
public class LocalizedMenuItem extends JMenuItem {

    /**
     * Creates a LocalizedMenuItem.
     * @param key The key for the menu item's text in the resource bundle.
     * @param lp The localization provider.
     */
    public LocalizedMenuItem(String key, ILocalizationProvider lp) {
        super(new LocalizableAction(key, lp) {});
    }
}
