package com.vick.test.util.jdbc.beans;

import java.util.EventListener;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface ReaderEventListener extends EventListener {

    /**
     * Notification that the given defaults has been registered.
     * @param defaultsDefinition a descriptor for the defaults
     * see org.springframework.beans.factory.xml.DocumentDefaultsDefinition
     */
    void defaultsRegistered(DefaultsDefinition defaultsDefinition);

    /**
     * Notification that the given component has been registered.
     * @param componentDefinition a descriptor for the new component
     * see BeanComponentDefinition
     */
    void componentRegistered(ComponentDefinition componentDefinition);

    /**
     * Notification that the given alias has been registered.
     * @param aliasDefinition a descriptor for the new alias
     */
    void aliasRegistered(AliasDefinition aliasDefinition);

    /**
     * Notification that the given import has been processed.
     * @param importDefinition a descriptor for the import
     */
    void importProcessed(ImportDefinition importDefinition);

}
