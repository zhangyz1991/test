package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ResolvableType;
import com.vick.test.util.jdbc.framework.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class NoUniqueBeanDefinitionException extends NoSuchBeanDefinitionException {

    private final int numberOfBeansFound;

    @Nullable
    private final Collection<String> beanNamesFound;


    /**
     * Create a new {@code NoUniqueBeanDefinitionException}.
     * @param type required type of the non-unique beans
     * @param numberOfBeansFound the number of matching beans
     * @param message detailed message describing the problem
     */
    public NoUniqueBeanDefinitionException(Class<?> type, int numberOfBeansFound, String message) {
        super(type, message);
        this.numberOfBeansFound = numberOfBeansFound;
        this.beanNamesFound = null;
    }

    /**
     * Create a new {@code NoUniqueBeanDefinitionException}.
     * @param type required type of the non-unique beans
     * @param beanNamesFound the names of all matching beans (as a Collection)
     */
    public NoUniqueBeanDefinitionException(Class<?> type, Collection<String> beanNamesFound) {
        super(type, "expected single matching beans but found " + beanNamesFound.size() + ": " +
                StringUtils.collectionToCommaDelimitedString(beanNamesFound));
        this.numberOfBeansFound = beanNamesFound.size();
        this.beanNamesFound = beanNamesFound;
    }

    /**
     * Create a new {@code NoUniqueBeanDefinitionException}.
     * @param type required type of the non-unique beans
     * @param beanNamesFound the names of all matching beans (as an array)
     */
    public NoUniqueBeanDefinitionException(Class<?> type, String... beanNamesFound) {
        this(type, Arrays.asList(beanNamesFound));
    }

    /**
     * Create a new {@code NoUniqueBeanDefinitionException}.
     * @param type required type of the non-unique beans
     * @param beanNamesFound the names of all matching beans (as a Collection)
     * @since 5.1
     */
    public NoUniqueBeanDefinitionException(ResolvableType type, Collection<String> beanNamesFound) {
        super(type, "expected single matching beans but found " + beanNamesFound.size() + ": " +
                StringUtils.collectionToCommaDelimitedString(beanNamesFound));
        this.numberOfBeansFound = beanNamesFound.size();
        this.beanNamesFound = beanNamesFound;
    }

    /**
     * Create a new {@code NoUniqueBeanDefinitionException}.
     * @param type required type of the non-unique beans
     * @param beanNamesFound the names of all matching beans (as an array)
     * @since 5.1
     */
    public NoUniqueBeanDefinitionException(ResolvableType type, String... beanNamesFound) {
        this(type, Arrays.asList(beanNamesFound));
    }


    /**
     * Return the number of beans found when only one matching beans was expected.
     * For a NoUniqueBeanDefinitionException, this will usually be higher than 1.
     * @see #getBeanType()
     */
    @Override
    public int getNumberOfBeansFound() {
        return this.numberOfBeansFound;
    }

    /**
     * Return the names of all beans found when only one matching beans was expected.
     * Note that this may be {@code null} if not specified at construction time.
     * @since 4.3
     * @see #getBeanType()
     */
    @Nullable
    public Collection<String> getBeanNamesFound() {
        return this.beanNamesFound;
    }

}
