package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

    private static final LocalizationProvider instance = new LocalizationProvider();
    private ResourceBundle bundle;
    private String language;

    private LocalizationProvider() {
        this.language = "en";
        loadResourceBundle();
    }

    public static LocalizationProvider getInstance() {
        return instance;
    }

    private void loadResourceBundle() {
        Locale locale = new Locale(language);
        bundle = ResourceBundle.getBundle("prijevodi", locale);
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getLanguage() {
        return bundle.getLocale().getLanguage();
    }

    public void setLanguage(String language) {
        this.language = language;
        loadResourceBundle();
        fire();
    }
}
