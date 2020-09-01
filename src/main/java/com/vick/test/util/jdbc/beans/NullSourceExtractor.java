package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.Resource;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class NullSourceExtractor implements SourceExtractor {

    /**
     * This implementation simply returns {@code null} for any input.
     */
    @Override
    @Nullable
    public Object extractSource(Object sourceCandidate, @Nullable Resource definitionResource) {
        return null;
    }

}
