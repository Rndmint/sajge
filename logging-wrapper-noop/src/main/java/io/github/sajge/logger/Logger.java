package io.github.sajge.logger;

public class Logger {
    private Logger(Class<?> clazz) {
    }

    public static Logger get(Class<?> clazz) {
        return new Logger(clazz);
    }

    public boolean isTraceEnabled() {
        return false;
    }

    public void trace(String msg, Object... args) {
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public void debug(String msg, Object... args) {
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public void info(String msg, Object... args) {
    }

    public boolean isWarnEnabled() {
        return false;
    }

    public void warn(String msg, Object... args) {
    }

    public boolean isErrorEnabled() {
        return false;
    }

    public void error(String msg, Object... args) {
    }

    public void error(String msg, Throwable t) {
    }
}
