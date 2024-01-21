package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

public class LocalizedMenu extends JMenu {

        public LocalizedMenu(String key, ILocalizationProvider lp) {
            super(new LocalizableAction(key, lp) {});
        }
}
