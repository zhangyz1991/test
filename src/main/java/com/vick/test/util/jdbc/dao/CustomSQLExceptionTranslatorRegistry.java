package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public final class CustomSQLExceptionTranslatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(CustomSQLExceptionTranslatorRegistry.class);

    /**
     * Keep track of a single instance so we can return it to classes that request it.
     */
    private static final CustomSQLExceptionTranslatorRegistry instance = new CustomSQLExceptionTranslatorRegistry();


    /**
     * Return the singleton instance.
     */
    public static CustomSQLExceptionTranslatorRegistry getInstance() {
        return instance;
    }


    /**
     * Map registry to hold custom translators specific databases.
     * Key is the database product name as defined in the
     * {link org.springframework.jdbc.support.SQLErrorCodesFactory}.
     */
    private final Map<String, SQLExceptionTranslator> translatorMap = new HashMap<>();


    /**
     * Create a new instance of the {@link CustomSQLExceptionTranslatorRegistry} class.
     * <p>Not public to enforce Singleton design pattern.
     */
    private CustomSQLExceptionTranslatorRegistry() {
    }


    /**
     * Register a new custom translator for the specified database name.
     * @param dbName the database name
     * @param translator the custom translator
     */
    public void registerTranslator(String dbName, SQLExceptionTranslator translator) {
        SQLExceptionTranslator replaced = this.translatorMap.put(dbName, translator);
        if (logger.isDebugEnabled()) {
            if (replaced != null) {
                logger.debug("Replacing custom translator [" + replaced + "] for database '" + dbName +
                        "' with [" + translator + "]");
            }
            else {
                logger.debug("Adding custom translator of type [" + translator.getClass().getName() +
                        "] for database '" + dbName + "'");
            }
        }
    }

    /**
     * Find a custom translator for the specified database.
     * @param dbName the database name
     * @return the custom translator, or {@code null} if none found
     */
    @Nullable
    public SQLExceptionTranslator findTranslatorForDatabase(String dbName) {
        return this.translatorMap.get(dbName);
    }

}
