package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

public class LocalizedButton extends JButton {

    public LocalizedButton(String key, ILocalizationProvider lp) {
        super(new LocalizableAction(key, lp) {});
    }
}
