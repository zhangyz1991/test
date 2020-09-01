package com.vick.test.util.jdbc.framework.io;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ResourceUtils;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public abstract class ResourcePatternUtils {

    /**
     * Return whether the given resource location is a URL: either a
     * special "classpath" or "classpath*" pseudo URL or a standard URL.
     * @param resourceLocation the location String to check
     * @return whether the location qualifies as a URL
     * @see ResourcePatternResolver#CLASSPATH_ALL_URL_PREFIX
     * see org.springframework.util.ResourceUtils#CLASSPATH_URL_PREFIX
     * see org.springframework.util.ResourceUtils#isUrl(String)
     * @see java.net.URL
     */
    public static boolean isUrl(@Nullable String resourceLocation) {
        return (resourceLocation != null &&
                (resourceLocation.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX) ||
                        ResourceUtils.isUrl(resourceLocation)));
    }

    /**
     * Return a default {@link ResourcePatternResolver} for the given {@link ResourceLoader}.
     * <p>This might be the {@code ResourceLoader} itself, if it implements the
     * {@code ResourcePatternResolver} extension, or a default
     * {@link PathMatchingResourcePatternResolver} built on the given {@code ResourceLoader}.
     * @param resourceLoader the ResourceLoader to build a pattern resolver for
     * (may be {@code null} to indicate a default ResourceLoader)
     * @return the ResourcePatternResolver
     * @see PathMatchingResourcePatternResolver
     */
    public static ResourcePatternResolver getResourcePatternResolver(@Nullable ResourceLoader resourceLoader) {
        if (resourceLoader instanceof ResourcePatternResolver) {
            return (ResourcePatternResolver) resourceLoader;
        }
        else if (resourceLoader != null) {
            return new PathMatchingResourcePatternResolver(resourceLoader);
        }
        else {
            return new PathMatchingResourcePatternResolver();
        }
    }

}
