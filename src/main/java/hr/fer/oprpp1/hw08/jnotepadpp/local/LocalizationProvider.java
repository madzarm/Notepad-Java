package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class providing localization resources and functionality.
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * The singleton instance of LocalizationProvider.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * The resource bundle containing localization data.
     */
    private ResourceBundle bundle;

    /**
     * The current language.
     */
    private String language;

    /**
     * Private constructor for singleton pattern.
     */
    private LocalizationProvider() {
        this.language = "en";
        loadResourceBundle();
    }

    /**
     * Returns the singleton instance of LocalizationProvider.
     * @return The singleton instance.
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Loads the resource bundle based on the current language.
     */
    private void loadResourceBundle() {
        Locale locale = new Locale(language);
        bundle = ResourceBundle.getBundle("prijevodi", locale);
    }

    /**
     * Retrieves a string for the given key from the resource bundle.
     * @param key The key for which the localized string is required.
     * @return The localized string.
     */
    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * Gets the current language of the localization.
     * @return The current language.
     */
    @Override
    public String getLanguage() {
        return bundle.getLocale().getLanguage();
    }

    /**
     * Sets the language for localization and reloads the resource bundle.
     * @param language The new language to be set.
     */
    public void setLanguage(String language) {
        this.language = language;
        loadResourceBundle();
        fire();
    }
}
