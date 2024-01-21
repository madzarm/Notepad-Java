package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * A bridge class for localization providers, managing the connection and disconnection of localization listeners.
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * The decorated localization provider.
     */
    private ILocalizationProvider decoratedProvider;

    /**
     * Flag indicating whether the bridge is connected.
     */
    private boolean isConnected = false;

    /**
     * The current language.
     */
    private String currentLanguage;

    /**
     * Internal listener for language change events.
     */
    private ILocalizationListener internalListener = this::onParentLanguageChanged;

    /**
     * Constructs a LocalizationProviderBridge with a specified provider.
     * @param provider The localization provider to be decorated.
     */
    public LocalizationProviderBridge(ILocalizationProvider provider) {
        this.decoratedProvider = provider;
        this.currentLanguage = provider.getLanguage();
    }

    /**
     * Connects this bridge to its decorated provider, enabling localization updates.
     */
    public void connect() {
        if (!isConnected) {
            decoratedProvider.addLocalizationListener(internalListener);
            isConnected = true;
            checkAndNotifyLanguageChange();
        }
    }

    /**
     * Disconnects this bridge from its decorated provider, stopping localization updates.
     */
    public void disconnect() {
        if (isConnected) {
            decoratedProvider.removeLocalizationListener(internalListener);
            isConnected = false;
        }
    }

    /**
     * Called when the parent's language changes. Updates the current language if necessary.
     */
    private void onParentLanguageChanged() {
        String newLanguage = decoratedProvider.getLanguage();
        if (!newLanguage.equals(currentLanguage)) {
            changeLanguage(newLanguage);
        }
    }

    /**
     * Checks the parent's language and notifies if a change occurred.
     */
    private void checkAndNotifyLanguageChange() {
        String parentLanguage = decoratedProvider.getLanguage();
        if (!parentLanguage.equals(currentLanguage)) {
            changeLanguage(parentLanguage);
        }
    }

    /**
     * Retrieves a localized string for the given key, if connected.
     * @param key The key for the desired localized string.
     * @return The localized string or null if not connected.
     */
    @Override
    public String getString(String key) {
        return isConnected ? decoratedProvider.getString(key) : null;
    }

    /**
     * Retrieves the current language of this provider.
     * @return The current language.
     */
    @Override
    public String getLanguage() {
        return currentLanguage;
    }

    /**
     * Changes the current language and notifies listeners.
     * @param newLanguage The new language to be set.
     */
    private void changeLanguage(String newLanguage) {
        this.currentLanguage = newLanguage;
        fire();
    }
}
