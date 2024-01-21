package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FormLocalizationProvider extends LocalizationProviderBridge implements WindowListener {

    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);
        frame.addWindowListener(this);
        connect();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        connect();
    }

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {
        disconnect();
    }

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
