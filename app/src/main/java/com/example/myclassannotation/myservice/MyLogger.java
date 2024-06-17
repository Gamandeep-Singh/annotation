package com.example.myclassannotation.myservice;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {

    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE)
    public @interface MyLoggerAnnotation {
    }

    public static void configure(Class<?> clazz) {
        MyLoggerAnnotation annotation = clazz.getAnnotation(MyLoggerAnnotation.class);

        if (annotation != null) {
            // Since the annotation has no argument, logging level remains at INFO
        } else {
            logger.setLevel(Level.INFO); // Default level if no annotation
        }
    }

    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    public static void debug(String message) {
        logger.log(Level.FINE, message); // Use FINE for debug-like messages
    }

    public static void warn(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void error(String message) {
        logger.log(Level.SEVERE, message);
    }

    public static void trace(String message) {
        logger.log(Level.FINEST, message); // Use FINEST for trace messages
    }
}
