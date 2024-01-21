package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

public class LocalizedMenuItem extends JMenuItem {

    public LocalizedMenuItem(String key, ILocalizationProvider lp) {
        super(new LocalizableAction(key, lp) {});
    }
}
