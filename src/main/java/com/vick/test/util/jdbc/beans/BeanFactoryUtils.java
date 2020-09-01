package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ResolvableType;
import com.vick.test.util.jdbc.framework.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public abstract class BeanFactoryUtils {

    /**
     * Separator for generated beans names. If a class name or parent name is not
     * unique, "#1", "#2" etc will be appended, until the name becomes unique.
     */
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    /**
     * Cache from name with factory beans prefix to stripped name without dereference.
     * @since 5.1
     * @see BeanFactory#FACTORY_BEAN_PREFIX
     */
    private static final Map<String, String> transformedBeanNameCache = new ConcurrentHashMap<>();


    /**
     * Return whether the given name is a factory dereference
     * (beginning with the factory dereference prefix).
     * @param name the name of the beans
     * @return whether the given name is a factory dereference
     * @see BeanFactory#FACTORY_BEAN_PREFIX
     */
    public static boolean isFactoryDereference(@Nullable String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }

    /**
     * Return the actual beans name, stripping out the factory dereference
     * prefix (if any, also stripping repeated factory prefixes if found).
     * @param name the name of the beans
     * @return the transformed name
     * @see BeanFactory#FACTORY_BEAN_PREFIX
     */
    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        if (!name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            return name;
        }
        return transformedBeanNameCache.computeIfAbsent(name, beanName -> {
            do {
                beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
            }
            while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
            return beanName;
        });
    }

    /**
     * Return whether the given name is a beans name which has been generated
     * by the default naming strategy (containing a "#..." part).
     * @param name the name of the beans
     * @return whether the given name is a generated beans name
     * @see #GENERATED_BEAN_NAME_SEPARATOR
     * see org.springframework.beans.factory.support.BeanDefinitionReaderUtils#generateBeanName
     * see org.springframework.beans.factory.support.DefaultBeanNameGenerator
     */
    public static boolean isGeneratedBeanName(@Nullable String name) {
        return (name != null && name.contains(GENERATED_BEAN_NAME_SEPARATOR));
    }

    /**
     * Extract the "raw" beans name from the given (potentially generated) beans name,
     * excluding any "#..." suffixes which might have been added for uniqueness.
     * @param name the potentially generated beans name
     * @return the raw beans name
     * @see #GENERATED_BEAN_NAME_SEPARATOR
     */
    public static String originalBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        int separatorIndex = name.indexOf(GENERATED_BEAN_NAME_SEPARATOR);
        return (separatorIndex != -1 ? name.substring(0, separatorIndex) : name);
    }


    // Retrieval of beans names

    /**
     * Count all beans in any hierarchy in which this factory participates.
     * Includes counts of ancestor beans factories.
     * <p>Beans that are "overridden" (specified in a descendant factory
     * with the same name) are only counted once.
     * @param lbf the beans factory
     * @return count of beans including those defined in ancestor factories
     * @see #beanNamesIncludingAncestors
     */
    public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesIncludingAncestors(lbf).length;
    }

    /**
     * Return all beans names in the factory, including ancestor factories.
     * @param lbf the beans factory
     * @return the array of matching beans names, or an empty array if none
     * @see #beanNamesForTypeIncludingAncestors
     */
    public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesForTypeIncludingAncestors(lbf, Object.class);
    }

    /**
     * Get all beans names for the given type, including those defined in ancestor
     * factories. Will return unique names in case of overridden beans definitions.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>This version of {@code beanNamesForTypeIncludingAncestors} automatically
     * includes prototypes and FactoryBeans.
     * @param lbf the beans factory
     * @param type the type that beans must match (as a {@code ResolvableType})
     * @return the array of matching beans names, or an empty array if none
     * @since 4.2
     * @see ListableBeanFactory#getBeanNamesForType(ResolvableType)
     */
    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, ResolvableType type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    /**
     * Get all beans names for the given type, including those defined in ancestor
     * factories. Will return unique names in case of overridden beans definitions.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit"
     * flag is set, which means that FactoryBeans will get initialized. If the
     * object created by the FactoryBean doesn't match, the raw FactoryBean itself
     * will be matched against the type. If "allowEagerInit" is not set,
     * only raw FactoryBeans will be checked (which doesn't require initialization
     * of each FactoryBean).
     * @param lbf the beans factory
     * @param type the type that beans must match (as a {@code ResolvableType})
     * @param includeNonSingletons whether to include prototype or scoped beans too
     * or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
     * <i>objects created by FactoryBeans</i> (or by factory methods with a
     * "factory-beans" reference) for the type check. Note that FactoryBeans need to be
     * eagerly initialized to determine their type: So be aware that passing in "true"
     * for this flag will initialize FactoryBeans and "factory-beans" references.
     * @return the array of matching beans names, or an empty array if none
     * @since 5.2
     * @see ListableBeanFactory#getBeanNamesForType(ResolvableType, boolean, boolean)
     */
    public static String[] beanNamesForTypeIncludingAncestors(
            ListableBeanFactory lbf, ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    /**
     * Get all beans names for the given type, including those defined in ancestor
     * factories. Will return unique names in case of overridden beans definitions.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>This version of {@code beanNamesForTypeIncludingAncestors} automatically
     * includes prototypes and FactoryBeans.
     * @param lbf the beans factory
     * @param type the type that beans must match (as a {@code Class})
     * @return the array of matching beans names, or an empty array if none
     * @see ListableBeanFactory#getBeanNamesForType(Class)
     */
    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    /**
     * Get all beans names for the given type, including those defined in ancestor
     * factories. Will return unique names in case of overridden beans definitions.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit"
     * flag is set, which means that FactoryBeans will get initialized. If the
     * object created by the FactoryBean doesn't match, the raw FactoryBean itself
     * will be matched against the type. If "allowEagerInit" is not set,
     * only raw FactoryBeans will be checked (which doesn't require initialization
     * of each FactoryBean).
     * @param lbf the beans factory
     * @param includeNonSingletons whether to include prototype or scoped beans too
     * or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
     * <i>objects created by FactoryBeans</i> (or by factory methods with a
     * "factory-beans" reference) for the type check. Note that FactoryBeans need to be
     * eagerly initialized to determine their type: So be aware that passing in "true"
     * for this flag will initialize FactoryBeans and "factory-beans" references.
     * @param type the type that beans must match
     * @return the array of matching beans names, or an empty array if none
     * @see ListableBeanFactory#getBeanNamesForType(Class, boolean, boolean)
     */
    public static String[] beanNamesForTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    /**
     * Get all beans names whose {@code Class} has the supplied {@link Annotation}
     * type, including those defined in ancestor factories, without creating any beans
     * instances yet. Will return unique names in case of overridden beans definitions.
     * @param lbf the beans factory
     * @param annotationType the type of annotation to look for
     * @return the array of matching beans names, or an empty array if none
     * @since 5.0
     * @see ListableBeanFactory#getBeanNamesForAnnotation(Class)
     */
    public static String[] beanNamesForAnnotationIncludingAncestors(
            ListableBeanFactory lbf, Class<? extends Annotation> annotationType) {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForAnnotation(annotationType);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForAnnotationIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), annotationType);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }


    // Retrieval of beans instances

    /**
     * Return all beans of the given type or subtypes, also picking up beans defined in
     * ancestor beans factories if the current beans factory is a HierarchicalBeanFactory.
     * The returned Map will only contain beans of this type.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p><b>Note: Beans of the same name will take precedence at the 'lowest' factory level,
     * i.e. such beans will be returned from the lowest factory that they are being found in,
     * hiding corresponding beans in ancestor factories.</b> This feature allows for
     * 'replacing' beans by explicitly choosing the same beans name in a child factory;
     * the beans in the ancestor factory won't be visible then, not even for by-type lookups.
     * @param lbf the beans factory
     * @param type type of beans to match
     * @return the Map of matching beans instances, or an empty Map if none
     * @throws BeansException if a beans could not be created
     * @see ListableBeanFactory#getBeansOfType(Class)
     */
    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<>(4);
        result.putAll(lbf.getBeansOfType(type));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                parentResult.forEach((beanName, beanInstance) -> {
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, beanInstance);
                    }
                });
            }
        }
        return result;
    }

    /**
     * Return all beans of the given type or subtypes, also picking up beans defined in
     * ancestor beans factories if the current beans factory is a HierarchicalBeanFactory.
     * The returned Map will only contain beans of this type.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
     * which means that FactoryBeans will get initialized. If the object created by the
     * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
     * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
     * (which doesn't require initialization of each FactoryBean).
     * <p><b>Note: Beans of the same name will take precedence at the 'lowest' factory level,
     * i.e. such beans will be returned from the lowest factory that they are being found in,
     * hiding corresponding beans in ancestor factories.</b> This feature allows for
     * 'replacing' beans by explicitly choosing the same beans name in a child factory;
     * the beans in the ancestor factory won't be visible then, not even for by-type lookups.
     * @param lbf the beans factory
     * @param type type of beans to match
     * @param includeNonSingletons whether to include prototype or scoped beans too
     * or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
     * <i>objects created by FactoryBeans</i> (or by factory methods with a
     * "factory-beans" reference) for the type check. Note that FactoryBeans need to be
     * eagerly initialized to determine their type: So be aware that passing in "true"
     * for this flag will initialize FactoryBeans and "factory-beans" references.
     * @return the Map of matching beans instances, or an empty Map if none
     * @throws BeansException if a beans could not be created
     * @see ListableBeanFactory#getBeansOfType(Class, boolean, boolean)
     */
    public static <T> Map<String, T> beansOfTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<>(4);
        result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                parentResult.forEach((beanName, beanInstance) -> {
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, beanInstance);
                    }
                });
            }
        }
        return result;
    }

    /**
     * Return a single beans of the given type or subtypes, also picking up beans
     * defined in ancestor beans factories if the current beans factory is a
     * HierarchicalBeanFactory. Useful convenience method when we expect a
     * single beans and don't care about the beans name.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>This version of {@code beanOfTypeIncludingAncestors} automatically includes
     * prototypes and FactoryBeans.
     * <p><b>Note: Beans of the same name will take precedence at the 'lowest' factory level,
     * i.e. such beans will be returned from the lowest factory that they are being found in,
     * hiding corresponding beans in ancestor factories.</b> This feature allows for
     * 'replacing' beans by explicitly choosing the same beans name in a child factory;
     * the beans in the ancestor factory won't be visible then, not even for by-type lookups.
     * @param lbf the beans factory
     * @param type type of beans to match
     * @return the matching beans instance
     * @throws NoSuchBeanDefinitionException if no beans of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one beans of the given type was found
     * @throws BeansException if the beans could not be created
     * @see #beansOfTypeIncludingAncestors(ListableBeanFactory, Class)
     */
    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException {

        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
        return uniqueBean(type, beansOfType);
    }

    /**
     * Return a single beans of the given type or subtypes, also picking up beans
     * defined in ancestor beans factories if the current beans factory is a
     * HierarchicalBeanFactory. Useful convenience method when we expect a
     * single beans and don't care about the beans name.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
     * which means that FactoryBeans will get initialized. If the object created by the
     * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
     * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
     * (which doesn't require initialization of each FactoryBean).
     * <p><b>Note: Beans of the same name will take precedence at the 'lowest' factory level,
     * i.e. such beans will be returned from the lowest factory that they are being found in,
     * hiding corresponding beans in ancestor factories.</b> This feature allows for
     * 'replacing' beans by explicitly choosing the same beans name in a child factory;
     * the beans in the ancestor factory won't be visible then, not even for by-type lookups.
     * @param lbf the beans factory
     * @param type type of beans to match
     * @param includeNonSingletons whether to include prototype or scoped beans too
     * or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
     * <i>objects created by FactoryBeans</i> (or by factory methods with a
     * "factory-beans" reference) for the type check. Note that FactoryBeans need to be
     * eagerly initialized to determine their type: So be aware that passing in "true"
     * for this flag will initialize FactoryBeans and "factory-beans" references.
     * @return the matching beans instance
     * @throws NoSuchBeanDefinitionException if no beans of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one beans of the given type was found
     * @throws BeansException if the beans could not be created
     * @see #beansOfTypeIncludingAncestors(ListableBeanFactory, Class, boolean, boolean)
     */
    public static <T> T beanOfTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
        return uniqueBean(type, beansOfType);
    }

    /**
     * Return a single beans of the given type or subtypes, not looking in ancestor
     * factories. Useful convenience method when we expect a single beans and
     * don't care about the beans name.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>This version of {@code beanOfType} automatically includes
     * prototypes and FactoryBeans.
     * @param lbf the beans factory
     * @param type type of beans to match
     * @return the matching beans instance
     * @throws NoSuchBeanDefinitionException if no beans of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one beans of the given type was found
     * @throws BeansException if the beans could not be created
     * @see ListableBeanFactory#getBeansOfType(Class)
     */
    public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type) throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type);
        return uniqueBean(type, beansOfType);
    }

    /**
     * Return a single beans of the given type or subtypes, not looking in ancestor
     * factories. Useful convenience method when we expect a single beans and
     * don't care about the beans name.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit"
     * flag is set, which means that FactoryBeans will get initialized. If the
     * object created by the FactoryBean doesn't match, the raw FactoryBean itself
     * will be matched against the type. If "allowEagerInit" is not set,
     * only raw FactoryBeans will be checked (which doesn't require initialization
     * of each FactoryBean).
     * @param lbf the beans factory
     * @param type type of beans to match
     * @param includeNonSingletons whether to include prototype or scoped beans too
     * or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
     * <i>objects created by FactoryBeans</i> (or by factory methods with a
     * "factory-beans" reference) for the type check. Note that FactoryBeans need to be
     * eagerly initialized to determine their type: So be aware that passing in "true"
     * for this flag will initialize FactoryBeans and "factory-beans" references.
     * @return the matching beans instance
     * @throws NoSuchBeanDefinitionException if no beans of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one beans of the given type was found
     * @throws BeansException if the beans could not be created
     * @see ListableBeanFactory#getBeansOfType(Class, boolean, boolean)
     */
    public static <T> T beanOfType(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        return uniqueBean(type, beansOfType);
    }


    /**
     * Merge the given beans names result with the given parent result.
     * @param result the local beans name result
     * @param parentResult the parent beans name result (possibly empty)
     * @param hbf the local beans factory
     * @return the merged result (possibly the local result as-is)
     * @since 4.3.15
     */
    private static String[] mergeNamesWithParent(String[] result, String[] parentResult, HierarchicalBeanFactory hbf) {
        if (parentResult.length == 0) {
            return result;
        }
        List<String> merged = new ArrayList<>(result.length + parentResult.length);
        merged.addAll(Arrays.asList(result));
        for (String beanName : parentResult) {
            if (!merged.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                merged.add(beanName);
            }
        }
        return StringUtils.toStringArray(merged);
    }

    /**
     * Extract a unique beans for the given type from the given Map of matching beans.
     * @param type type of beans to match
     * @param matchingBeans all matching beans found
     * @return the unique beans instance
     * @throws NoSuchBeanDefinitionException if no beans of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one beans of the given type was found
     */
    private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans) {
        int count = matchingBeans.size();
        if (count == 1) {
            return matchingBeans.values().iterator().next();
        }
        else if (count > 1) {
            throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
        }
        else {
            throw new NoSuchBeanDefinitionException(type);
        }
    }

}
