package io.github.sajge.desktop;

import io.github.sajge.logger.Logger;

public class AppContext {
    private static final Logger log = Logger.get(AppContext.class);

    private final MainFrame mainFrame;

    public AppContext(){
        log.debug("Creating AppContext and MainFrame");
        this.mainFrame = new MainFrame();
    }

    public MainFrame getMainFrame(){
        log.debug("getMainFrame() called");
        return mainFrame;
    }
}
