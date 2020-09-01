package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class EmptyReaderEventListener implements ReaderEventListener {

    @Override
    public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
        // no-op
    }

    @Override
    public void componentRegistered(ComponentDefinition componentDefinition) {
        // no-op
    }

    @Override
    public void aliasRegistered(AliasDefinition aliasDefinition) {
        // no-op
    }

    @Override
    public void importProcessed(ImportDefinition importDefinition) {
        // no-op
    }

}
