package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Interface defining a localization provider that offers methods for retrieving localized strings and managing listeners.
 */
public interface ILocalizationProvider {

    /**
     * Retrieves a localized string for the given key.
     * @param key The key for the desired localized string.
     * @return The localized string.
     */
    String getString(String key);

    /**
     * Retrieves the current language.
     * @return The current language.
     */
    String getLanguage();

    /**
     * Adds a localization listener to this provider.
     * @param listener The listener to be added for localization changes.
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes a localization listener from this provider.
     * @param listener The listener to be removed.
     */
    void removeLocalizationListener(ILocalizationListener listener);
}
