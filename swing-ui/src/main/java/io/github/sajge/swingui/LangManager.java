package io.github.sajge.swingui;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public enum LangManager {
    INSTANCE;

    private final Map<Locale, ResourceBundle> bundles = new ConcurrentHashMap<>();
    private final Set<WeakReference<LanguageSwitchable>> components =
            Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Locale currentLocale = Locale.ENGLISH;

    public void loadBundle(String baseName) {
        bundles.put(Locale.ENGLISH, ResourceBundle.getBundle(baseName, Locale.ENGLISH));
        bundles.put(Locale.FRENCH, ResourceBundle.getBundle(baseName, Locale.FRENCH));
    }

    public String get(String key) {
        if (!bundles.get(currentLocale).containsKey(key)) {
            return "!" + key + "!";
        }
        return bundles.get(currentLocale).getString(key);
    }

    void register(LanguageSwitchable component) {
        components.add(new WeakReference<>(component));
        cleanupReferences();
    }

    void unregister(LanguageSwitchable component) {
        components.removeIf(ref ->
                ref.get() != null && ref.get().equals(component));
    }

    public void switchLanguage(Locale newLocale) {
        currentLocale = newLocale;
        components.forEach(ref -> {
            LanguageSwitchable component = ref.get();
            if (component != null) {
                component.updateLanguage();
            }
        });
        cleanupReferences();
    }

    private void cleanupReferences() {
        components.removeIf(ref -> ref.get() == null);
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }
}
