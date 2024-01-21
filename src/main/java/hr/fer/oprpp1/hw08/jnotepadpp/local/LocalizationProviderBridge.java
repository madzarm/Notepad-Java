package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * decorator class that manages connection status
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider implements ILocalizationProvider {

    private ILocalizationProvider decoratedProvider;
    private boolean isConnected = false;
    private String currentLanguage;
    private ILocalizationListener internalListener = this::onParentLanguageChanged;


    public LocalizationProviderBridge(ILocalizationProvider provider) {
        this.decoratedProvider = provider;
        this.currentLanguage = provider.getLanguage();
    }

    public void connect() {
        if (!isConnected) {
            decoratedProvider.addLocalizationListener(internalListener);
            isConnected = true;
            checkAndNotifyLanguageChange();
        }
    }

    public void disconnect() {
        if (isConnected) {
            decoratedProvider.removeLocalizationListener(internalListener);
            isConnected = false;
        }
    }

    private void onParentLanguageChanged() {
        String newLanguage = decoratedProvider.getLanguage();
        if (!newLanguage.equals(currentLanguage)) {
            changeLanguage(newLanguage);
        }
    }

    private void checkAndNotifyLanguageChange() {
        String parentLanguage = decoratedProvider.getLanguage();
        if (!parentLanguage.equals(currentLanguage)) {
            changeLanguage(parentLanguage);
        }
    }

    @Override
    public String getString(String key) {
        return isConnected ? decoratedProvider.getString(key) : null;
    }

    @Override
    public String getLanguage() {
        return currentLanguage;
    }

    private void changeLanguage(String newLanguage) {
        this.currentLanguage = newLanguage;
        fire();
    }
}
