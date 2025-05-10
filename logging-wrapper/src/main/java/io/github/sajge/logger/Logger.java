package io.github.sajge.logger;

import org.slf4j.LoggerFactory;

public class Logger {
    private final org.slf4j.Logger delegate;

    private Logger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }

    public static Logger get(Class<?> clazz) {
        return new Logger(clazz);
    }

    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    public void trace(String msg, Object... args) {
        delegate.trace(msg, args);
    }

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    public void debug(String msg, Object... args) {
        delegate.debug(msg, args);
    }

    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    public void info(String msg, Object... args) {
        delegate.info(msg, args);
    }

    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    public void warn(String msg, Object... args) {
        delegate.warn(msg, args);
    }

    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    public void error(String msg, Object... args) {
        delegate.error(msg, args);
    }

    public void error(String msg, Throwable t) {
        delegate.error(msg, t);
    }
}
