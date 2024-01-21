package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * A localization provider for forms, extending the LocalizationProviderBridge.
 * It also listens to window events to manage localization resources based on window state.
 */
public class FormLocalizationProvider extends LocalizationProviderBridge implements WindowListener {

    /**
     * Creates a FormLocalizationProvider.
     * @param provider The localization provider.
     * @param frame The frame to attach the window listener to.
     */
    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);
        frame.addWindowListener(this);
        connect();
    }

    /**
     * Invoked when a window has been opened. Connects the localization provider.
     * @param e The window event.
     */
    @Override
    public void windowOpened(WindowEvent e) {
        connect();
    }

    /**
     * Invoked when a window is in the process of being closed. Does not perform any specific operation.
     * @param e The window event.
     */
    @Override
    public void windowClosing(WindowEvent e) {}

    /**
     * Invoked when a window has been closed. Disconnects the localization provider.
     * @param e The window event.
     */
    @Override
    public void windowClosed(WindowEvent e) {
        disconnect();
    }

    /**
     * Invoked when a window is iconified. Does not perform any specific operation.
     * @param e The window event.
     */
    @Override
    public void windowIconified(WindowEvent e) {}

    /**
     * Invoked when a window is de-iconified. Does not perform any specific operation.
     * @param e The window event.
     */
    @Override
    public void windowDeiconified(WindowEvent e) {}

    /**
     * Invoked when a window is activated. Does not perform any specific operation.
     * @param e The window event.
     */
    @Override
    public void windowActivated(WindowEvent e) {}

    /**
     * Invoked when a window is deactivated. Does not perform any specific operation.
     * @param e The window event.
     */
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
