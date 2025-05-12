package io.github.sajge.swingui;

import java.util.concurrent.CopyOnWriteArrayList;

public enum LangChange {
    INST;

    private boolean isEnglish = true;
    private final CopyOnWriteArrayList<Runnable> actions = new CopyOnWriteArrayList<>();

    public boolean isEnglish() {
        return isEnglish;
    }

    public void register(Runnable action) {
        actions.add(action);
    }

    public void unregister(Runnable action) {
        actions.remove(action);
    }

    public void toggle() {
        isEnglish = !isEnglish;
        for (Runnable action : actions) {
            action.run();
        }
    }

    public void clear() {
        actions.clear();
    }
}
