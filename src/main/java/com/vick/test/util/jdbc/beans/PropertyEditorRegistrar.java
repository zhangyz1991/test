package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface PropertyEditorRegistrar {

    /**
     * Register custom {@link java.beans.PropertyEditor PropertyEditors} with
     * the given {@code PropertyEditorRegistry}.
     * <p>The passed-in registry will usually be a {link BeanWrapper} or a
     * {link org.springframework.validation.DataBinder DataBinder}.
     * <p>It is expected that implementations will create brand new
     * {@code PropertyEditors} instances for each invocation of this
     * method (since {@code PropertyEditors} are not threadsafe).
     * @param registry the {@code PropertyEditorRegistry} to register the
     * custom {@code PropertyEditors} with
     */
    void registerCustomEditors(PropertyEditorRegistry registry);

}
