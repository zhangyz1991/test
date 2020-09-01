package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.io.AbstractResource;
import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
class BeanDefinitionResource extends AbstractResource {

    private final BeanDefinition beanDefinition;


    /**
     * Create a new BeanDefinitionResource.
     * @param beanDefinition the BeanDefinition object to wrap
     */
    public BeanDefinitionResource(BeanDefinition beanDefinition) {
        Assert.notNull(beanDefinition, "BeanDefinition must not be null");
        this.beanDefinition = beanDefinition;
    }

    /**
     * Return the wrapped BeanDefinition object.
     */
    public final BeanDefinition getBeanDefinition() {
        return this.beanDefinition;
    }


    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean isReadable() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new FileNotFoundException(
                "Resource cannot be opened because it points to " + getDescription());
    }

    @Override
    public String getDescription() {
        return "BeanDefinition defined in " + this.beanDefinition.getResourceDescription();
    }


    /**
     * This implementation compares the underlying BeanDefinition.
     */
    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof BeanDefinitionResource &&
                ((BeanDefinitionResource) other).beanDefinition.equals(this.beanDefinition)));
    }

    /**
     * This implementation returns the hash code of the underlying BeanDefinition.
     */
    @Override
    public int hashCode() {
        return this.beanDefinition.hashCode();
    }

}
