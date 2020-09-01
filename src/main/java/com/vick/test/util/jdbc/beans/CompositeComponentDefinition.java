package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class CompositeComponentDefinition extends AbstractComponentDefinition {

    private final String name;

    @Nullable
    private final Object source;

    private final List<ComponentDefinition> nestedComponents = new ArrayList<>();


    /**
     * Create a new CompositeComponentDefinition.
     * @param name the name of the composite component
     * @param source the source element that defines the root of the composite component
     */
    public CompositeComponentDefinition(String name, @Nullable Object source) {
        Assert.notNull(name, "Name must not be null");
        this.name = name;
        this.source = source;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @Nullable
    public Object getSource() {
        return this.source;
    }


    /**
     * Add the given component as nested element of this composite component.
     * @param component the nested component to add
     */
    public void addNestedComponent(ComponentDefinition component) {
        Assert.notNull(component, "ComponentDefinition must not be null");
        this.nestedComponents.add(component);
    }

    /**
     * Return the nested components that this composite component holds.
     * @return the array of nested components, or an empty array if none
     */
    public ComponentDefinition[] getNestedComponents() {
        return this.nestedComponents.toArray(new ComponentDefinition[0]);
    }

}
