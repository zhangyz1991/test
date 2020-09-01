package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.Resource;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@FunctionalInterface
public interface SourceExtractor {

    /**
     * Extract the source metadata from the candidate object supplied
     * by the configuration parser.
     * @param sourceCandidate the original source metadata (never {@code null})
     * @param definingResource the resource that defines the given source object
     * (may be {@code null})
     * @return the source metadata object to store (may be {@code null})
     */
    @Nullable
    Object extractSource(Object sourceCandidate, @Nullable Resource definingResource);

}
