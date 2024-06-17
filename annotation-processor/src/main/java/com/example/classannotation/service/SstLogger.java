package com.example.classannotation.service;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SstLogger {
    private static Logger logger;

    private final String classname;

    /***
     * Constructor
     * @param className: Source Class calling Logger instance
     */
    public SstLogger(String className) {
        classname = className;
        setLogger();
    }

    public String getClassname(){
        return this.classname;
    }

    public void setLogger(){
        logger = LoggerFactory.getLogger(this.classname);
    }

    public Logger getLogger(){
        return this.logger;
    }

    /**
     * Determines and returns the current logging level.
     *
     * <p>This method checks the logging configuration to determine the highest enabled
     * logging level. The order of logging levels from highest to lowest is:
     * TRACE, DEBUG, INFO, WARN, ERROR, and OFF. If no logging levels are enabled,
     * the method defaults to {@link Level#OFF}.
     *
     * @return The highest enabled logging level.
     * @see Level
     */
    public Level getLogLevel() {
        if (logger.isTraceEnabled()) {
            return Level.TRACE;
        } else if (logger.isDebugEnabled()) {
            return Level.DEBUG;
        } else if (logger.isInfoEnabled()) {
            return Level.INFO;
        } else if (logger.isWarnEnabled()) {
            return Level.WARN;
        } else if (logger.isErrorEnabled()) {
            return Level.ERROR;
        } else {
            return Level.OFF;
        }
    }

    public void logTrace(String message) {
        logger.trace(message);
    }

    public void logDebug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void logInfo(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void logError(String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public void logWarn(String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

}
