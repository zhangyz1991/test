package com.vick.test.util.jdbc.framework.annotation;

import com.vick.test.util.jdbc.framework.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
enum IntrospectionFailureLogger {

    DEBUG {
        @Override
        public boolean isEnabled() {
            return getLogger().isDebugEnabled();
        }
        @Override
        public void log(String message) {
            getLogger().debug(message);
        }
    },

    INFO {
        @Override
        public boolean isEnabled() {
            return getLogger().isInfoEnabled();
        }
        @Override
        public void log(String message) {
            getLogger().info(message);
        }
    };


    @Nullable
    private static Logger logger;


    void log(String message, @Nullable Object source, Exception ex) {
        String on = (source != null ? " on " + source : "");
        log(message + on + ": " + ex);
    }

    abstract boolean isEnabled();

    abstract void log(String message);


    private static Logger getLogger() {
        Logger logger = IntrospectionFailureLogger.logger;
        if (logger == null) {
            logger = LoggerFactory.getLogger(MergedAnnotation.class);
            IntrospectionFailureLogger.logger = logger;
        }
        return logger;
    }

}
