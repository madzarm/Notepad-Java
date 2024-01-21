package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an abstract implementation of a localization provider.
 * It manages localization listeners and notifies them when a localization change occurs.
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * A list of listeners interested in localization changes.
     */
    List<ILocalizationListener> listeners = new ArrayList<>();

    /**
     * Adds a localization listener.
     * @param listener The listener to be added.
     */
    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a localization listener.
     * @param listener The listener to be removed.
     */
    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners about a localization change.
     */
    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }
}
